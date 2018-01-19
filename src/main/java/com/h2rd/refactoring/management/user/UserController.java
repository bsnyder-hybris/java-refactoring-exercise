package com.h2rd.refactoring.management.user;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;

import org.h2.jdbc.JdbcSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    public UserService userService;

    @Consumes("application/json")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addUser(@Validated(User.New.class) @RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {
        try {
            User createdUser = userService.saveUser(user);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponentsBuilder.path("/users/{id}").buildAndExpand(createdUser.getId()).toUri());

            return new ResponseEntity(headers, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException d){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @Consumes("application/json")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@PathVariable @NotNull UUID id, @Validated(User.Existing.class) @RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {
        if (!id.equals(user.getId())){
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        } else if (userService.getUserById(id) == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            try {
                userService.updateUser(user);

                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId().toString()).toUri());

                return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
            } catch (DataIntegrityViolationException d){
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable @NotNull UUID id) {
        if (userService.getUserById(id) != null){
            userService.deleteUser(userService.getUserById(id));
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Produces("application/json")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllUsers(@RequestParam("firstName") Optional<String> firstName, @RequestParam("lastName") Optional<String> lastName) {
        List<User> users = null;

        if (firstName.isPresent() || lastName.isPresent()){
            users = userService.findUsersByFirstNameAndLastName(firstName.orElse(""), lastName.orElse(""));
        } else {
            users = userService.findAll();
        }

        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(users, HttpStatus.OK);
        }
    }

    @Produces("application/json")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<HttpEntity> getUserById(@PathVariable @NotNull UUID id) {
        User user = userService.getUserById(id);

        if (user == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity(user, headers, HttpStatus.OK);
        }
    }
}
