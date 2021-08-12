package com.github.knightliao.middle.redis.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.knightliao.middle.redis.IMyRedisService;
import com.github.knightliao.middle.utils.trans.JsonUtils;

import lombok.Getter;
import redis.clients.jedis.JedisCluster;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/11 16:51
 */
public class MyRedisServiceImpl implements IMyRedisService {

    @Getter
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
        setRaw(key, expireSeconds, JsonUtils.toJson(data));
    }

    @Override
    public void setRaw(String key, Integer expireSeconds, String data) {

        if (expireSeconds != null) {
            jedisCluster.setex(key, expireSeconds, data);
        } else {
            jedisCluster.set(key, data);
        }
    }

    @Override
    public <T> T get(String key, Class<T> myclass) {

        String ret = jedisCluster.get(key);
        if (ret != null) {
            return JsonUtils.fromJson(ret, myclass);
        } else {
            return null;
        }
    }

    @Override
    public String get(String key) {

        return jedisCluster.get(key);
    }

    @Override
    public boolean del(String key) {
        return jedisCluster.del(key) == 1L;
    }

    @Override
    public void hset(String key, String field, Object data, Integer expireSeconds) {

        jedisCluster.hset(key, field, JsonUtils.toJson(data));
        if (expireSeconds != null) {
            jedisCluster.expire(key, expireSeconds);
        }
    }

    @Override
    public <T> Map<String, T> hmgetAll(String key, Class<T> myclass) {

        Map<String, String> dataList = jedisCluster.hgetAll(key);
        Map<String, T> map = new HashMap<>();

        Iterator var5 = dataList.keySet().iterator();

        while (var5.hasNext()) {
            String field = (String) var5.next();
            String value = dataList.get(field);
            if (!StringUtils.isBlank(value)) {
                map.put(field, JsonUtils.fromJson(value, myclass));
            }
        }

        return map;
    }

    @Override
    public void hdel(String key, String... field) {
        this.jedisCluster.hdel(key, field);
    }
}
