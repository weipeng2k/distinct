/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct;

import java.io.File;
import java.util.List;

import org.junit.Test;

/**
 * @author weipeng 2012-12-13 ÉÏÎç10:14:19
 */
public class AntxConfigTest {

    @Test
    public void test() {
        AntxConfig ac = new AntxConfig();
        ac.constructProp("trading");
        ac.constructDK(new File("/home/weipeng/project/trading"));

        List<String> need = ac.needKV();
        for (String str : ac.needKV()) {
            System.out.println(str);
        }
        System.out.println(need.size());
        
        List<String> list = ac.doNotNeedKV();
        for (String str : list) {
            System.out.println(str);
        }
        System.out.println(list.size());
    }

}
