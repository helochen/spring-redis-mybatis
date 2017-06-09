package com.chen.redisCache;

import com.chen.Util.SerializeUtil;
import com.chen.redis.RedisClient;
import org.apache.ibatis.cache.Cache;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Created by Chen on 2017/6/4.
 */
public class RedisCache implements Cache {
    private static Logger logger = Logger.getLogger(RedisCache.class);

    private Jedis redisClient = new RedisClient().getJedis();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private String id;

    public RedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug(">>>>>>>>>>>>>>>>myBatisRedisCache:id=" + id);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>putObject:" + key + "=" + value);
        redisClient.set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
    }

    @Override
    public Object getObject(Object key) {
        Object value = SerializeUtil.unserialize(redisClient.get(SerializeUtil.serialize(key.toString())));
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>getObject:" + key + "=" + value);
        return value;
    }
    @Override
    public Object removeObject(Object o) {
        return redisClient.expire(SerializeUtil.serialize(o.toString()), 0);
    }
    @Override
    public void clear() {
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>redis flush DB");
        redisClient.flushDB();
    }
    @Override
    public int getSize() {
        return Integer.valueOf(redisClient.dbSize().toString());
    }
    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
