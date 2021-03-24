package org.geektimes.projects.user.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.geektimes.configuration.microprofile.config.source.servlet.MicroprofileConfigInitializer;
import org.geektimes.context.ClassicComponentContext;
import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.domain.dto.Const;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.servlet.ControllerServletHolder;
import org.geektimes.web.mvc.controller.RestController;

/**
 * @author xubin
 * @date 2021/3/3 21:24
 */
@Path("/do")
public class DoRegisterController implements RestController {

    @Path("/register")
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {

        // UserService userService = UserServiceFactory.getInstance();
        ComponentContext componentContext =
            (ComponentContext)request.getServletContext().getAttribute(ClassicComponentContext.class.getName());
        printParams(request.getServletContext());
        UserService userService = componentContext.getComponent(Const.USER_SERVICE);
        User user = new User();
        user.setName("test");
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("pwd"));
        user.setPhoneNumber(request.getParameter("iphoneNum"));
        boolean register = userService.register(user);
        if (register) {
            request.getServletContext().getRequestDispatcher("/show.jsp").forward(request, response);
        } else {
            response.sendRedirect("/register/index");
        }
    }

    private void printParams(ServletContext servletContext) {
        ConfigProviderResolver configProviderResolver =
            (ConfigProviderResolver)servletContext.getAttribute(MicroprofileConfigInitializer.class.getName());
        Config config = configProviderResolver.getConfig(servletContext.getClassLoader());
        String appName = config.getOptionalValue("application.name", String.class).orElse("default");
        System.out.println(appName);

        String appName2 = ControllerServletHolder.getConfigThreadLocal().get()
            .getOptionalValue("application.name", String.class).orElse("default");
        System.out.println(appName2);

    }
}
