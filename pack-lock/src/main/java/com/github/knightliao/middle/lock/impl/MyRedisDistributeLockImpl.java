package com.github.knightliao.middle.lock.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.knightliao.middle.lock.IMyDistributeLock;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisNoScriptException;

/**
 * @author knightliao
 * @date 2021/8/12 14:51
 */
public class MyRedisDistributeLockImpl implements IMyDistributeLock {

    private Logger logger = LoggerFactory.getLogger(MyRedisDistributeLockImpl.class);

    private JedisCluster jedisCluster;

    private static final String DISTRIBUTE_LOCK_SCRIPT_UNLOCK_VAL = "if "
            + "redis.call(\"get\",KEYS[1]) == ARGV[1] "
            + "then\n"
            + "    return redis.call(\"del\",KEYS[1])\n"
            + "else\n"
            + "    return 0\n"
            + "end";

    private volatile String unlockSha1 = "";
    private static final Long UNLOCK_SUCCESS_CODE = 1L;
    private static final String LOCK_SUCCESS_CODE = "ok";

    public MyRedisDistributeLockImpl(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public boolean tryLock(String lockKey, String lockVal, long expireTimeSeconds, long loopTryTime) {

        long endTime = System.currentTimeMillis() + loopTryTime;
        while (System.currentTimeMillis() < endTime) {
            if (tryLock(lockKey, lockVal, expireTimeSeconds)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean tryLock(String lockKey, String lockVal, long expireTimeSeconds, int retryTime, long stepTime) {

        while (retryTime > 0) {

            if (tryLock(lockKey, lockVal, expireTimeSeconds)) {
                return true;
            }
            retryTime--;

            try {
                Thread.sleep(stepTime);
            } catch (InterruptedException e) {
                logger.error("get distribute lock error" + e.getLocalizedMessage());
            }
        }

        return false;
    }

    @Override
    public boolean tryLock(String lockKey, String lockVal, long expireTimeSeconds) {

        logger.info("lockKey {}", lockKey);

        String result = jedisCluster.set(lockKey, lockVal, "NX", "PX", expireTimeSeconds);

        return LOCK_SUCCESS_CODE.equalsIgnoreCase(result);
    }

    @Override
    public boolean tryUnlock(String lockKey, String lockVal) {

        List<String> keys = new ArrayList<>();
        keys.add(lockKey);
        List<String> argv = new ArrayList<>();
        argv.add(lockVal);

        try {

            Object result = jedisCluster.evalsha(unlockSha1, keys, argv);
            return UNLOCK_SUCCESS_CODE.equals(result);

        } catch (JedisNoScriptException e) {

            // 没有脚本缓存时，重新发送缓存
            logger.info("try to store script...");
            storeScript(lockKey);
            Object result = jedisCluster.evalsha(unlockSha1, keys, argv);
            return UNLOCK_SUCCESS_CODE.equals(result);

        } catch (Exception e) {

            logger.error(e.toString(), e);
            return false;
        }

    }

    // 由于使用redis集群，因此每个节点需要各自缓存一份脚本数据
    private void storeScript(String slotKey) {

        if (StringUtils.isEmpty(unlockSha1) || !jedisCluster.scriptExists(unlockSha1, slotKey)) {

            // redis 支持脚本缓存，返回哈希码，后续可以继续用来调用脚本
            unlockSha1 = jedisCluster.scriptLoad(DISTRIBUTE_LOCK_SCRIPT_UNLOCK_VAL, slotKey);
        }
    }
}
