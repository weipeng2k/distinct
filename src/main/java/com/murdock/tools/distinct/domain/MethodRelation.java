/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.domain;

/**
 * @author weipeng 2012-12-3 下午3:34:09
 */
public class MethodRelation {

    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 代码行数
     */
    private int    codeLines;
    /**
     * 被调用的次数
     */
    private int    calledTimes;

    public void addCalledTime(int delta) {
        calledTimes += delta;
    }

    // ----------------------Getters and Setters-------------------------//

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getCodeLines() {
        return codeLines;
    }

    public void setCodeLines(int codeLines) {
        this.codeLines = codeLines;
    }

    public int getCalledTimes() {
        return calledTimes;
    }

    public void setCalledTimes(int calledTimes) {
        this.calledTimes = calledTimes;
    }

    // ----------------------Getters and Setters-------------------------//

    @Override
    public String toString() {
        return "MethodRelation [filePath=" + filePath + ", methodName=" + methodName + ", codeLines=" + codeLines
               + ", calledTimes=" + calledTimes + "]";
    }

}
