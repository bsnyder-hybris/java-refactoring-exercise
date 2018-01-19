package com.h2rd.refactoring;

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
        response.sendRedirect("/users");
    }
}
