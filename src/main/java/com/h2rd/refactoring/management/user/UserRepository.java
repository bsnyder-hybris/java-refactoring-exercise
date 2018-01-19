package com.h2rd.refactoring.management.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findUsersByFirstNameContainingAndLastNameContaining(String firstName, String lastName);

    User getUserByEmail(String email);
}
