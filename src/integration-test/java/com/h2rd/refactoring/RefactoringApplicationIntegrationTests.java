package com.h2rd.refactoring;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.h2rd.refactoring.management.Role;
import com.h2rd.refactoring.management.user.*;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Category(IntegrationTest.class)
public class RefactoringApplicationIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    private List<Role> buildRoles(){
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        return roles;
    }

    private User buildAUser(String email){
        return User.builder().email(email).firstName("Steve").lastName("Blue").roles(buildRoles()).build();
    }

    @After
    public void tearDown(){
        repository.deleteAll();
        repository.flush();
    }

    @Test
    public void testAddUser(){
        ResponseEntity response = restTemplate.postForEntity(UserController.USER_CONTROLLER_URI_PATH, buildAUser("adsf@gmail.com"), User.class);
        HttpHeaders headers = response.getHeaders();

        UUID createdId = UUID.fromString(headers.getLocation().getPath().split("/")[2]);
        Assertions.assertThat(headers).isNotEmpty();
        Assertions.assertThat(headers.getLocation().toString()).asString().isNotEmpty();

        Assertions.assertThat(service.getUserById(createdId)).isNotNull();
    }

    @Test
    public  void testUpdateUser() throws JsonProcessingException {
        User savedUser = service.saveUser(buildAUser("asdf@gmail.com"));
        User updatedUser = savedUser.toBuilder().firstName("Mike").build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setIfMatch(updatedUser.getVersion().toString());
        HttpEntity request = new HttpEntity<User>(updatedUser, headers);
        restTemplate.exchange(UserController.USER_CONTROLLER_URI_PATH + "/" + updatedUser.getId().toString(), HttpMethod.PUT, request, Void.class);

        User foundUser = service.getUserById(savedUser.getId());
        Assertions.assertThat(foundUser.getVersion()).isEqualTo(1);
        Assertions.assertThat(foundUser.getFirstName()).isEqualTo("Mike");
    }

    @Test
    public void testDeleteUser(){
        User savedUser = service.saveUser(buildAUser("asdf@gmail.com"));

        restTemplate.delete(UserController.USER_CONTROLLER_URI_PATH + "/" + savedUser.getId().toString());

        Assertions.assertThat(service.getUserById(savedUser.getId())).isNull();
    }

    @Test
    public void testGetUserById(){
        User user1 = service.saveUser(buildAUser("asdf@gmail.com"));

        ResponseEntity<User> response = restTemplate.getForEntity(UserController.USER_CONTROLLER_URI_PATH + "/" + user1.getId().toString(), User.class);

        Assertions.assertThat(service.getUserById(user1.getId())).isNotNull();
        Assertions.assertThat(response.getBody().getId().toString()).isEqualTo(user1.getId().toString());
    }

    @Test
    public void testGetAllUsers(){
        User user1 = service.saveUser(buildAUser("asdf@gmail.com"));
        User user2 = service.saveUser(buildAUser("fdsa@gmail.com"));

        ResponseEntity<User[]> response = restTemplate.getForEntity(UserController.USER_CONTROLLER_URI_PATH, User[].class);

        Assertions.assertThat(service.findAll().size()).isEqualTo(response.getBody().length);
        User responseUser1 = response.getBody()[0];
        User responseUser2 = response.getBody()[1];
        Assertions.assertThat(responseUser1.getId().toString().equals(user1.getId().toString()) || responseUser1.getId().toString().equals(user2.getId().toString())).isTrue();
        Assertions.assertThat(responseUser2.getId().toString().equals(user1.getId().toString()) || responseUser2.getId().toString().equals(user2.getId().toString())).isTrue();
    }
}
