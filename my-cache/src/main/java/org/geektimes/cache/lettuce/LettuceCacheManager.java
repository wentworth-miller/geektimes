package org.geektimes.cache.lettuce;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Properties;

import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

import org.geektimes.cache.AbstractCacheManager;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

/**
 * @author xubin
 * @date 2021/4/14 20:49
 */
public class LettuceCacheManager extends AbstractCacheManager {

    private final RedisClient redisClient;

    private final StatefulRedisConnection<String, String> connection;

    public LettuceCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties)
        throws MalformedURLException {
        super(cachingProvider, uri, classLoader, properties);
        String replace = uri.toString().replace("lettuce", "redis");
        this.redisClient = RedisClient.create(replace);
        this.connection = redisClient.connect();
    }

    @Override
    protected <K, V, C extends Configuration<K, V>> Cache doCreateCache(String cacheName, C configuration) {
        return new LettuceCache(this, cacheName, (Configuration<String, String>)configuration, connection);
    }

    @Override
    protected void doClose() {
        redisClient.shutdown();
    }
}
