/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.murdock.tools.distinct.util.AutoConfigUtils;
import com.murdock.tools.distinct.util.FileExecuteCallback;
import com.murdock.tools.distinct.util.FileRecursionUtils;
import com.murdock.tools.distinct.util.NetworkUtils;

/**
 * @author weipeng 2012-12-12 下午1:26:15
 */
public class AntxConfig {

    /**
     * 目前已有的配置
     */
    private Properties  prop;
    /**
     * 需要KEY
     */
    private Set<String> needKey;

    public void constructProp(String appName) {
        if (appName == null) {
            throw new RuntimeException();
        }

        String resource = "/antxconfig/figo/figo.htm?project=" + appName + "&group=test";
        List<String> rows = NetworkUtils.readHttpContent("dzone.alibaba-inc.com", resource, 1999);

        if (rows == null || rows.isEmpty()) {
            return;
        }
        prop = new Properties();
        for (String row : rows) {
            if (row.contains("=") && row.contains(".")) {
                String[] kv = row.split("=");
                prop.setProperty(kv[0].trim(), kv[1].trim());
            }
        }
    }

    public void constructDK(File file) {
        needKey = new HashSet<String>();

        FileRecursionUtils.recursion(file, new AutoConfigCallback());
    }

    class AutoConfigCallback implements FileExecuteCallback {

        @Override
        public void handle(File file) {
            if (file != null && !file.getPath().contains(".svn") && file.getName().equals("auto-config.xml")) {
                List<String> keys = AutoConfigUtils.getAutoConfigKeyNames(file);
                for (String key : keys) {
                    if (!key.startsWith("@")) {
                        needKey.add(key.trim());
                    }
                }
            }
        }
    }

    public List<String> doNotNeedKV() {
        if (isConstructDK() && isConstructProp()) {
            for (String str : needKey) {
                prop.remove(str);
            }

            List<String> result = new ArrayList<String>();
            for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                result.add(sb.toString());
            }

            return result;
        }

        return Collections.emptyList();
    }

    public List<String> needKV() {
        if (isConstructDK() && isConstructProp()) {
            List<String> result = new ArrayList<String>();

            for (String str : needKey) {
                String value = prop.getProperty(str);

                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("=");
                sb.append(value);
                result.add(sb.toString());
            }

            return result;
        }

        return Collections.emptyList();
    }

    /**
     * 是否已经构建了Prop
     * 
     * @return
     */
    public boolean isConstructProp() {
        return prop != null;
    }

    /**
     * 是否构建了需要删除的Prop
     * 
     * @return
     */
    public boolean isConstructDK() {
        return needKey != null;
    }

    public void deleteProp() {
        prop = null;
    }

    public void deleteDK() {
        needKey = null;
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public Set<String> getNeedDeleteKey() {
        return needKey;
    }

    public void setNeedDeleteKey(Set<String> needDeleteKey) {
        this.needKey = needDeleteKey;
    }

}
