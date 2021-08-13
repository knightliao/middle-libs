package com.github.knightliao.middle.idgen.impl;

import com.github.knightliao.middle.idgen.IIdgenService;
import com.github.knightliao.middle.redis.IMyRedisService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/13 15:05
 */
@Slf4j
public class IdgenServiceRedisImpl implements IIdgenService {

    private IMyRedisService myRedisService;

    public IdgenServiceRedisImpl(IMyRedisService myRedisService) {
        this.myRedisService = myRedisService;
    }

    @Override
    public Long getSequenceId(long key) {
        return getSequenceId(String.valueOf(key));
    }

    @Override
    public Long getSequenceId(String key) {

        String innerKey = getInnerKey(key);

        Long ret = myRedisService.incr(innerKey, 1, null);

        return ret;
    }

    private String getInnerKey(String key) {
        return String.format("%s:%s", "ID_GEN", key);
    }
}
