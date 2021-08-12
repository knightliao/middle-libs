package com.github.knightliao.middle.redis;

import java.util.Map;

/**
 * @author knightliao
 * @date 2021/8/11 16:50
 */
public interface IMyRedisService {

    Boolean exist(String key);

    void expire(String key, int expireTimeSeconds);

    void set(String key, Integer expireSeconds, Object data);

    void setRaw(String key, Integer expireSeconds, String data);

    <T> T get(String key, Class<T> myclass);

    String get(String key);

    void del(String key);

    void hset(String key, String field, Object data, Integer expireSeconds);

    <T> Map<String, T> hmgetAll(String key, Class<T> myclass);

    void hdel(String key, String... field);
}
