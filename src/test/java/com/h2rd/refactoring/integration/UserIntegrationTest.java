package com.h2rd.refactoring.integration;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.Test;

import com.h2rd.refactoring.management.user.User;
import com.h2rd.refactoring.management.user.UserController;

public class UserIntegrationTest {
	
//	@Test
//	public void createUserTest() {
//		UserController userController = new UserController();
//
//		User integration = new User();
//        integration.setName("integration");
//        integration.setEmail("initial@integration.com");
//        integration.setRoles(new ArrayList<String>());
//
//        Response response = userController.addUser(integration.getName(), integration.getEmail(), integration.getRoles());
//        Assert.assertEquals(200, response.getStatus());
//	}
//
//	@Test
//	public void updateUserTest() {
//		UserController userController = new UserController();
//
//		createUserTest();
//
//        User updated = new User();
//        updated.setName("integration");
//        updated.setEmail("updated@integration.com");
//        updated.setRoles(new ArrayList<String>());
//
//        Response response = userController.updateUser(updated.getName(), updated.getEmail(), updated.getRoles());
//        Assert.assertEquals(200, response.getStatus());
//	}
}
