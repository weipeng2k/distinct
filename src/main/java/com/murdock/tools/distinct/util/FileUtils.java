/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weipeng 2012-12-4 ÏÂÎç3:53:02
 */
public class FileUtils {

    public static final File[] convert(String path) throws FileNotFoundException {
        if (path == null) {
            return null;
        }

        String[] pathArray = path.split(",");

        List<File> files = new ArrayList<File>();
        for (String p : pathArray) {
            File file = new File(p);

            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            files.add(file);
        }

        return files.toArray(new File[] {});
    }
}
