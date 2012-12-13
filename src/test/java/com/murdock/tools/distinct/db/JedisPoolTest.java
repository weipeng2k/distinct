package com.murdock.tools.distinct.db;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class JedisPoolTest {

    @Test
    public void test() {

        long start = System.currentTimeMillis();
        Jedis jedis = JedisPool.getPool().getConnection();

        for (int i = 0; i < 100000; i++) {
            jedis.set("1", "2");
        }

        JedisPool.getPool().releaseConnection();
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            Jedis jedis1 = JedisPool.getPool().getConnection();
            jedis1.set("1", "2");
            JedisPool.getPool().releaseConnection();
        }

        System.out.println(System.currentTimeMillis() - start);
    }

}
