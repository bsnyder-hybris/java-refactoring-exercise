package com.h2rd.refactoring;

import com.h2rd.refactoring.management.user.UserController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/")
public class IndexController {
    @RequestMapping(method = RequestMethod.GET)
    public void getIndex(HttpServletResponse response) throws IOException {
        response.sendRedirect(UserController.USER_CONTROLLER_URI_PATH);
    }
}
