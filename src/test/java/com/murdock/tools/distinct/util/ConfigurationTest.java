package com.murdock.tools.distinct.util;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void test() throws Exception {

        List<String> list = AutoConfigUtils.getAutoConfigKeyNames(new File(
                                                                           "/home/weipeng/project/trading/common/config/src/conf/META-INF/autoconf/auto-config.xml"));

        for (String str : list) {
            System.out.println(str);
        }
    }

}
