package com.h2rd.refactoring.management.user;

import com.h2rd.refactoring.management.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> findUsersByFirstNameAndLastName(String firstName, String lastName);

    void deleteUser(User user);

    User updateUser(User user);

    User saveUser(User user);

    User getUserById(UUID id);

    User getUserByEmail(String email);

    List<User> findAll();
}
