package com.github.knightliao.middle.metrics.tomcat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/29 00:00
 */
@Slf4j
public class TomcatMetricService {

    public static final String TOMCAT_THREAD_BUSY = "0.tomcat.thread.busy";
    public static final String TOMCAT_THREAD_MAX = "1.tomcat.thread.max";
    public static final String TOMCAT_THREAD_CURRENT = "2.tomcat.thread.current";
    public static final String TOMCAT_THREAD_CONNECTION_COUNT = "3.tomcat.thread.connectionCount";
    public static final String TOMCAT_THREAD_MAX_CONNECTIONS = "4.tomcat.thread.maxConnections";

    @Resource
    private MBeanServer mBeanServer;

    public Map<String, Long> getMetrics() {

        Map<String, Long> map = new HashMap<>();

        try {

            Set<ObjectName> objectNameSet = mBeanServer.queryNames(new ObjectName("Tomcat:type=ThreadPool,*"), null);

            if (objectNameSet != null && objectNameSet.size() == 1) {

                for (ObjectName objectName : objectNameSet) {

                    long maxThreads = (Integer) mBeanServer.getAttribute(objectName, "maxThreads");
                    long currentThreadCount = (Integer) mBeanServer.getAttribute(objectName, "currentThreadCount");
                    long currentThreadsBusy = (Integer) mBeanServer.getAttribute(objectName, "currentThreadsBusy");
                    long connectionCount = (Integer) mBeanServer.getAttribute(objectName, "connectionCount");
                    long maxConnections = (Integer) mBeanServer.getAttribute(objectName, "maxConnections");

                    map.put(TOMCAT_THREAD_BUSY, currentThreadsBusy);
                    map.put(TOMCAT_THREAD_MAX, maxThreads);
                    map.put(TOMCAT_THREAD_CURRENT, currentThreadCount);
                    map.put(TOMCAT_THREAD_MAX, connectionCount);
                    map.put(TOMCAT_THREAD_MAX_CONNECTIONS, maxConnections);

                }
            }
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
        }

        return map;
    }
}
