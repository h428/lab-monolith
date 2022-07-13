package com.lab.common.component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author hao
 */
@SuppressWarnings({ "unchecked"})
@Component
public class RedisUtil implements ApplicationContextAware {

    private static RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        redisTemplate = (RedisTemplate<String, Object>) applicationContext.getBean("redisTemplate");
    }

    public static boolean exist(String key) {
        Boolean tag = redisTemplate.hasKey(key);
        return tag != null && tag;
    }

    public static void remove(String key) {
        redisTemplate.delete(key);
    }

    public static void remove(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    // String 类型
    public static <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public static <T> void put(String key, T val) {
        redisTemplate.opsForValue().set(key, val);
    }

    public static <T> void put(String key, T val, long seconds) {
        redisTemplate.opsForValue().set(key, val, seconds, TimeUnit.SECONDS);
    }

    // hash 类型
    public static <K, V> Map<K, V> hashEntries(String key) {
        return redisTemplate.<K, V>opsForHash().entries(key);
    }

    public static <K, V> V hashGet(String key, K hashKey) {
        return redisTemplate.<K, V>opsForHash().get(key, hashKey);
    }

    public static <V> void hashPut(String key, String hashKey, V value) {
        redisTemplate.<String, V>opsForHash().put(key, hashKey, value);
    }

    public static <V> void hashPut(String key, String hashKey, V value, long seconds) {
        redisTemplate.<String, V>opsForHash().put(key, hashKey, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public static boolean hashExist(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public static void hashRemove(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

}
