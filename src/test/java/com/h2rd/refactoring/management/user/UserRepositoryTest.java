package com.h2rd.refactoring.management.user;

import com.h2rd.refactoring.management.Role;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    UserRepository repository;

    List<Role> roles = new ArrayList<>();

    @Before
    public void setup(){
        roles.add(Role.USER);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateNewWithNonUniqueEmail() throws Exception{
        User user1 = User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Green").roles(roles).build();
        User user2 = User.builder().email("asdf@gmail.com").firstName("Steve").lastName("Blue").roles(roles).build();
        entityManager.persistAndFlush(user1);

        repository.saveAndFlush(user2);
    }

    @Test
    public void testFindUsersByFirstNameContainingAndLastNameContaining(){
        User user1 = User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Green").roles(roles).build();
        User user2 = User.builder().email("fdsa@gmail.com").firstName("Steve").lastName("Blue").roles(roles).build();
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        Assertions.assertThat(repository.findUsersByFirstNameContainingAndLastNameContaining("Mike", "Green")).hasSize(1);
        Assertions.assertThat(repository.findUsersByFirstNameContainingAndLastNameContaining("", "Blue")).hasSize(1);
        Assertions.assertThat(repository.findUsersByFirstNameContainingAndLastNameContaining("Steve", "")).hasSize(1);
        Assertions.assertThat(repository.findUsersByFirstNameContainingAndLastNameContaining("Mike", "Blue")).isNullOrEmpty();
        Assertions.assertThat(repository.findUsersByFirstNameContainingAndLastNameContaining("","")).hasSize(2);
    }

    @Test
    public void testGetUserByEmail(){
        User user1 = User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Green").roles(roles).build();
        UUID id = (UUID) entityManager.persistAndGetId(user1);

        Assertions.assertThat(repository.getUserByEmail("asdf@gmail.com")).isEqualToComparingFieldByField(user1.toBuilder().id(id).build());
    }

    @Test
    public void testUpdateUserIncrementVersion(){
        User user = User.builder().email("asdf@gmail.com").firstName("Mike").lastName("Green").roles(roles).build();
        User savedUser = entityManager.persistAndFlush(user);

        User updatedUser = savedUser.toBuilder().firstName("Michael").build();
        Assertions.assertThat(repository.saveAndFlush(updatedUser).getVersion()).isEqualTo(1);
    }
}
