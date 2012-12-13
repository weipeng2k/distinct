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
    private JLabel            pathLabel                  = new JLabel("��д����Ŀ¼(�ö��Ÿ���)");
    private JTextField        path                       = new JTextField(30);
    private JButton           genDBButton                = new JButton("����DB");
    private JButton           cleanDBButton              = new JButton("���DB");
    private JButton           showDBProjectButton        = new JButton("�鿴���й���");
    private JButton           antxCleanButton            = new JButton("antx��������");
    private JLabel            projectLabel               = new JLabel("��д��Ҫƥ��Ĺ���Ŀ¼(�ö��Ÿ���)");
    private JTextField        project                    = new JTextField(20);
    private JLabel            classNameNotContainsLabel  = new JLabel("��д�������������ַ���(�ö��Ÿ���)");
    private JTextField        classNameNotContains       = new JTextField("Impl", 10);
    private JLabel            methodNameNotContainsLabel = new JLabel("��д���������������ַ���(�ö��Ÿ���)");
    private JTextField        methodNameNotContains      = new JTextField("<init>,do,execute,set,is,get", 20);
    private JButton           button                     = new JButton("ɨ��");
    private JTextArea         area                       = new JTextArea(30, 68);
    private JScrollPane       jsp                        = new JScrollPane(area);

    public Main(){
        this.setLayout(new FlowLayout());
        this.add(pathLabel);
        this.add(path);
        genDBButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jsp, "�Ƿ�����DB");
                if (result == 0) {
                    try {
                        File[] files = FileUtils.convert(path.getText());
                        dm.constructDB(files);
                        JOptionPane.showMessageDialog(jsp, "DB�Ѿ�����", "��ʾ", JOptionPane.OK_CANCEL_OPTION);
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(jsp, "[��д����Ŀ¼]������ļ�·��������", "����", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        this.add(genDBButton);
        cleanDBButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jsp, "�Ƿ����DB");

                if (result == 0) {
                    // �����Լ�����
                    if (dm.userNum() > 1) {
                        JOptionPane.showMessageDialog(jsp, "������ʹ���ߣ��������DB", "��ʾ", JOptionPane.OK_CANCEL_OPTION);
                    } else {
                        dm.cleanDB();
                        JOptionPane.showMessageDialog(jsp, "DB�Ѿ����", "��ʾ", JOptionPane.OK_CANCEL_OPTION);
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
                    JOptionPane.showMessageDialog(jsp, "DBû������", "��ʾ", JOptionPane.OK_CANCEL_OPTION);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String item : list) {
                        sb.append(item).append("\n");
                    }

                    sb.append("DB�е��ļ�����" + dm.getSize());

                    JOptionPane.showMessageDialog(jsp, sb.toString(), "�Ѿ����ӵĹ���", JOptionPane.OK_CANCEL_OPTION);
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
                    JOptionPane.showMessageDialog(jsp, "������DB�ٽ���ɨ��", "����", JOptionPane.WARNING_MESSAGE);
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
                                // ���û�й�����
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
                                sb.append(mr.getFilePath()).append("\t").append(mr.getMethodName()).append("\t").append("����������").append(mr.getCodeLines()).append("\n");
                                count += mr.getCodeLines();
                            }
                            sb.append("���д���������").append(count);

                            area.setText(sb.toString());
                        } else {
                            area.setText("û�м�����");
                        }

                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(jsp, "[��Ҫƥ��Ĺ���Ŀ¼]������ļ�·��������", "����", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        this.add(button);
        antxCleanButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!NetworkUtils.ping("dzone.alibaba-inc.com", 1999)) {
                    JOptionPane.showMessageDialog(jsp, "�޷����ӵ�dzone.alibaba-inc.com", "����", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String defaultAntx = JOptionPane.showInputDialog(jsp, "����figoӦ������");
                    ac.constructProp(defaultAntx);

                    while (ac.getProp() == null || ac.getProp().isEmpty()) {
                        defaultAntx = JOptionPane.showInputDialog(jsp, "��������figoӦ������ eg:trading");
                        ac.constructProp(defaultAntx);
                    }

                    File file = null;
                    String dk = JOptionPane.showInputDialog(jsp, "���뱾�ع����ļ�·��");

                    file = new File(dk);

                    while (!file.exists()) {
                        dk = JOptionPane.showInputDialog(jsp, "�������뱾�ع����ļ�·�� eg:/home/weipeng/project/trading");
                        file = new File(dk);
                    }

                    ac.constructDK(file);

                    StringBuilder sb = new StringBuilder();
                    List<String> need = ac.needKV();
                    sb.append("��Ҫ��antx���ã�\n");
                    for (String r : need) {
                        sb.append(r).append("\n");
                    }
                    sb.append("��" + need.size() + "�\n");
                    sb.append("==========================================================================\n");
                    List<String> notNeed = ac.doNotNeedKV();
                    sb.append("����Ҫ��antx���ã�\n");
                    for (String r : notNeed) {
                        sb.append(r).append("\n");
                    }
                    sb.append("��" + notNeed.size() + "�");

                    area.setText(sb.toString());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(jsp, "figoӦ�����Ʋ�����", "����", JOptionPane.ERROR_MESSAGE);
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
        main.setTitle("���ô���ɨ�蹤��(CodeName:MOLLY-DISTINCT)");
        main.setVisible(true);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}