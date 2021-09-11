package com.github.knightliao.middle.idem.service.helper;

import com.github.knightliao.middle.lang.exceptions.exceptions.param.IdemException;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/12 14:39
 */
public interface IIdemServiceHelper {

    // 幂等性校验
    String checkIdem(String key) throws IdemException;

    // 存储幂等
    void saveIdem(String key, String value);
}
