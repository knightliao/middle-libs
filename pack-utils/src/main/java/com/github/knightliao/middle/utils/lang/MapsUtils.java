package com.github.knightliao.middle.utils.lang;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.val;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/9/20 18:06
 */
public class MapsUtils {

    public static void putIn(Map map, Object value, String... paths) {

        Map last = map;
        for (int i = 0; i < paths.length - 1; ++i) {
            String key = paths[i];
            Object v = last.get(key);

            if (v == null) {
                val newMap = Maps.newHashMap();
                last.put(key, newMap);
                last = newMap;
            } else {
                if (v instanceof Map) {
                    last = (Map) v;
                } else {

                }
            }
        }

        last.put(paths[paths.length - 1], value);
    }

    public static <V> V takeIn(Map map, String... keys) {
        Object last = map;

        for (String key : keys) {
            if (last != null) {
                last = ((Map) last).get(key);
            } else {
                return null;
            }
        }

        return (V) last;
    }
}
