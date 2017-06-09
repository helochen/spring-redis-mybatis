package com.chen.redisCache;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LoggingCache;

/**
 * Created by Chen on 2017/6/4.
 */
public class LoggingRedisCache extends LoggingCache {

    public LoggingRedisCache(String id) {
        super(new RedisCache(id));
    }
}
