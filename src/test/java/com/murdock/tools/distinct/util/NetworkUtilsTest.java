/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.util;

import org.junit.Test;

/**
 * @author weipeng 2012-12-12 ÏÂÎç2:07:33
 */
public class NetworkUtilsTest {

    @Test
    public void ping() {
        System.out.println(NetworkUtils.ping("www.sohu.com", 80));
        System.out.println(NetworkUtils.ping("www.intel.com", 80));
        System.out.println(NetworkUtils.ping("dzone.alibaba-inc.com", 1999));
    }

    @Test
    public void read() {
        for (String s : NetworkUtils.readHttpContent("dzone.alibaba-inc.com", "/antxconfig/figo/figo.htm?project=trading&group=test", 1999)) {
            System.out.println(s);
        }
    }
}
