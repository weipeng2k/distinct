/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.murdock.tools.distinct.config.CharsetConfig;
import com.murdock.tools.distinct.config.Constant;
import com.murdock.tools.distinct.util.IOUtils;

/**
 * @author weipeng 2012-12-3 下午4:11:22
 */
public class JavaMethodBodyParser implements Parser<File, List<Map<String, String>>> {

    /*
     * (non-Javadoc)
     * @see com.murdock.tools.distinct.parser.Parser#parser(java.lang.Object)
     */
    @Override
    public List<Map<String, String>> parser(File input) {
        if (input == null) {
            return Collections.emptyList();
        }

        if (!input.getPath().endsWith("java") || input.getPath().contains("test") || input.getPath().contains("svn")) {
            return Collections.emptyList();
        }

        String content = IOUtils.readFile(input, CharsetConfig.getCharset());
        // 设置文件
        ASTParser parsert = ASTParser.newParser(AST.JLS3);
        parsert.setSource(content.toCharArray());

        // 分析方法
        CompilationUnit result = (CompilationUnit) parsert.createAST(null);
        TypeDeclaration typeDec = (TypeDeclaration) result.types().get(0);
        MethodDeclaration methodDec[] = typeDec.getMethods();

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (MethodDeclaration method : methodDec) {
            if (method.getBody() != null) {
                Map<String, String> item = new HashMap<String, String>(4);
                item.put(Constant.METHOD_BODY, method.getBody().toString());
                item.put(Constant.METHOD_NAME, method.getName().toString());
                list.add(item);
            }
        }

        return list;
    }
}
