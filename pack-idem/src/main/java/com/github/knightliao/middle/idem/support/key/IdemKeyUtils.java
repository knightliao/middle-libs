package com.github.knightliao.middle.idem.support.key;

/**
 * @author knightliao
 * @date 2021/8/12 14:43
 */
public class IdemKeyUtils {

    // 3小时
    public static final int getIdemKeyExpire = 60 * 60 * 3;

    //
    public static String getIdemKey(String idemKey) {

        return String.format("idem_%s", idemKey);
    }
}
