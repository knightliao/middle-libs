package com.github.knightliao.middle.lock;

/**
 * @author knightliao
 * @date 2021/8/12 14:51
 */
public interface IMyDistributeLock {

    // 根据 loopTryTime 循环重试
    boolean tryLock(String lockKey, String lockVal, long expireTimeSeconds, long loopTryTime);

    //
    boolean tryLock(String lockKey, String lockVal, long expireTimeSeconds, int retryTime, long stepTime);

    // 一次尝试，快速失败，不支持重入
    boolean tryLock(String lockKey, String lockVal, long expireTimeSeconds);

    // 释放分布式锁，释放失败可能是业务执行时间长于lockkey过期时间，应当结合业务场景调整过期时间
    boolean tryUnlock(String lockKey, String lockVal);
}

