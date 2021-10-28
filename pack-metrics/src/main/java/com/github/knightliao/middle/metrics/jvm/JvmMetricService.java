package com.github.knightliao.middle.metrics.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/28 23:48
 */
@Slf4j
public class JvmMetricService {

    public static final String INIT_HEAP = "0.init. heap";
    public static final String MAX_HEAP = "0.max.heap";
    public static final String USED_HEAP = "0.used.heap";
    public static final String COMMITTED_HEAP = "0.committed.heap";
    public static final String INIT_HEAP_NO = "1.init.heap.no";
    public static final String MAX_HEAP_NO = "1.max.heap.no";
    public static final String USED_HEAP_NO = "1.used.heap.no";
    public static final String COMMITTED_HEAP_NO = "1.committed.heap.no";
    public static final String TOTAL_MEM = "2.total.mem";
    public static final String FREE_MEM = "2.free.mem";
    public static final String MAX_MEM = "2.max.mem";

    public static Map<String, String> getMetrics() {

        Map<String, String> map = new HashMap<>();
        try {
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heap = memoryMXBean.getHeapMemoryUsage();
            map.put(INIT_HEAP, heap.getInit() / 1024 / 1024 + "Mb");
            map.put(MAX_HEAP, heap.getMax() / 1024 / 1024 + "Mb");
            map.put(USED_HEAP, heap.getUsed() / 1024 / 1024 + "Mb");
            map.put(COMMITTED_HEAP, heap.getCommitted() / 1024 / 1024 + "Mb");

            MemoryUsage noHeap = memoryMXBean.getNonHeapMemoryUsage();
            map.put(INIT_HEAP_NO, noHeap.getInit() / 1024 / 1024 + "Mb");
            map.put(MAX_HEAP_NO, noHeap.getMax() / 1024 / 1024 + "Mb");
            map.put(USED_HEAP_NO, noHeap.getUsed() / 1024 / 1024 + "Mb");
            map.put(COMMITTED_HEAP_NO, noHeap.getCommitted() / 1024 / 1024 + "Mb");

            map.put(TOTAL_MEM, Runtime.getRuntime().totalMemory() / 1024 / 1024 + "Mb");
            map.put(FREE_MEM, Runtime.getRuntime().freeMemory() / 1024 / 1024 + "Mb");
            map.put(MAX_MEM, Runtime.getRuntime().maxMemory() / 1024 / 1024 + "Mb");

        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return map;
    }
}