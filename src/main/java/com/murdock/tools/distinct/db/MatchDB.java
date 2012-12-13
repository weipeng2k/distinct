/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.db;

import java.util.List;

/**
 * @author weipeng 2012-12-6 下午3:59:54
 */
public interface MatchDB {

    /**
     * 加入一条记录
     * 
     * @param row
     * @return
     */
    boolean insert(String row);

    /**
     * 加入key value
     * 
     * @param row
     * @param value
     * @return
     */
    boolean insert(String row, String value);

    /**
     * @param jedis
     */
    void addProject(String projectName);

    /**
     * @param jedis
     * @return
     */
    List<String> getProjectNames();

    /**
     * <pre>
     * 匹配字符串，返回多少个
     * 
     * </pre>
     * 
     * @param sample
     * @return
     */
    int match(String sample);

    /**
     * 在线用户
     * 
     * @return
     */
    int onlineUsers();

    /**
     * <pre>
     * 清空DB
     * 
     * </pre>
     * 
     * @return
     */
    boolean clean();

    /**
     * 当前的key数量
     * 
     * @return
     */
    long dbsize();

}
