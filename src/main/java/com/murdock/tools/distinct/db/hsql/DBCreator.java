/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.db.hsql;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.murdock.tools.distinct.config.Constant;

/**
 * @author weipeng 2012-12-21 ÏÂÎç3:57:32
 */
public class DBCreator extends JdbcDaoSupport {
    
    static final String CREATE_PROJECT = "CREATE TABLE " + Constant.PROJECT_NAME_TABLE +  " ( " +
    		                                                                    " NAME VARCHAR(64), " +
    		                                                                    " PRIMARY KEY (NAME) " +
    		                                                               ");";
    
    static final String CREATE_METHOD = "CREATE TABLE " + Constant.METHOD_CONTENT_TABLE + " ( " + 
                                                                                " METHOD_BODY LONGVARCHAR, " +
                                                                                " METHOD_NAME VARCHAR(128), " +
                                                                                " PRIMARY KEY (METHOD_BODY) " +
                                                                            ");";
                                                                                
    
    public void initial() {
        getJdbcTemplate().execute(CREATE_PROJECT);
        getJdbcTemplate().execute(CREATE_METHOD);
    }

}
