package org.geektimes.cache.lettuce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;

import org.geektimes.cache.AbstractCache;
import org.geektimes.cache.ExpirableEntry;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class LettuceCache extends AbstractCache<String, String> {

    private final RedisCommands<String, String> syncCommands;

    private final StatefulRedisConnection<String, String> connection;

    public LettuceCache(CacheManager cacheManager, String cacheName, Configuration<String, String> configuration,
        StatefulRedisConnection<String, String> connection) {
        super(cacheManager, cacheName, configuration);
        this.connection = connection;
        this.syncCommands = connection.sync();
    }

    @Override
    protected boolean containsEntry(String key) throws CacheException, ClassCastException {
        Long exists = syncCommands.exists(key);
        return exists > 0;
    }

    @Override
    protected ExpirableEntry<String, String> getEntry(String key) throws CacheException, ClassCastException {
//        byte[] keyBytes = serialize(key);
        return ExpirableEntry.of(key, syncCommands.get(key));
    }

    protected ExpirableEntry<String, String> getEntry(byte[] keyBytes) throws CacheException, ClassCastException {
        String s = syncCommands.get(new String(keyBytes));
        return ExpirableEntry.of(deserialize(keyBytes), s);
    }

    @Override
    protected void putEntry(ExpirableEntry<String, String> entry) throws CacheException, ClassCastException {
        syncCommands.set(entry.getKey(), entry.getValue());
    }

    @Override
    protected ExpirableEntry<String, String> removeEntry(String key) throws CacheException, ClassCastException {
        byte[] keyBytes = serialize(key);
        ExpirableEntry<String, String> oldEntry = getEntry(keyBytes);
        syncCommands.del(key);
        return oldEntry;
    }

    @Override
    protected void clearEntries() throws CacheException {
        // TODO
    }

    @Override
    protected Set<String> keySet() {
        // TODO
        return null;
    }

    @Override
    protected void doClose() {
        this.connection.close();
    }

    // 是否可以抽象出一套序列化和反序列化的 API
    private byte[] serialize(Object value) throws CacheException {
        byte[] bytes = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            // Key -> byte[]
            objectOutputStream.writeObject(value);
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new CacheException(e);
        }
        return bytes;
    }

    private <T> T deserialize(byte[] bytes) throws CacheException {
        if (bytes == null) {
            return null;
        }
        T value = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            // byte[] -> Value
            value = (T)objectInputStream.readObject();
        } catch (Exception e) {
            throw new CacheException(e);
        }
        return value;
    }

}
