package com.github.knightliao.middle.redis.impl;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.knightliao.middle.redis.IMyRedisBatchService;
import com.github.knightliao.middle.utils.trans.JsonUtils;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.JedisClusterCRC16;

/**
 * @author knightliao
 * @date 2021/8/12 09:36
 */
@Slf4j
public class MyRedisBatchServiceImpl implements IMyRedisBatchService {

    private static final Logger logger = LoggerFactory.getLogger("MIDDLE_REDIS_LOG");

    private Map<String, JedisPool> nodeMap;
    private TreeMap<Long, String> slotHostMap;

    private String host;
    private String password;

    public MyRedisBatchServiceImpl(JedisCluster jedisCluster, String host, String password) {

        this.nodeMap = jedisCluster.getClusterNodes();
        this.password = password;
        this.host = host;
        this.slotHostMap = getSlotHostMap();
    }

    @Override
    public Map<String, String> batchGetData(List<String> keys) {

        if (keys == null || keys.isEmpty()) {
            return new HashMap<>();
        }

        //
        StopWatch stopWatch = new StopWatch();
        Map<String, String> data = new HashMap<>();
        try {

            //
            stopWatch.start();

            //
            Map<JedisPool, List<String>> poolKeysMap = getPoolKeyMap(keys);

            //
            data = batchGetDataInner(poolKeysMap);

            return data;

        } finally {

            doLog("batchGetData", keys.size(), data.keySet().size(), stopWatch);
        }
    }

    @Override
    public <T> Map<String, T> batchGetData(List<String> keys, Class<T> myclass) {

        Map<String, String> map = batchGetData(keys);
        Map<String, T> retMap = new HashMap<>();

        for (String key : map.keySet()) {
            String objectStr = map.get(key);
            T object = JsonUtils.fromJson(objectStr, myclass);
            retMap.put(objectStr, object);
        }

        return retMap;
    }

    @Override
    public <T> Map<Long, T> batchGetDataWithLong(List<String> keys, Class<T> myclass) {
        return null;
    }

    protected TreeMap<Long, String> getSlotHostMap() {

        TreeMap<Long, String> tree = new TreeMap<>();

        Set<HostAndPort> nodes = stream(this.host.split(","))
                .map(host -> host.split(":"))
                .filter(strings -> strings.length == 2)
                .map(strings -> new HostAndPort(strings[0], Integer.valueOf(strings[1])))
                .collect(Collectors.toSet());

        try {

            for (HostAndPort hostAndPort : nodes) {
                Jedis jedisNode = new Jedis(hostAndPort.getHost(), hostAndPort.getPort());
                jedisNode.auth(this.password);
                List<Object> list = jedisNode.clusterSlots();

                for (Object object : list) {
                    List<Object> list1 = (List<Object>) object;
                    List<Object> master = (List<Object>) list1.get(2);
                    String hostAndPortStr = new String((byte[]) master.get(0)) + ":" + master.get(1);
                    tree.put((Long) list1.get(0), hostAndPortStr);
                    tree.put((Long) list1.get(1), hostAndPortStr);
                }

                jedisNode.close();
            }

            log.info(tree.toString());

        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }

        return tree;
    }

    /**
     * 将key按slort分批整理
     *
     * @param keys
     * @return
     */
    protected Map<JedisPool, List<String>> getPoolKeyMap(List<String> keys) {
        Map<JedisPool, List<String>> poolKeysMap = new LinkedHashMap<JedisPool, List<String>>();
        try {
            for (String key : keys) {

                int slot = JedisClusterCRC16.getSlot(key);

                //获取到对应的Jedis对象，此处+1解决临界问题
                Map.Entry<Long, String> entry = slotHostMap.lowerEntry(Long.valueOf(slot + 1));

                JedisPool jedisPool = nodeMap.get(entry.getValue());

                if (poolKeysMap.containsKey(jedisPool)) {
                    poolKeysMap.get(jedisPool).add(key);
                } else {
                    List<String> subKeyList = new ArrayList<String>();
                    subKeyList.add(key);
                    poolKeysMap.put(jedisPool, subKeyList);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return poolKeysMap;
    }

    /**
     * @return
     */
    protected Map<String, String> batchGetDataInner(Map<JedisPool, List<String>> poolKeysMap) {

        Map<String, String> resultMap = new HashMap<>();

        for (Map.Entry<JedisPool, List<String>> entry : poolKeysMap.entrySet()) {

            JedisPool jedisPool = entry.getKey();
            List<String> subkeys = entry.getValue();
            if (subkeys == null || subkeys.isEmpty()) {
                continue;
            }

            //申请jedis对象
            Jedis jedis = null;
            Pipeline pipeline = null;
            List<Object> subResultList = null;

            try {
                jedis = jedisPool.getResource();
                pipeline = jedis.pipelined();

                for (String key : subkeys) {
                    pipeline.smembers(key);
                }

                subResultList = pipeline.syncAndReturnAll();

            } catch (JedisConnectionException e) {

                e.getMessage();

            } catch (Exception e) {

                e.getMessage();

            } finally {
                if (pipeline != null) {
                    try {
                        pipeline.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //释放jedis对象
                if (jedis != null) {
                    jedis.close();
                }
            }

            if (subResultList == null || subResultList.isEmpty()) {
                continue;
            }

            if (subResultList.size() == subkeys.size()) {
                for (int i = 0; i < subkeys.size(); i++) {
                    String key = subkeys.get(i);
                    Object result = subResultList.get(i);
                    resultMap.put(key, (String) result);
                }
            } else {
                log.error("{} {}", "redis cluster pipeline error!", entry.toString());
            }
        }

        return resultMap;
    }

    protected void doLog(final String method, final int keyNum, final int nodeKeyNum, final StopWatch stopWatch) {

        stopWatch.stop();

        logger.info("MyRedisBatchService {} {} {} {} ms", method, keyNum, nodeKeyNum, stopWatch.getTime());
    }

}
