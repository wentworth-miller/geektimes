package org.geektimes.configuration.microprofile.config;

import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

/**
 * build provider resolver instance
 * 
 * @author xubin
 * @date 2021/3/16 19:19
 */
public class DefaultConfigProviderResolverBack extends ConfigProviderResolver {

    @Override
    public Config getConfig() {
        return getConfig(null);
    }

    @Override
    public Config getConfig(ClassLoader loader) {
        ClassLoader classLoader = null;
        if (Objects.isNull(classLoader)) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        ServiceLoader<Config> load = ServiceLoader.load(Config.class, classLoader);
        Iterator<Config> iterator = load.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new IllegalStateException("No Config implementation found!");
    }

    @Override
    public ConfigBuilder getBuilder() {
        return null;
    }

    @Override
    public void registerConfig(Config config, ClassLoader classLoader) {

    }

    @Override
    public void releaseConfig(Config config) {

    }
}
