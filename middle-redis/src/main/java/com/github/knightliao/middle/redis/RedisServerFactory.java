package com.github.knightliao.middle.redis;

import com.github.knightliao.middle.redis.impl.MyRedisServiceImpl;

import redis.clients.jedis.JedisCluster;

/**
 * @author knightliao
 * @date 2021/8/11 17:26
 */
public class RedisServerFactory {

    public static IMyRedisService getMyRedisService(JedisCluster jedisCluster) {
        return new MyRedisServiceImpl(jedisCluster);
    }
}
