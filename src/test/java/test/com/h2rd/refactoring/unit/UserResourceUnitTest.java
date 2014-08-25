package test.com.h2rd.refactoring.unit;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.UserDao;
import com.h2rd.refactoring.web.UserResource;
import junit.framework.Assert;
import org.junit.Test;

import javax.ws.rs.core.Response;

public class UserResourceUnitTest {

    UserResource userResource;
    UserDao userDao;

    @Test
    public void getUsersTest() {

        userResource = new UserResource();
        userDao = UserDao.getUserDao();

        User user = new User();
        user.setName("fake user");
        user.setEmail("fake@user.com");
        userDao.saveUser(user);

        Response response = userResource.getUsers();
        Assert.assertEquals(200, response.getStatus());
    }
}
