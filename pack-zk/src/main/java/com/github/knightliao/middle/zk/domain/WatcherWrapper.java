package com.github.knightliao.middle.zk.domain;

import org.apache.zookeeper.Watcher;

import com.google.common.base.Preconditions;

import lombok.Getter;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/5 17:38
 */
@Getter
public abstract class WatcherWrapper implements Watcher {

    protected Watcher rawWatcher;

    public WatcherWrapper(Watcher watcher) {
        Preconditions.checkNotNull(watcher);
        this.rawWatcher = watcher;
    }
}
