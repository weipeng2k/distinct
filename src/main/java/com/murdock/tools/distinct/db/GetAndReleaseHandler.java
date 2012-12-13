/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author weipeng 2012-12-6 œ¬ŒÁ4:42:22
 */
public class GetAndReleaseHandler implements InvocationHandler {

    private MatchDB matchDB = new MatchDBImpl();

    /*
     * (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            result = method.invoke(matchDB, args);
        } finally {
            // ∞Ô÷˙ Õ∑≈¡¥Ω”
            JedisPool.getPool().releaseConnection();
        }
        return result;
    }

}
