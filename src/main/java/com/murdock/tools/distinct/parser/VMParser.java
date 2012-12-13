/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.parser;

import java.io.File;

import com.murdock.tools.distinct.config.CharsetConfig;
import com.murdock.tools.distinct.util.IOUtils;

/**
 * @author weipeng 2012-12-3 ÏÂÎç7:09:47
 */
public class VMParser implements Parser<File, String> {

    /*
     * (non-Javadoc)
     * @see com.murdock.tools.distinct.parser.Parser#parser(java.lang.Object)
     */
    @Override
    public String parser(File input) {
        if (input == null) {
            return "";
        }

        if (!input.getPath().endsWith("vm") || input.getPath().contains("test") || input.getPath().contains("svn")) {
            return "";
        }

        return IOUtils.readFile(input, CharsetConfig.getCharset());
    }

}
