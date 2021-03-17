package org.geektimes.configuration.microprofile.config;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.springframework.util.ClassUtils;

/**
 * @author xubin
 * @date 2021/3/16 19:21
 */
public class DefaultConfig implements Config {

    private List<ConfigSource> configSources = new LinkedList<>();

    public DefaultConfig() {
        ClassLoader classLoader = getClass().getClassLoader();
        synchronized (DefaultConfig.class) {
            ServiceLoader<ConfigSource> load = ServiceLoader.load(ConfigSource.class, classLoader);
            load.forEach(configSources::add);
        }
        // 默认升序，Ordinal 定义为越大优先级越大，所以反转一下
        configSources.sort(Comparator.comparingInt(ConfigSource::getOrdinal).reversed());
    }

    protected String getPropertyValue(String propertyName) {
        String propertyValue = null;
        for (ConfigSource configSource : configSources) {
            propertyValue = configSource.getValue(propertyName);
            if (propertyValue != null) {
                break;
            }
        }
        return propertyValue;
    }

    /**
     * 原生类型
     * 
     * @param propertyName
     * @param propertyType
     * @param <T>
     * @return
     */
    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        String propertyValue = getPropertyValue(propertyName);
        if (ClassUtils.isPrimitiveOrWrapper(propertyType)) {
            return propertyType.cast(propertyValue);
        } else if (String.class.isAssignableFrom(propertyType)) {
            return (T)propertyValue;
        } else {
            return null;
        }
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {
        return null;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        return Optional.ofNullable(getValue(propertyName, propertyType));
    }

    @Override
    public Iterable<String> getPropertyNames() {
        List<Set<String>> collects =
            configSources.stream().map(ConfigSource::getPropertyNames).collect(Collectors.toList());
        Set<String> result = new HashSet<>();
        collects.forEach(collect -> result.addAll(collect));
        return result;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return configSources;
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        return Optional.empty();
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }
}
