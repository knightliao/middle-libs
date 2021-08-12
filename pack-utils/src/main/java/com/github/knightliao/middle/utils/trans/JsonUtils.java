package com.github.knightliao.middle.utils.trans;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @date 2021/8/11 17:13
 */
@Slf4j
public class JsonUtils {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJson(Map<String, ?> map) {

        if (null == map || map.size() == 0) {
            return "";
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (Exception ex) {
            log.error("to json cause exception", ex);
            throw new RuntimeException(ex);
        }
    }

    public static String toJson(Object obj) {

        if (null == obj) {
            return "";
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception ex) {
            log.error("to json cause exception", ex);
            throw new RuntimeException(ex);
        }
    }

    public static <T> T fromJson(String json, Class<T> clz) {

        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, clz);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
