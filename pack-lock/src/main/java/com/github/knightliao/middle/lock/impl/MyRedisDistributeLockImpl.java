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
 * @email knightliao@gmail.com
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

        if (jedisCluster == null) {
            throw new NullPointerException("jedisCluster is null");
        }

        this.jedisCluster = jedisCluster;
    }

    @Override
    public boolean tryLock(String lockKey, String lockVal, long expireTimeMills, long loopTryTime) {

        long endTime = System.currentTimeMillis() + loopTryTime;
        while (System.currentTimeMillis() < endTime) {
            if (tryLock(lockKey, lockVal, expireTimeMills)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean tryLock(String lockKey, String lockVal, long expireTimeMills, int retryTime, long stepTime) {

        while (retryTime > 0) {

            if (tryLock(lockKey, lockVal, expireTimeMills)) {
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
    public boolean tryLock(String lockKey, String lockVal, long expireTimeMills) {

        logger.info("lockKey {}", lockKey);

        String result = jedisCluster.set(lockKey, lockVal, "NX", "PX", expireTimeMills);

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

            // ??????????????????????????????????????????
            logger.info("try to store script...");
            storeScript(lockKey);
            Object result = jedisCluster.evalsha(unlockSha1, keys, argv);
            return UNLOCK_SUCCESS_CODE.equals(result);

        } catch (Exception e) {

            logger.error(e.toString(), e);
            return false;
        }

    }

    // ????????????redis???????????????????????????????????????????????????????????????
    private void storeScript(String slotKey) {

        if (StringUtils.isEmpty(unlockSha1) || !jedisCluster.scriptExists(unlockSha1, slotKey)) {

            // redis ???????????????????????????????????????????????????????????????????????????
            unlockSha1 = jedisCluster.scriptLoad(DISTRIBUTE_LOCK_SCRIPT_UNLOCK_VAL, slotKey);
        }
    }
}
