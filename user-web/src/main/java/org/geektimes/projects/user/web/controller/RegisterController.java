package org.geektimes.projects.user.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.geektimes.web.mvc.controller.PageController;

/**
 * @author xubin
 * @date 2021/3/3 19:22
 */
@Path("/register")
public class RegisterController implements PageController {

    @Path("/index")
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = cookies[0];
        return "register-form.jsp";
    }
}
