/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.murdock.tools.distinct.domain.MethodRelation;
import com.murdock.tools.distinct.util.FileExecuteCallback;
import com.murdock.tools.distinct.util.FileRecursionUtils;
import com.murdock.tools.distinct.util.IOUtils;

/**
 * 使用该测试可以获取项目的AO
 * 
 * @author weipeng 2012-12-20 上午9:25:28
 */
public class AOTest {

    @Test
    public void test() {

        final List<MethodRelation> list = new ArrayList<MethodRelation>();

        FileRecursionUtils.recursion(new File("/home/weipeng/project/trading"), new FileExecuteCallback() {

            JavaMethodRelationParser javaMethodRelationParser = new JavaMethodRelationParser();

            @Override
            public void handle(File file) {
                try {
                    List<MethodRelation> result = javaMethodRelationParser.parser(file);
                    if (result != null && !result.isEmpty()) {
                        list.addAll(result);
                    }
                } catch (Exception ex) {
                }
            }
        });

        for (MethodRelation mr : list) {
            if (mr.getFilePath().contains("AO") && mr.getMethodName().startsWith("do")) {
                File file = new File(mr.getFilePath());
                String fileName = file.getName().substring(0, file.getName().length() - 5);

                System.out.println(fileName + "." + mr.getMethodName());
            }
        }
    }

    @Test
    public void upperOne() {
        String content = IOUtils.readFile("/home/weipeng/文档/项目文档/代码下线/web01-1219.txt", "UTF-8");

        String[] rows = content.split("\n");

        for (String row : rows) {


                String className = row.split("\\.")[0];
                String methodAndTail = row.split("\\.")[1];

                Character firstUpper = Character.toUpperCase(className.charAt(0));
                Character secondUpper = Character.toUpperCase(methodAndTail.charAt(0));
                System.out.println(firstUpper + className.substring(1, className.length()) + ".do" + secondUpper
                                   + methodAndTail.substring(1, methodAndTail.length()));
        }
    }

    @Test
    public void uselessAO() {
        String content = IOUtils.readFile("/home/weipeng/文档/项目文档/代码下线/webupper.txt", "UTF-8");
        String[] rows = content.split("\n");

        Set<String> stillWorkAO = new HashSet<String>();
        for (String row : rows) {
            stillWorkAO.add(row.split("=")[0].trim());
        }
        
        content = IOUtils.readFile("/home/weipeng/文档/项目文档/代码下线/ao.txt", "UTF-8");
        rows = content.split("\n");
        Set<String> allAO = new HashSet<String>();
        for (String row : rows) {
            allAO.add(row.trim());
        }

        Iterator<String> iterator = allAO.iterator();
        while (iterator.hasNext()) {
            String currentRow = iterator.next();

            if (stillWorkAO.contains(currentRow)) {
                iterator.remove();
                stillWorkAO.remove(currentRow);
            }
        }
        
        //无用的
        System.out.println("===============useless=============");
        for (String row : allAO) {
            System.out.println(row);
        }
        
        System.out.println("===============Not static=============");
        for (String row : stillWorkAO) {
            System.out.println(row);
        }

    }
}
