package com.murdock.tools.distinct.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author weipeng 2012-12-3 下午5:21:50
 */
public class IOUtils {

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * <pre>
     * 读取一个文件
     * 
     * </pre>
     * 
     * @param file
     * @return
     */
    public static final String readFile(File file, String charset) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                                                          charset != null ? charset : DEFAULT_CHARSET));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    br = null;
                }
            }
        }

        return sb.toString();
    }

    public static final String readFile(String filePath, String charset) {
        return readFile(new File(filePath), charset);
    }
}
