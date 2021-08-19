package com.github.knightliao.middle.utils.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/19 18:58
 */
public class MyStringUtils {

    public static List<String> split(String value, String splitter) {

        return StringUtils.isBlank(value) ?
                new ArrayList<>() :
                Splitter.on(splitter).trimResults().omitEmptyStrings().splitToList(value);
    }

    public static List<Long> splitToLong(String value, String splitter) {

        if (StringUtils.isEmpty(value)) {
            return new ArrayList<>();
        }
        List<String> list = split(value, splitter);
        return list.stream().map(Long::valueOf).collect(Collectors.toList());
    }
}
