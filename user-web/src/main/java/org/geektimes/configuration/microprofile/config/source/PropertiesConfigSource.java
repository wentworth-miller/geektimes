package org.geektimes.configuration.microprofile.config.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * 默认数据源 <br/>
 * 1、System.getProperties() (ordinal=400)<br/>
 * 2、System.getenv() (ordinal=300)<br/>
 * 3、类路径上的所有META-INF/microprofile-config.properties（ordinal=100）
 * 
 * getOrdinal()可以复写，修改优先级，也可以经优先级配置的数据源中（如properties文件中）
 *
 * @author xubin
 * @date 2021/3/16 14:46
 */
public class PropertiesConfigSource implements ConfigSource {

    private final Map<String, String> properties;

    public PropertiesConfigSource() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("custom-microprofile-config");
        properties = new HashMap<>();
        // 后定义的值覆盖之前定义的值
        bundle.keySet().forEach(key -> properties.put(key, bundle.getString(key)));
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getName() {
        return "custom properties";
    }
}
