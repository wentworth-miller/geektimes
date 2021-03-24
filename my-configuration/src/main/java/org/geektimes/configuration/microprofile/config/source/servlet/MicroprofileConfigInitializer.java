package org.geektimes.configuration.microprofile.config.source.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.geektimes.configuration.microprofile.config.web.WebAppInitializer;

/**
 * @author xubin
 * @date 2021/3/24 14:31
 */
public class MicroprofileConfigInitializer implements WebAppInitializer {

    private final String KEY = getClass().getName();

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ClassLoader classLoader = servletContext.getClassLoader();
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
        // 配置 ClassLoader
        configBuilder.forClassLoader(classLoader);
        // 默认配置源（内建的，静态的）
        configBuilder.addDefaultSources();
        // 通过发现配置源（动态的）
        configBuilder.addDiscoveredSources();
        // 发现转换器
        configBuilder.addDiscoveredConverters();
        // 增加扩展配置源（基于 Servlet 引擎）
        configBuilder.withSources(new ServletContextConfigSource(servletContext));
        // 获取 Config
        Config config = configBuilder.build();
        // 注册 Config 关联到当前 ClassLoader
        configProviderResolver.registerConfig(config, classLoader);

        servletContext.setAttribute(KEY, configProviderResolver);
    }
}
