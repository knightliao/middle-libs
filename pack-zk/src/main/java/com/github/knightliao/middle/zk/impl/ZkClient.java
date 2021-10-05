package com.github.knightliao.middle.zk.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ProtectACLCreateModePathAndBytesable;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import com.github.knightliao.middle.utils.lang.ExceptionUtils;
import com.github.knightliao.middle.zk.domain.WatcherWrapper;
import com.github.knightliao.middle.zk.domain.select.ISelectState;
import com.github.knightliao.middle.zk.domain.zk.IZkClient;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/5 17:33
 */
@Slf4j
public class ZkClient implements IZkClient {

    private static final int DEFAULT_SESSION_TIMEOUT = 30 * 1000;

    private String zkAddr;
    private String rootPath;
    private int sessionTimeouttimeoutMs = DEFAULT_SESSION_TIMEOUT;

    private volatile CuratorFramework curatorFramework;
    private Set<WatcherWrapper> watchers = Sets.newConcurrentHashSet();

    public ZkClient(String zkAddr, String rootPath, int sessionTimeouttimeoutMs) {

        Preconditions.checkNotNull(zkAddr);
        Preconditions.checkNotNull(rootPath);

        this.zkAddr = zkAddr;
        this.rootPath = rootPath;
        this.sessionTimeouttimeoutMs = sessionTimeouttimeoutMs;
    }

    // 使用curator来进行Zk管理
    @Override
    public void start() {

        Preconditions.checkArgument(curatorFramework != null);
        Preconditions.checkArgument(StringUtils.isNotEmpty(zkAddr));
        if (rootPath.startsWith("/")) {
            rootPath = rootPath.substring(1);
        }

        //
        curatorFramework = CuratorFrameworkFactory.builder().connectString(zkAddr)
                .namespace(rootPath)
                .retryPolicy(new RetryNTimes(0, 1000))
                .connectionTimeoutMs((int) (sessionTimeouttimeoutMs * 0.3))
                .sessionTimeoutMs(sessionTimeouttimeoutMs)
                .build();
        curatorFramework.start();

        //
        try {
            boolean success = curatorFramework.blockUntilConnected(5, TimeUnit.SECONDS);
            log.info("start ZkClient {}", success);
        } catch (InterruptedException ex) {
            throw Throwables.propagate(ex);
        }
    }

    // 清理zk的关注信息
    @Override
    public synchronized void stop() {

        if (curatorFramework != null) {
            for (WatcherWrapper watcherWrapper : watchers) {
                curatorFramework.clearWatcherReferences(watcherWrapper);
            }

            watchers.clear();
            curatorFramework.close();
        }
    }

    @Override
    public void listenConnectionState(ConnectionStateListener listener, Executor executor) {

        Preconditions.checkArgument(curatorFramework != null);
        curatorFramework.getConnectionStateListenable().addListener(listener, executor);
    }

    @Override
    public boolean isAvailable() {

        try {
            return this.curatorFramework.blockUntilConnected(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            throw Throwables.propagate(ex);
        }
    }

    @Override
    public void create(String path, boolean ephemeral, byte[] data) {

        Preconditions.checkArgument(curatorFramework != null);
        log.info("create path={} ephemeral={}", path, ephemeral);

        try {
            if (data == null) {
                ProtectACLCreateModePathAndBytesable<String> protectACLCreateModePathAndBytesable =
                        curatorFramework.create().creatingParentContainersIfNeeded();
                if (ephemeral) {
                    protectACLCreateModePathAndBytesable.withProtection().withMode(CreateMode.EPHEMERAL).forPath(path);
                } else {
                    protectACLCreateModePathAndBytesable.withMode(CreateMode.PERSISTENT).forPath(path);
                }
            } else {

                curatorFramework.create().creatingParentContainersIfNeeded().withProtection().withMode(ephemeral ?
                        CreateMode.EPHEMERAL : CreateMode.PERSISTENT).forPath(path, data);
            }
        } catch (Exception e) {

            if (ephemeral && ExceptionUtils.exceptionMatch(e, KeeperException.NodeExistsException.class)) {
                try {
                    long sessionId = curatorFramework.getZookeeperClient().getZooKeeper().getSessionId();
                    Stat stat = curatorFramework.checkExists().forPath(path);
                    if (stat != null && stat.getEphemeralOwner() != sessionId) {
                        throw new RuntimeException("node exist and conflict");
                    }
                } catch (Exception ex) {
                    log.error(ex.toString());
                }
            }

            throw Throwables.propagate(e);
        }
    }

    @Override
    public void create(List<String> paths, boolean ephemeral) {

        Preconditions.checkArgument(curatorFramework != null);
        log.info("create paths={} ephemeral={}", paths, ephemeral);

        try {

            CuratorTransaction curatorTransaction = curatorFramework.inTransaction();
            for (int i = 0; i < paths.size(); ++i) {
                String path = paths.get(i);
                curatorFramework.createContainers(ZKPaths.getPathAndNode(path).getPath());

                boolean last = i == paths.size() - 1;
                CuratorTransactionFinal curatorTransactionFinal = curatorTransaction.create()
                        .withMode(ephemeral ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT).forPath(path).and();

                if (!last) {
                    curatorTransaction = curatorTransactionFinal;
                } else {
                    curatorTransactionFinal.commit();
                }
            }

        } catch (Exception e) {

            if (ephemeral && ExceptionUtils.exceptionMatch(e, KeeperException.NodeExistsException.class)) {
                try {
                    long sessionId = curatorFramework.getZookeeperClient().getZooKeeper().getSessionId();

                    for (String path : paths) {
                        Stat stat = curatorFramework.checkExists().forPath(path);
                        if (stat != null && stat.getEphemeralOwner() != sessionId) {
                            throw new RuntimeException("node exist and conflict");
                        }
                    }
                } catch (Exception ex) {
                    log.error(ex.toString());
                }
            }

            throw Throwables.propagate(e);
        }

    }

    @Override
    public void setData(String path, byte[] data) {

        Preconditions.checkArgument(curatorFramework != null);
        log.info("setData path={}", path);

        try {
            if (data == null) {
                curatorFramework.setData().forPath(path);
            } else {
                curatorFramework.setData().forPath(path, data);
            }
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }
    }

    @Override
    public String getDataAsString(String path) {

        byte[] data = getData(path);
        if (data == null) {
            return null;
        }

        try {
            return new String(data, 0, data.length, "UTF8");
        } catch (UnsupportedEncodingException ex) {
            throw Throwables.propagate(ex);
        }
    }

    @Override
    public List<String> getChildren(String path) {

        Preconditions.checkArgument(curatorFramework != null);

        try {
            return curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public byte[] getData(String path) {

        Preconditions.checkArgument(curatorFramework != null);

        try {
            return curatorFramework.getData().forPath(path);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void delete(String path) {

        Preconditions.checkArgument(curatorFramework != null);
        log.info("delete {}", path);

        try {
            curatorFramework.delete().guaranteed().forPath(path);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void delete(List<String> paths) {

        Preconditions.checkArgument(curatorFramework != null);
        log.info("delete {}", paths);

        try {

            CuratorTransaction curatorTransaction = curatorFramework.inTransaction();
            for (int i = 0; i < paths.size(); ++i) {
                String path = paths.get(i);

                boolean last = i == paths.size() - 1;
                if (curatorFramework.checkExists().creatingParentContainersIfNeeded().forPath(path) == null) {
                    continue;
                }

                CuratorTransactionFinal curatorTransactionFinal = curatorTransaction.delete().forPath(path).and();
                if (!last) {
                    curatorTransaction = curatorTransactionFinal;
                } else {
                    curatorTransactionFinal.commit();
                }
            }
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }
    }

    @Override
    public Watcher watchData(String path, Watcher watcher) {

        Preconditions.checkArgument(curatorFramework != null);
        log.info("watchData {}", path);

        WatcherWrapper watcherWrapper = watcher instanceof WatcherWrapper ?
                (WatcherWrapper) watcher : new WatcherWrapper(watcher) {
            @Override
            public void process(WatchedEvent watchedEvent) {

                try {

                    if (isAvailable()) {
                        curatorFramework.getChildren().usingWatcher(this).forPath(path);
                    } else {
                        log.info("zk异常，不再重新watch, 由上层逻辑重新watch处理");
                    }
                } catch (KeeperException.ConnectionLossException ex) {
                    log.info("connection lost {} ", ex.getMessage(), ex);

                } catch (Exception ex) {
                    if (!omitException(ex)) {
                        log.error("watchData process watch data error, path {}", path, ex);
                    }
                }

                try {
                    log.info("receive event: {}", watchedEvent);
                    watcher.process(watchedEvent);
                } catch (Exception ex) {

                    if (omitException(ex)) {
                        return;
                    }
                    log.error("watchData process watchedEvent: {}  fail", watchedEvent);
                }
            }
        };

        try {
            curatorFramework.createContainers(path);
            curatorFramework.getChildren().usingWatcher(watcherWrapper).forPath(path);
            watchers.add(watcherWrapper);
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }

        return watcherWrapper;
    }

    @Override
    public Watcher watchChildren(String path, Watcher watcher) {

        Preconditions.checkArgument(curatorFramework != null);
        log.info("watchChildren {}", path);

        WatcherWrapper watcherWrapper = watcher instanceof WatcherWrapper ?
                (WatcherWrapper) watcher : new WatcherWrapper(watcher) {
            @Override
            public void process(WatchedEvent watchedEvent) {

                try {

                    if (isAvailable()) {
                        curatorFramework.getData().usingWatcher(this).forPath(path);
                    } else {
                        log.info("zk异常，不再重新watch, 由上层逻辑重新watch处理");
                    }
                } catch (KeeperException.ConnectionLossException ex) {
                    log.info("connection lost {} ", ex.getMessage(), ex);

                } catch (Exception ex) {
                    if (!omitException(ex)) {
                        log.error("watchData process watch data error, path {}", path, ex);
                    }
                }

                try {
                    log.info("receive event: {}", watchedEvent);
                    watcher.process(watchedEvent);
                } catch (Exception ex) {

                    if (omitException(ex)) {
                        return;
                    }
                    log.error("watchData process watchedEvent: {}  fail", watchedEvent);
                }
            }
        };

        try {
            curatorFramework.getData().usingWatcher(watcherWrapper).forPath(path);
            watchers.add(watcherWrapper);
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }

        return watcherWrapper;
    }

    @Override
    public void unWatch(Watcher watcher) {

        Preconditions.checkArgument(curatorFramework != null);
        log.info("unWatch {}", watcher);

        curatorFramework.clearWatcherReferences(watcher);
        watchers.remove(watcher);
    }

    @Override
    public ISelectState joinSelect() {
        return null;
    }

    private boolean omitException(Exception ex) {
        if (curatorFramework.getState() != CuratorFrameworkState.STARTED) {

            if (ex instanceof IllegalStateException ||
                    ex.getCause() != null && ex.getCause() instanceof IllegalStateException) {
                log.info("非启动状态, {}", ex.getMessage(), ex);
            }

            return true;
        }

        return false;
    }
}
