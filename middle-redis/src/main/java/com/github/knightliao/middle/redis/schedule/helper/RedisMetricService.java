package com.github.knightliao.middle.redis.schedule.helper;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.knightliao.middle.log.LoggerUtil;
import com.github.knightliao.middle.utils.net.IpUtils;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/19 18:28
 */
@Slf4j
public class RedisMetricService {

    private static final Logger logger = LoggerFactory.getLogger("RedisMetric");

    private JedisCluster jedisCluster;
    private String keyType;
    private String ip;

    public RedisMetricService(String keyType, JedisCluster jedisCluster) {
        this.keyType = keyType;
        this.jedisCluster = jedisCluster;
        this.ip = IpUtils.getLocalIp();
    }

    public void printMetric() {

        Map<String, Long> map = new HashMap<>();

        try {

            if (jedisCluster == null) {
                return;
            }

            Map<String, JedisPool> jedisPoolMap = jedisCluster.getClusterNodes();
            for (String key : jedisPoolMap.keySet()) {

                JedisPool jedisPool = jedisPoolMap.get(key);

                LoggerUtil.info(logger,
                        "{0} {1} key={2} active={3} waiters={4} idle={5} maxWaitmill={6} meanWaitmil={7}",
                        ip, keyType, key,
                        jedisPool.getNumActive(), jedisPool.getNumWaiters(), jedisPool.getNumIdle(),
                        jedisPool.getMaxBorrowWaitTimeMillis(), jedisPool.getMeanBorrowWaitTimeMillis());
            }
        } catch (Exception e) {

            log.error(e.toString(), e);
        }
    }
}
