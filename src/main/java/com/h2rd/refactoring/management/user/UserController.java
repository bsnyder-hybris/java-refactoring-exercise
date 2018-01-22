package com.h2rd.refactoring.management.user;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = UserController.USER_CONTROLLER_URI_PATH)
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    public static final String USER_CONTROLLER_URI_PATH = "/users";

    @Autowired
    public UserService userService;

    @Consumes("application/json")
    @RequestMapping(method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity> addUser(@Validated(User.New.class) @RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {
        log.info("AddUserThread-" + Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
            log.info("CF-AddUserThread-" + Thread.currentThread().getName());
            try {
                User createdUser = userService.saveUser(user);

                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(uriComponentsBuilder.path(UserController.USER_CONTROLLER_URI_PATH + "/{id}").buildAndExpand(createdUser.getId()).toUri());

                return new ResponseEntity(headers, HttpStatus.CREATED);
            } catch (DataIntegrityViolationException d) {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
        });
    }

    @Consumes("application/json")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public CompletableFuture updateUser(WebRequest request, @PathVariable @NotNull UUID id, @Validated(User.Existing.class) @RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {
        log.info("UpdateUserThread-" + Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
            log.info("CF-UpdateUserThread-" + Thread.currentThread().getName());
            String ifMatchHeaderValue = request.getHeader(HttpHeaders.IF_MATCH);
            if (!id.equals(user.getId())) {
                return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
            } else if (userService.getUserById(id) == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else if (ifMatchHeaderValue == null || ifMatchHeaderValue.isEmpty()){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            } else {
                User foundUser = userService.getUserById(id);

                if (!ifMatchHeaderValue.equals(foundUser.getVersion().toString())){
                    return new ResponseEntity(HttpStatus.PRECONDITION_FAILED);
                } else {
                    try {
                        User updatedUser = userService.updateUser(user);

                        HttpHeaders headers = new HttpHeaders();
                        headers.setLocation(uriComponentsBuilder.path(UserController.USER_CONTROLLER_URI_PATH + "/{id}").buildAndExpand(user.getId().toString()).toUri());
                        headers.setETag("\"" + updatedUser.getVersion() + "\"");

                        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
                    } catch (DataIntegrityViolationException d) {
                        return new ResponseEntity(HttpStatus.CONFLICT);
                    }
                }
            }
        });
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public CompletableFuture<ResponseEntity> deleteUser(@PathVariable @NotNull UUID id) {
        log.info("DeleteUserThread-" + Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
            log.info("CF-DeleteUserThread-" + Thread.currentThread().getName());
            if (userService.getUserById(id) != null) {
                userService.deleteUser(userService.getUserById(id));
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        });
    }

    @Produces("application/json")
    @RequestMapping(method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity> getAllUsers(@RequestParam("firstName") Optional<String> firstName, @RequestParam("lastName") Optional<String> lastName) {
        log.info("GetAllUsersThread-" + Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
            log.info("CF-GetAllUsersThread-" + Thread.currentThread().getName());
            List<User> users = null;

            if (firstName.isPresent() || lastName.isPresent()) {
                users = userService.findUsersByFirstNameAndLastName(firstName.orElse(""), lastName.orElse(""));
            } else {
                users = userService.findAll();
            }

            if (users.isEmpty()) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity(users, HttpStatus.OK);
            }
        });
    }

    @Produces("application/json")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<HttpEntity>> getUserById(@PathVariable @NotNull UUID id) {
        log.info("GetUserByIdThread-" + Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
            log.info("CF-GetUserByIdThread-" + Thread.currentThread().getName());
            User user = userService.getUserById(id);

            if (user == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setETag("\"" + user.getVersion() + "\"");

                return new ResponseEntity(user, headers, HttpStatus.OK);
            }
        });
    }
}
