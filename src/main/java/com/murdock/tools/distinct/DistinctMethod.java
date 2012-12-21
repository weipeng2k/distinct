/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.murdock.tools.distinct.config.Constant;
import com.murdock.tools.distinct.db.MatchDB;
import com.murdock.tools.distinct.domain.MethodRelation;
import com.murdock.tools.distinct.parser.JavaMethodBodyParser;
import com.murdock.tools.distinct.parser.JavaMethodRelationParser;
import com.murdock.tools.distinct.parser.VMParser;
import com.murdock.tools.distinct.util.FileExecuteCallback;
import com.murdock.tools.distinct.util.FileRecursionUtils;
import com.murdock.tools.distinct.util.IOUtils;

/**
 * @author weipeng 2012-12-3 下午8:06:33
 */
public class DistinctMethod {

    private MatchDB         matchDB;

    private ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);

    public DistinctMethod(){
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                pool.shutdownNow();
            }
        });
    }

    public void constructDB(File[] files) {
        if (files == null || files.length <= 0) {
            return;
        }

        for (File file : files) {

            try {
                File pom = new File(file.getPath() + File.separator + "pom.xml");
                if (pom.exists()) {
                    String pomString = IOUtils.readFile(pom, "UTF-8");

                    // 包含了name
                    if (pomString.contains("<name>") && pomString.contains("</name>")) {
                        int start = pomString.indexOf("<name>") + "<name>".length();
                        int end = pomString.indexOf("</name>");

                        String projectName = pomString.substring(start, end);
                        matchDB.addProject(projectName);
                    }
                }
            } catch (Exception ex) {

            }

            FileRecursionUtils.recursion(file, new FileExecuteCallback() {

                JavaMethodBodyParser javaMethodBodyParser = new JavaMethodBodyParser();

                VMParser             vmParser             = new VMParser();

                @Override
                public void handle(File file) {
                    try {
                        List<Map<String, String>> result = javaMethodBodyParser.parser(file);
                        if (result != null && !result.isEmpty()) {
                            for (Map<String, String> item : result) {
                                // 生成Java文件将会以body为KEY，name为value
                                matchDB.insert(item.get(Constant.METHOD_BODY), item.get(Constant.METHOD_NAME));
                            }
                        }
                    } catch (Exception ex) {
                    }

                    String vm = vmParser.parser(file);

                    if (vm != null && !"".equals(vm)) {
                        matchDB.insert(vm);
                    }
                }
            });
        }
    }

    public List<MethodRelation> constructMethod(File[] files) {
        if (files == null || files.length <= 0) {
            return Collections.emptyList();
        }

        final List<MethodRelation> list = new ArrayList<MethodRelation>(512);

        for (File file : files) {
            FileRecursionUtils.recursion(file, new FileExecuteCallback() {

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
        }

        return list;
    }

    /**
     * @param list
     * @return
     */
    public List<MethodRelation> matchMethod(List<MethodRelation> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        CountDownLatch cdl = new CountDownLatch(list.size());

        for (MethodRelation mr : list) {
            Job job = new Job();
            job.cdl = cdl;
            job.mr = mr;

            pool.execute(job);
        }

        try {
            cdl.await();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        return list;
    }

    public void cleanDB() {
        matchDB.clean();
    }

    public int userNum() {
        return matchDB.onlineUsers();
    }

    public List<String> getProjectNames() {
        return matchDB.getProjectNames();
    }

    public long getSize() {
        return matchDB.dbsize();
    }

    class Job implements Runnable {

        CountDownLatch cdl;

        MethodRelation mr;

        @Override
        public void run() {
            try {
                int matchCount = matchDB.match(mr.getMethodName());
                System.out.println(cdl.getCount());
                mr.addCalledTime(matchCount);
            } finally {
                cdl.countDown();
            }
        }
    }

    public void setMatchDB(MatchDB matchDB) {
        this.matchDB = matchDB;
    }

    public static void main(String[] args) {
        String pomString = "<name>sdfsdfsdf</name>";
        int start = pomString.indexOf("<name>") + "<name>".length();
        int end = pomString.indexOf("</name>");

        String projectName = pomString.substring(start, end);
        System.out.println(projectName);
    }
}
