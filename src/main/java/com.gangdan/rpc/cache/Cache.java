package com.gangdan.rpc.cache;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangzhuo on 16-12-25.
 */
public class Cache<KEY, VALUE> {

    @Getter
    @Setter
    private ConcurrentHashMap<KEY, VALUE> cache = new ConcurrentHashMap<KEY, VALUE>();

    /**
     * 写入操作
     * @param key
     * @param value
     */
    public void put(KEY key, VALUE value) {
        cache.put(key, value);
    }

    /**
     * 查询操作
     * @param key
     * @return
     */
    public VALUE get(KEY key) {
        return this.cache.get(key);
    }

    /**
     * 是否包含该key
     * @param key
     * @return
     */
    public boolean isConctains(KEY key) {
        return this.cache.contains(key);
    }


}
