package com.github.knightliao.middle.redis;

/**
 * @author knightliao
 * @date 2021/8/11 16:50
 */
public interface IMyRedisService {

    Boolean exist(String key);

    void expire(String key, int expireTimeSeconds);

    void set(String key, Object data, Integer expireSeconds);
}
