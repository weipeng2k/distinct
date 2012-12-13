/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.parser;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.murdock.tools.distinct.domain.MethodRelation;

/**
 * @author weipeng 2012-12-3 ÏÂÎç5:45:37
 */
public class MethodParserTest {

    @Test
    public void test() {
        Parser<File, List<MethodRelation>> parser = new JavaMethodRelationParser();
        List<MethodRelation> list = parser.parser(new File(
                               "/home/weipeng/project/distinct/src/main/java/com/murdock/tools/distinct/parser/Parser.java"));
        
        System.out.println(list);
    }
}
