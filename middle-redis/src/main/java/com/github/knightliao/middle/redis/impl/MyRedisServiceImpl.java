package com.github.knightliao.middle.redis.impl;

import com.github.knightliao.middle.redis.IMyRedisService;

import redis.clients.jedis.JedisCluster;

/**
 * @author knightliao
 * @date 2021/8/11 16:51
 */
public class MyRedisServiceImpl implements IMyRedisService {

    private JedisCluster jedisCluster;

    public MyRedisServiceImpl(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public Boolean exist(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public void expire(String key, int expireTimeSeconds) {
        jedisCluster.expire(key, expireTimeSeconds);
    }

    @Override
    public void set(String key, Integer expireSeconds, Object data) {
        setRaw(key,expireSeconds,JsonU);
    }

    public void setRaw(String key, Integer expireSeconds, String data) {

        if (expireSeconds != null) {
            jedisCluster.setex(key, expireSeconds, data);
        } else {
            jedisCluster.set(key, data);
        }

    }
}
