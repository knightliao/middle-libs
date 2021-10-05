package com.github.knightliao.middle.zk.domain.zk;

import java.util.List;
import java.util.concurrent.Executor;

import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.Watcher;

import com.github.knightliao.middle.zk.domain.select.ISelectState;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/5 17:33
 */
public interface IZkClient {

    void start();

    void stop();

    void listenConnectionState(ConnectionStateListener listener, Executor executor);

    boolean isAvailable();

    void create(String path, boolean ephemeral, byte[] data);

    void create(List<String> paths, boolean ephemeral);

    void setData(String path, byte[] data);

    String getDataAsString(String path);

    List<String> getChildren(String path);

    byte[] getData(String path);

    void delete(String path);

    void delete(List<String> paths);

    Watcher watchData(final String path, final Watcher watcher);

    Watcher watchChildren(final String path, final Watcher watcher);

    void unWatch(Watcher watcher);

    ISelectState joinSelect();
}


