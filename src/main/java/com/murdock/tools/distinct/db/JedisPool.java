/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.db;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;

import redis.clients.jedis.Jedis;

import com.murdock.tools.distinct.config.Constant;

/**
 * @author weipeng 2012-12-6 ÏÂÎç3:49:22
 */
public class JedisPool {

    private static final JedisPool pool               = new JedisPool();

    private ThreadLocal<Jedis>     JEDIS_THREAD_LOACL = new ThreadLocal<Jedis>();

    public static final JedisPool getPool() {
        return pool;
    }

    private LinkedBlockingDeque<Jedis> queue = new LinkedBlockingDeque<Jedis>();

    private JedisPool(){
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("redis.properties"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        int num = Integer.parseInt(properties.getProperty("redis.connection", "10").toString().trim());

        if (num <= 0) {
            throw new IllegalArgumentException("redis.connection must greater than 0");
        }
        for (int i = 0; i < num; i++) {
            Jedis jedis = new Jedis(properties.get("redis.ip").toString().trim(),
                                    Integer.parseInt(properties.get("redis.port").toString().trim()));
            queue.addLast(jedis);
        }

        Jedis jedis = getConnection();
        int user = jedis.get(Constant.USER_COUNT) != null ? Integer.parseInt(jedis.get(Constant.USER_COUNT)) : 0;
        user++;
        jedis.set(Constant.USER_COUNT, String.valueOf(user));

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                Jedis jedis = getConnection();
                int user = jedis.get(Constant.USER_COUNT) != null ? Integer.parseInt(jedis.get(Constant.USER_COUNT)) : 0;
                user--;
                jedis.set(Constant.USER_COUNT, String.valueOf(user));
            }
        });
    }

    public Jedis getConnection() {
        Jedis jedis = null;
        if (JEDIS_THREAD_LOACL.get() != null) {
            return JEDIS_THREAD_LOACL.get();
        }
        try {
            jedis = queue.takeFirst();
            if (jedis != null) {
                JEDIS_THREAD_LOACL.set(jedis);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return jedis;
    }

    public void releaseConnection() {
        Jedis jedis = JEDIS_THREAD_LOACL.get();
        if (jedis != null) {
            queue.offerLast(jedis);
            JEDIS_THREAD_LOACL.set(null);
        }
    }
}
