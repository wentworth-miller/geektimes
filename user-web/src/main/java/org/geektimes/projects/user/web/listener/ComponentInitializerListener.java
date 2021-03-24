package org.geektimes.projects.user.web.listener;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.geektimes.projects.user.sql.DataSourceManager;

@WebListener
public class ComponentInitializerListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DataSourceManager.init();
//        ComponentContext componentContext = new ComponentContext();
//        componentContext.init(sce.getServletContext());

//        Config config = ConfigProvider.getConfig();
//        String applicationName = config.getValue("application.name", String.class);
//        logger.info("application.name: **** " + applicationName);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
