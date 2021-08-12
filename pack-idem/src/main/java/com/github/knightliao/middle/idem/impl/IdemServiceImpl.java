package com.github.knightliao.middle.idem.impl;

import com.github.knightliao.middle.idem.IIdemService;
import com.github.knightliao.middle.idem.exception.IdemException;
import com.github.knightliao.middle.idem.service.callback.IIdemBizCallback;
import com.github.knightliao.middle.idem.service.helper.IIdemServiceHelper;
import com.github.knightliao.middle.idem.service.helper.impl.IdemServiceRedisHelperImpl;
import com.github.knightliao.middle.lock.IMyDistributeLock;
import com.github.knightliao.middle.lock.impl.MyRedisDistributeLockImpl;
import com.github.knightliao.middle.redis.IMyRedisService;
import com.github.knightliao.middle.utils.trans.JsonUtils;

/**
 * @author knightliao
 * @date 2021/8/12 14:38
 */
public class IdemServiceImpl implements IIdemService {

    private IIdemServiceHelper idemServiceHelper;

    private IMyDistributeLock myDistributeLock;

    public IdemServiceImpl(IMyRedisService myRedisService) {

        if (myRedisService == null || myRedisService.getJedisCluster() == null) {
            throw new NullPointerException("myRedisService or jedisCluster is null");
        }

        this.idemServiceHelper = new IdemServiceRedisHelperImpl(myRedisService);
        this.myDistributeLock = new MyRedisDistributeLockImpl(myRedisService.getJedisCluster());
    }

    @Override
    public <T> T executeWithResult(String idemKey, IIdemBizCallback<T> callback, int lockTimeMills, Class<T> myClass) {

        // 检查 幂等性
        String value = idemServiceHelper.checkIdem(idemKey);

        // 已经存在值了，则直接返回吧
        if (value != null) {
            return JsonUtils.fromJson(value, myClass);
        }

        // 以下开启分布式锁

        String timestampStr = String.valueOf(System.currentTimeMillis());
        try {

            boolean canDo = myDistributeLock.tryLock(idemKey, timestampStr, lockTimeMills);

            if (canDo) {

                // 处理
                T t = callback.process();

                // 保存幂等
                idemServiceHelper.saveIdem(idemKey, JsonUtils.toJson(t));

                //
                return t;
            } else {

                // 并发异常
                throw IdemException.getIdemConcurrentException();
            }

        } finally {

            myDistributeLock.tryUnlock(idemKey, timestampStr);
        }
    }

    @Override
    public <T> boolean execute(String idemKey, IIdemBizCallback<T> callback, int lockTimeMills) throws IdemException {

        // 检查 幂等性
        String value = idemServiceHelper.checkIdem(idemKey);

        // 已经存在值了，则直接返回吧
        if (value != null) {
            return false;
        }

        // 以下开启分布式锁

        String timestampStr = String.valueOf(System.currentTimeMillis());
        try {

            boolean canDo = myDistributeLock.tryLock(idemKey, timestampStr, lockTimeMills);

            if (canDo) {

                // 处理
                T t = callback.process();

                // 保存幂等
                idemServiceHelper.saveIdem(idemKey, JsonUtils.toJson(t));

                //
                return true;
            } else {

                // 并发异常
                throw IdemException.getIdemConcurrentException();
            }

        } finally {

            myDistributeLock.tryUnlock(idemKey, timestampStr);
        }
    }
}
