package com.github.knightliao.middle.lang.ables.data;

import java.util.Map;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/21 02:02
 */
public interface IExtAware {

    Map getExtMap();

    void setExtMap();

    void putExt(String key, Object value);

    <Ext> Ext getExt(String key);

    <Ext> Ext getExt(String key, Class<Ext> t);

    default <Ext> Ext getExt(String key, Ext defaultVal) {
        Ext ext = getExt(key);
        if (ext == null) {
            return defaultVal;
        }
        return ext;
    }
}
