package com.github.knightliao.middle.redis;

import java.util.List;
import java.util.Map;

/**
 * @author knightliao
 * @date 2021/8/12 09:33
 */
public interface IMyRedisBatchService {

    Map<String, String> batchGetData(List<String> keys);

    <T> Map<String, T> batchGetData(List<String> keys, Class<T> myclass);

    <T> Map<Long, T> batchGetDataWithLong(List<String> keys, Class<T> myclass);
}
