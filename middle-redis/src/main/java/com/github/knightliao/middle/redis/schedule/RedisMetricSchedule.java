package com.github.knightliao.middle.redis.schedule;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.github.knightliao.middle.redis.schedule.helper.RedisMetricService;
import com.github.knightliao.middle.utils.thread.NamedThreadFactory;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/19 18:33
 */
@Slf4j
public class RedisMetricSchedule {

    private RedisMetricService redisMetricService;

    public RedisMetricSchedule(String keyType, JedisCluster jedisCluster) {
        this.redisMetricService = new RedisMetricService(keyType, jedisCluster);
    }

    @PostConstruct
    private void init() {

        printLog();

        ScheduledExecutorService pool =
                new ScheduledThreadPoolExecutor(1,
                        new NamedThreadFactory("redis_metric_print", true));
        pool.scheduleAtFixedRate(this::printLog, 1, 1, TimeUnit.MINUTES);
    }

    private void printLog() {

        redisMetricService.printMetric();
    }
}
