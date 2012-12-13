/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.util;

import org.junit.Test;

/**
 * @author weipeng 2012-12-3 ÏÂÎç5:25:31
 */
public class IOUtilsTest {

    @Test
    public void test() {
        System.out.println(IOUtils.readFile("/home/weipeng/project/tradecenter/tradecenter.service/src/main/java/com/alibaba/china/tradecenter/enhancedmit/handler/PayOrderInvocationStatsAfterExecutionHandler.java",
                                            "GBK"));
    }

}
