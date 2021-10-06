package com.github.knightliao.middle.utils.trans;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
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

    /**
     * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
     *
     * @param <T>
     * @param jsonString JSON字符串
     * @param tr         TypeReference,例如: new TypeReference< List<FamousUser> >(){}
     * @return List对象列表
     */
    @SuppressWarnings("unchecked")
    public static <T> T json2GenericObject(String jsonString,
                                           TypeReference<T> tr) {

        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
                return (T) OBJECT_MAPPER.readValue(jsonString, tr);
            } catch (Exception e) {
                log.warn("json error:" + e.getMessage());
            }
        }
        return null;
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
