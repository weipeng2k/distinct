package com.murdock.tools.distinct.config;

/**
 * @author weipeng 2012-12-3 обнГ5:35:57
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
