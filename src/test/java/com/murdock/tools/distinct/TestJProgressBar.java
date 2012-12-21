package com.murdock.tools.distinct;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import org.junit.Ignore;

@Ignore
public class TestJProgressBar {

    JFrame       frame         = new JFrame("测试进度条");
    // 创建一条垂直进度条
    JProgressBar bar           = new JProgressBar(JProgressBar.VERTICAL);
    JCheckBox    indeterminate = new JCheckBox("不确定进度");
    JCheckBox    noBorder      = new JCheckBox("不绘制边框");

    public void init() {
        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(indeterminate);
        box.add(noBorder);
        frame.setLayout(new FlowLayout());
        frame.add(box);
        // 把进度条添加到JFrame窗口中
        frame.add(bar);

        // 设置在进度条中绘制完成百分比
        bar.setStringPainted(true);
        noBorder.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                // 根据该选择框决定是否绘制进度条的边框
                bar.setBorderPainted(!noBorder.isSelected());
            }
        });
        final SimulatedTarget target = new SimulatedTarget(1000);
        // 以启动一条线程的方式来执行一个耗时的任务
        new Thread(target).start();
        // 设置进度条的最大值和最小值,
        bar.setMinimum(0);
        // 以总任务量作为进度条的最大值
        bar.setMaximum(target.getAmount());
        Timer timer = new Timer(300, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // 以任务的当前完成量设置进度条的value
                bar.setValue(target.getCurrent());
            }
        });
        timer.start();
        indeterminate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                // 设置该进度条的进度是否确定
                bar.setIndeterminate(indeterminate.isSelected());
                bar.setStringPainted(!indeterminate.isSelected());
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new TestJProgressBar().init();
    }
}

// 模拟一个耗时的任务
class SimulatedTarget implements Runnable {

    // 任务的当前完成量
    private volatile int current;
    // 总任务量
    private int          amount;

    public SimulatedTarget(int amount){
        current = 0;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getCurrent() {
        return current;
    }

    // run方法代表不断完成任务的过程
    public void run() {

        while (current < amount) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {

            }
            current++;
        }
    }
}
