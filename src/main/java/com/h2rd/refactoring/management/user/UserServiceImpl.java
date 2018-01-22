package com.h2rd.refactoring.management.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {

    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository repository;

    public List<User> findUsersByFirstNameAndLastName(String firstName, String lastName){
        log.info("Running search by first and last name: " + firstName + " " + lastName);
        return repository.findUsersByFirstNameContainingAndLastNameContaining(firstName, lastName);
    }

    public void deleteUser(User user) {
        log.info("Attempting to delete user with id: " + user.getId().toString());
        repository.delete(user);
    }

    public User updateUser(User user) {
        return saveUser(user);
    }

    public User saveUser(User user) {
        log.info("Attempting to save user with email: " + user.getEmail());
        return repository.saveAndFlush(user);
    }

    public User getUserById(UUID id) {
        log.info("Attempting to retrieve a user with id: " + id.toString());
        return repository.findOne(id);
    }

    public User getUserByEmail(String email) {
        log.info("Attempting to retrieve a user with email: " + email);
        return repository.getUserByEmail(email);
    }

    public List<User> findAll(){
        log.info("Retrieving all users.");
        return repository.findAll();
    }
}
