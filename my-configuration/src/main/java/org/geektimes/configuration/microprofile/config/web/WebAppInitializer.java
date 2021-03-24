package org.geektimes.configuration.microprofile.config.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * servlet扩展接口
 * 
 * @author xubin
 * @date 2021/3/24 14:12
 */
public interface WebAppInitializer {

    void onStartup(ServletContext servletContext) throws ServletException;
}
