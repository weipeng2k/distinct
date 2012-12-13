/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.util;

import java.io.File;

import org.junit.Test;

/**
 * @author weipeng 2012-12-3 ÏÂÎç3:49:00
 */
public class FileRecursionUtilsTest {

    static class Print implements FileExecuteCallback {

        @Override
        public void handle(File file) {
            if (file != null && file.getPath().endsWith("java")) {
                System.out.println(file.getName() + ", bytes:" + file.length());
            }
        }
    }

    @Test
    public void recursion() {
        long start = System.currentTimeMillis();
        FileRecursionUtils.recursion(new File("/home/weipeng/project"), new Print());

        System.out.println(System.currentTimeMillis() - start);
    }

}
