package com.h2rd.refactoring.management.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2rd.refactoring.management.Role;
import com.h2rd.refactoring.management.user.User;
import com.h2rd.refactoring.management.user.UserController;
import com.h2rd.refactoring.management.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class UserControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    User user;

    @Mock
    List<Role> roles;

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mapper = new ObjectMapper();

        roles = new ArrayList<>();
        roles.add(Role.USER);
    }

    @Test
    public void contextLoaded(){
        Assertions.assertThat(userController).isNotNull();
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception{
        UUID userId = UUID.randomUUID();
        mockMvc.perform(get("/users/" + userId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserByIdSuccessful() throws Exception{
        UUID id = UUID.randomUUID();

        user = User.builder().email("asdf@gmail.com").firstName("Steve").lastName("Green").roles(roles).id(id).build();

        Mockito.when(userService.getUserById(Mockito.any(UUID.class))).thenReturn(user);

        mockMvc.perform(get("/users/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().bytes(mapper.writeValueAsBytes(user)));
    }


    @Test
    public void testGetAllUsersNoContent() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllUsersSearchByFirstName() throws Exception {
        UUID id = UUID.randomUUID();

        user = User.builder().email("asdf@gmail.com").firstName("Steve").lastName("Blue").roles(roles).id(id).build();

        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userService.findUsersByFirstNameAndLastName("Steve", "")).thenReturn(users);

        mockMvc.perform(get("/users?firstName=Steve"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().bytes(mapper.writeValueAsBytes(users)));
    }

    @Test
    public void testGetAllUsersSearchByLastName() throws Exception {
        UUID id = UUID.randomUUID();

        user = User.builder().email("asdf@gmail.com").firstName("Steve").lastName("Blue").roles(roles).id(id).build();

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Blue").roles(roles).id(id).build());
        Mockito.when(userService.findUsersByFirstNameAndLastName("", "Blue")).thenReturn(users);

        mockMvc.perform(get("/users?lastName=Blue"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().bytes(mapper.writeValueAsBytes(users)));
    }

    @Test
    public void testGetAllUsersSearchByFirstNameAndLastNameEmptyReturnAllUsers() throws Exception {
        UUID id = UUID.randomUUID();

        user = User.builder().email("asdf@gmail.com").firstName("Steve").lastName("Blue").roles(roles).id(id).build();

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Blue").roles(roles).id(id).build());
        Mockito.when(userService.findUsersByFirstNameAndLastName("", "")).thenReturn(users);

        mockMvc.perform(get("/users?firstName=&lastName="))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().bytes(mapper.writeValueAsBytes(users)));
    }


    @Test
    public void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/users/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUserNoContent() throws Exception {
        user = User.builder().email("asdf@gmail.com").firstName("Steve").lastName("Blue").roles(roles).id(UUID.randomUUID()).build();

        Mockito.when(userService.getUserById(user.getId())).thenReturn(user);
        Mockito.doNothing().when(userService).deleteUser(Mockito.any(User.class));

        mockMvc.perform(delete("/users/" + user.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @Test
    public void testAddUser() throws Exception {
        user = User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Blue").roles(roles).build();
        User newUser = User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Blue").roles(roles).id(UUID.randomUUID()).build();

        Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(newUser);

        MvcResult result = mockMvc.perform(post("/users").content(mapper.writeValueAsBytes(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Assertions.assertThat(result.getResponse().getHeader("Location")).containsPattern(Pattern.compile("^http://localhost/users/"));

        String [] uri = result.getResponse().getHeader("Location").split("/");
        String uuid = uri[uri.length-1];
        String uuidMinusDashes = uuid.replace("-", "");

        assertThat(uuidMinusDashes).containsPattern(Pattern.compile("[A-z0-9]{32}"));
    }


    @Test
    public void testUpdateUser() throws Exception {
        user = User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Blue").id(UUID.randomUUID()).roles(roles).build();

        Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(userService.getUserById(Mockito.any(UUID.class))).thenReturn(user);

        MvcResult result = mockMvc.perform(put("/users/" + user.getId()).content(mapper.writeValueAsBytes(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertThat(result.getResponse().getHeader("Location")).containsPattern(Pattern.compile("^http://localhost/users/"));

        String [] uri = result.getResponse().getHeader("Location").split("/");
        String uuid = uri[uri.length-1];
        String uuidMinusDashes = uuid.replace("-", "");

        assertThat(uuidMinusDashes).containsPattern(Pattern.compile("[A-z0-9]{32}"));
    }

    @Test
    public void testUpdateUserDifferentIds() throws Exception {
        user = User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Blue").id(UUID.randomUUID()).roles(roles).build();

        mockMvc.perform(put("/users/" + UUID.randomUUID()).content(mapper.writeValueAsBytes(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void testUpdateUserDoesNotExist() throws Exception {
        user = User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Blue").id(UUID.randomUUID()).roles(roles).build();

        mockMvc.perform(put("/users/" + user.getId()).content(mapper.writeValueAsBytes(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
