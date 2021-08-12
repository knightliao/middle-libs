package com.github.knightliao.middle.idem.service.helper.impl;

import com.github.knightliao.middle.idem.exception.IdemException;
import com.github.knightliao.middle.idem.service.helper.IIdemServiceHelper;
import com.github.knightliao.middle.idem.support.key.IdemKeyUtils;
import com.github.knightliao.middle.redis.IMyRedisService;

/**
 * @author knightliao
 * @date 2021/8/12 14:40
 */
public class IdemServiceRedisHelperImpl implements IIdemServiceHelper {

    private IMyRedisService myRedisService;

    public IdemServiceRedisHelperImpl(IMyRedisService myRedisService) {
        this.myRedisService = myRedisService;
    }

    @Override
    public String checkIdem(String key) throws IdemException {

        String redisKey = IdemKeyUtils.getIdemKey(key);

        return myRedisService.get(redisKey);
    }

    @Override
    public void saveIdem(String key, String value) {

        String redisKey = IdemKeyUtils.getIdemKey(key);

        myRedisService.set(redisKey, IdemKeyUtils.getIdemKeyExpire, value);
    }
}
