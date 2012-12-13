/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.config;

/**
 * @author weipeng 2012-12-3 ÏÂÎç5:35:57
 */
public class CharsetConfig {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static String       charset;

    static {
        charset = DEFAULT_CHARSET;
    }

    public static String getCharset() {
        return charset;
    }

    public static void setCharset(String charset) {
        CharsetConfig.charset = charset;
    }

}
