package org.geektimes.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.geektimes.configuration.microprofile.config.web.WebAppInitializer;
import org.geektimes.context.ClassicComponentContext;
import org.geektimes.context.ComponentContext;

/**
 * @author xubin
 * @date 2021/3/24 17:07
 */
public class ComponentContextInitializer implements WebAppInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ComponentContext componentContext = new ClassicComponentContext().init(servletContext);
    }
}
