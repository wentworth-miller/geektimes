package org.geektimes.configuration.microprofile.config.source.servlet;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;

import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;

public class ServletContextConfigSource extends MapBasedConfigSource {

    private final ServletContext servletContext;

    public ServletContextConfigSource(ServletContext servletContext) {
        super("ServletContext Init Parameters", 500);
        this.servletContext = servletContext;
        super.source = getProperties();
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        Enumeration<String> parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            configData.put(parameterName, servletContext.getInitParameter(parameterName));
        }
    }
}
