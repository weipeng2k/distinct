/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author weipeng 2012-12-12 下午2:04:50
 */
public class NetworkUtils {

    /**
     * ping一下
     * 
     * @param domain
     * @param port
     * @return
     */
    public static final boolean ping(String domain, int port) {
        boolean result = false;
        Socket socket = null;
        try {
            socket = new Socket(domain, port);
            result = true;
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ex) {

                }
            }
        }

        return result;
    }

    /**
     * 尝试获取一个domain下的资源，根据http1.0协议尝试读取内容 当然，如果返回的是500系列的异常，那么将不会获取内容
     * 
     * @param domain
     * @param resource
     * @param port
     * @return
     */
    public static final List<String> readHttpContent(String domain, String resource, int port) {
        Socket socket = null;
        try {
            socket = new Socket(domain, port);

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            StringBuilder send = new StringBuilder();
            send.append("GET " + resource + " HTTP/1.0").append("\n");
            send.append("Accept: text/html").append("\n");
            send.append("\n");
            out.write(send.toString());
            out.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            List<String> result = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                if (line.contains("HTTP/1.1 5")) {
                    return Collections.emptyList();
                }

                result.add(line);
            }

            return result;
        } catch (Exception ex) {
            return Collections.emptyList();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ex) {

                }
            }
        }
    }
}
