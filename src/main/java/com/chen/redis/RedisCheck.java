package com.chen.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by Chen on 2017/6/4.
 */
public class RedisCheck {
    public static void main(String[] args) {
        Jedis jedis = new RedisClient().getJedis();
        jedis.flushDB();
    }
}
