/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.parser;

/**
 * <pre>
 * ½âÎöÆ÷
 * 
 * </pre>
 * 
 * @author weipeng 2012-12-3 ÏÂÎç4:12:23
 */
public interface Parser<Input, Output> {

    /**
     * <pre>
     * ½âÎö
     * 
     * </pre>
     * 
     * @param input
     * @return
     */
    Output parser(Input input);
}
