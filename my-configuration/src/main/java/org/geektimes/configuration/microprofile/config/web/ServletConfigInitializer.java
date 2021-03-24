package org.geektimes.configuration.microprofile.config.web;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

/**
 * servlet config by ServletContainerInitializer
 */
@HandlesTypes(WebAppInitializer.class)
public class ServletConfigInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {

        List<WebAppInitializer> initializers = new LinkedList<>();
        if (Objects.nonNull(c)) {
            List<Class<?>> collect = c.stream().filter(this::preCheck).collect(Collectors.toList());
            for (Class clazz : collect) {
                try {
                    initializers.add((WebAppInitializer)clazz.newInstance());
                } catch (Throwable ex) {
                    throw new ServletException("failed to instantiate webAppInitializer class", ex);
                }
            }
        } else {
            servletContext.log("no webAppInitializer types detected on classpath");
            return;
        }

        // 增加 ServletContextListener
        // servletContext.addListener(ServletContextConfigInitializer.class);
        for (WebAppInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }
    }

    private boolean preCheck(Class clazz) {
        return WebAppInitializer.class.isAssignableFrom(clazz) && !clazz.isInterface()
            && !Modifier.isAbstract(clazz.getModifiers());
    }

    private WebAppInitializer create(Class clazz) throws IllegalAccessException, InstantiationException {
        return (WebAppInitializer)clazz.newInstance();
    }
}
