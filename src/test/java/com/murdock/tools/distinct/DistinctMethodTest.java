/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.murdock.tools.distinct.domain.MethodRelation;

/**
 * @author weipeng 2012-12-3 ÏÂÎç8:18:40
 */
@Ignore
public class DistinctMethodTest {

    @Test
    public void constructDB() {
        DistinctMethod dm = new DistinctMethod();
        dm.constructDB(new File[] { new File("/home/weipeng/project/tradecenter") });
    }

    @Test
    public void cleanDB() {
        DistinctMethod dm = new DistinctMethod();
        dm.cleanDB();
    }
    
    @Test
    public void match() {
        DistinctMethod dm = new DistinctMethod();
        List<MethodRelation> list = dm.constructMethod(new File[] { new File("/home/weipeng/project/tradecenter") });
        
        System.out.println(list.size());
        dm.constructDB(new File[] { new File("/home/weipeng/project/tradecenter") });
        
        list = dm.matchMethod(list);
        
        for (MethodRelation mr : list) {
            if (mr.getCalledTimes() == 0) {
                System.out.println(mr);
            }
        }
    }
}
