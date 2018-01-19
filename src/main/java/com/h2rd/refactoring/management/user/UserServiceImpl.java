package com.h2rd.refactoring.management.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    public List<User> findUsersByFirstNameAndLastName(String firstName, String lastName){
        return repository.findUsersByFirstNameContainingAndLastNameContaining(firstName, lastName);
    }

    public void deleteUser(User user) {
        repository.delete(user);
    }

    public User updateUser(User user) {
        return saveUser(user);
    }

    public User saveUser(User user) {
        return repository.saveAndFlush(user);
    }

    public User getUserById(UUID id) {
        return repository.findOne(id);
    }

    public User getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public List<User> findAll(){
        return repository.findAll();
    }
}
