/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.murdock.tools.distinct;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.murdock.tools.distinct.domain.MethodRelation;
import com.murdock.tools.distinct.util.FileUtils;
import com.murdock.tools.distinct.util.NetworkUtils;
import com.murdock.tools.distinct.util.TextUtils;

public class Main extends JFrame {

    private static final long serialVersionUID           = -7446418295640581930L;
    private DistinctMethod    dm                         = new DistinctMethod();
    private AntxConfig        ac                         = new AntxConfig();
    private JLabel            pathLabel                  = new JLabel("填写工程目录(用逗号隔开)");
    private JTextField        path                       = new JTextField(30);
    private JButton           genDBButton                = new JButton("生成DB");
    private JButton           cleanDBButton              = new JButton("清空DB");
    private JButton           showDBProjectButton        = new JButton("查看已有工程");
    private JButton           antxCleanButton            = new JButton("antx配置清理");
    private JLabel            projectLabel               = new JLabel("填写需要匹配的工程目录(用逗号隔开)");
    private JTextField        project                    = new JTextField(20);
    private JLabel            classNameNotContainsLabel  = new JLabel("填写类名不包含的字符串(用逗号隔开)");
    private JTextField        classNameNotContains       = new JTextField("Impl", 10);
    private JLabel            methodNameNotContainsLabel = new JLabel("填写方法名不包含的字符串(用逗号隔开)");
    private JTextField        methodNameNotContains      = new JTextField("<init>,do,execute,set,is,get", 20);
    private JButton           button                     = new JButton("扫描");
    private JTextArea         area                       = new JTextArea(30, 68);
    private JScrollPane       jsp                        = new JScrollPane(area);

    public Main(){
        this.setLayout(new FlowLayout());
        this.add(pathLabel);
        this.add(path);
        genDBButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jsp, "是否生成DB");
                if (result == 0) {
                    try {
                        File[] files = FileUtils.convert(path.getText());
                        dm.constructDB(files);
                        JOptionPane.showMessageDialog(jsp, "DB已经生成", "提示", JOptionPane.OK_CANCEL_OPTION);
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(jsp, "[填写工程目录]输入的文件路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        this.add(genDBButton);
        cleanDBButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jsp, "是否清除DB");

                if (result == 0) {
                    // 除了自己有人
                    if (dm.userNum() > 1) {
                        JOptionPane.showMessageDialog(jsp, "有其他使用者，不能清除DB", "提示", JOptionPane.OK_CANCEL_OPTION);
                    } else {
                        dm.cleanDB();
                        JOptionPane.showMessageDialog(jsp, "DB已经清除", "提示", JOptionPane.OK_CANCEL_OPTION);
                    }
                }
            }

        });
        this.add(cleanDBButton);
        showDBProjectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> list = dm.getProjectNames();
                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(jsp, "DB没有数据", "提示", JOptionPane.OK_CANCEL_OPTION);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String item : list) {
                        sb.append(item).append("\n");
                    }

                    sb.append("DB中的文件数：" + dm.getSize());

                    JOptionPane.showMessageDialog(jsp, sb.toString(), "已经添加的工程", JOptionPane.OK_CANCEL_OPTION);
                }
            }

        });
        this.add(showDBProjectButton);
        this.add(projectLabel);
        this.add(project);
        this.add(classNameNotContainsLabel);
        this.add(classNameNotContains);
        this.add(methodNameNotContainsLabel);
        this.add(methodNameNotContains);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (dm.getProjectNames().isEmpty()) {
                    JOptionPane.showMessageDialog(jsp, "先生成DB再进行扫描", "警告", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        File[] files = FileUtils.convert(project.getText());

                        String[] classNames = TextUtils.getInputs(classNameNotContains.getText());
                        String[] methodNames = TextUtils.getInputs(methodNameNotContains.getText());

                        List<MethodRelation> list = dm.constructMethod(files);

                        dm.matchMethod(list);

                        Iterator<MethodRelation> iterator = list.iterator();

                        while (iterator.hasNext()) {
                            MethodRelation mr = iterator.next();
                            if (mr.getCalledTimes() > 0) {
                                iterator.remove();
                            } else {
                                // 针对没有关联的
                                boolean delete = false;
                                if (classNames != null && classNames.length > 0) {
                                    for (String clazz : classNames) {
                                        if (mr.getFilePath().contains(clazz)) {
                                            delete = true;
                                            break;
                                        }
                                    }
                                }

                                if (!delete && methodNames != null && methodNames.length > 0) {
                                    for (String method : methodNames) {
                                        if (mr.getMethodName().contains(method)) {
                                            delete = true;
                                            break;
                                        }
                                    }
                                }

                                if (delete) {
                                    iterator.remove();
                                }
                            }
                        }

                        if (list != null && !list.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            int count = 0;
                            for (MethodRelation mr : list) {
                                sb.append(mr.getFilePath()).append("\t").append(mr.getMethodName()).append("\t").append("代码行数：").append(mr.getCodeLines()).append("\n");
                                count += mr.getCodeLines();
                            }
                            sb.append("共有代码行数：").append(count);

                            area.setText(sb.toString());
                        } else {
                            area.setText("没有检查出来");
                        }

                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(jsp, "[需要匹配的工程目录]输入的文件路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        this.add(button);
        antxCleanButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!NetworkUtils.ping("dzone.alibaba-inc.com", 1999)) {
                    JOptionPane.showMessageDialog(jsp, "无法连接到dzone.alibaba-inc.com", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String defaultAntx = JOptionPane.showInputDialog(jsp, "输入figo应用名称");
                    ac.constructProp(defaultAntx);

                    while (ac.getProp() == null || ac.getProp().isEmpty()) {
                        defaultAntx = JOptionPane.showInputDialog(jsp, "重新输入figo应用名称 eg:trading");
                        ac.constructProp(defaultAntx);
                    }

                    File file = null;
                    String dk = JOptionPane.showInputDialog(jsp, "输入本地工程文件路径");

                    file = new File(dk);

                    while (!file.exists()) {
                        dk = JOptionPane.showInputDialog(jsp, "重新输入本地工程文件路径 eg:/home/weipeng/project/trading");
                        file = new File(dk);
                    }

                    ac.constructDK(file);

                    StringBuilder sb = new StringBuilder();
                    List<String> need = ac.needKV();
                    sb.append("需要的antx配置：\n");
                    for (String r : need) {
                        sb.append(r).append("\n");
                    }
                    sb.append("共" + need.size() + "项。\n");
                    sb.append("==========================================================================\n");
                    List<String> notNeed = ac.doNotNeedKV();
                    sb.append("不需要的antx配置：\n");
                    for (String r : notNeed) {
                        sb.append(r).append("\n");
                    }
                    sb.append("共" + notNeed.size() + "项。");

                    area.setText(sb.toString());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(jsp, "figo应用名称不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                } finally {
                    ac.deleteDK();
                    ac.deleteProp();
                }
            }
        });
        this.add(antxCleanButton);

        jsp.setSize(30, 70);
        area.setAutoscrolls(true);
        area.setEditable(false);
        this.add(jsp);

        this.setSize(800, 600);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Main main = new Main();
        main.setTitle("无用代码扫描工具(CodeName:MOLLY-DISTINCT)");
        main.setVisible(true);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
