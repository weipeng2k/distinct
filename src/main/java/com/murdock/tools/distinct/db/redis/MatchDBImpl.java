/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.db.redis;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.murdock.tools.distinct.config.Constant;
import com.murdock.tools.distinct.db.MatchDB;

/**
 * <pre>
 * 能够在启动和关闭时完成用户数量的计算，使用Redis来完成
 * 中央节点存储
 * 
 * </pre>
 * 
 * @author weipeng 2012-12-3 下午7:37:54
 */
public class MatchDBImpl implements MatchDB {

    /**
     * 加入一条记录
     * 
     * @param row
     * @return
     */
    public boolean insert(String row) {
        if (row != null && !"".equals(row)) {
            JedisPool.getPool().getConnection().set(row, "1");
            return true;
        }

        return false;
    }

    /**
     * 加入一条记录
     * 
     * @param row
     * @return
     */
    public boolean insert(String row, String value) {
        if (row != null && !"".equals(row)) {
            JedisPool.getPool().getConnection().set(row, value);
            return true;
        }

        return false;
    }

    public void addProject(String projectName) {
        if (projectName != null) {
            JedisPool.getPool().getConnection().rpush(Constant.PROJECT_NAME, projectName);
        }
    }

    public List<String> getProjectNames() {
        Long length = JedisPool.getPool().getConnection().llen(Constant.PROJECT_NAME);

        if (length > 0) {
            return JedisPool.getPool().getConnection().lrange(Constant.PROJECT_NAME, 0, length.intValue());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * <pre>
     * 匹配字符串，返回多少个
     * 
     * </pre>
     * 
     * @param sample
     * @return
     */
    public int match(String sample) {
        int result = 0;

        if (sample != null && !"".equals(sample)) {
            String pattern = "*" + sample + "(*";
            Set<String> resultSet = JedisPool.getPool().getConnection().keys(pattern);

            result = resultSet != null ? resultSet.size() : 0;

            // 只有一个关联，需要看一下是不是自己
            if (result == 1) {
                String value = JedisPool.getPool().getConnection().get((String) resultSet.toArray()[0]);

                // 是自己
                if (sample.equals(value)) {
                    result = 0;
                }
            }
        }

        return result;
    }

    /**
     * 在线用户
     * 
     * @return
     */
    public int onlineUsers() {
        String count = JedisPool.getPool().getConnection().get(Constant.USER_COUNT);
        return Integer.parseInt(count != null ? count : "0");
    }

    /**
     * <pre>
     * 清空DB
     * 
     * </pre>
     * 
     * @return
     */
    public boolean clean() {
        JedisPool.getPool().getConnection().flushAll();
        int user = Integer.parseInt(JedisPool.getPool().getConnection().get(Constant.USER_COUNT) != null ? JedisPool.getPool().getConnection().get(Constant.USER_COUNT) : "0");
        user++;
        JedisPool.getPool().getConnection().set(Constant.USER_COUNT, String.valueOf(user));
        return true;
    }

    @Override
    public long dbsize() {
        return JedisPool.getPool().getConnection().dbSize();
    }
}
