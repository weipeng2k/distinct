/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weipeng 2012-12-4 ÏÂÎç4:35:21
 */
public class TextUtils {

    public static final String[] getInputs(String str) {
        if (str == null) {
            return null;
        }

        String[] splits = str.split(",");

        List<String> list = new ArrayList<String>();

        for (String split : splits) {
            if (!split.trim().equals("")) {
                list.add(split);
            }
        }

        return list.toArray(new String[] {});
    }

    public static void main(String[] args) {
        System.out.println("sdfsdf,".split(",").length);
        System.out.println(",sdfsdf,".split(",").length);
        System.out.println(getInputs("sdfsdf,").length);
        System.out.println(getInputs(",sdfsdf,").length);
    }
}
