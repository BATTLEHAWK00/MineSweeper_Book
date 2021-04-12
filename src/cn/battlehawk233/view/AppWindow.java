package cn.battlehawk233.view;

import cn.battlehawk233.model.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 主窗口
 */
public class AppWindow extends JFrame implements ActionListener {
    private JMenuBar bar;
    private JMenu startMenu, rankingMenu;
    private JMenuItem easyRecordItem, mediumRecordItem, hardRecordItem;
    private JMenuItem easyStartItem, mediumStartItem, hardStartItem, customStartItem;
    private final MineAreaDialog mineArea;
    private final ShowRecordDialog showRecord;

    private void initMenu() {
        //组件初始化
        bar = new JMenuBar();
        startMenu = new JMenu("开始游戏");
        rankingMenu = new JMenu("排行榜");

        easyRecordItem = new JMenuItem("初级英雄榜");
        mediumRecordItem = new JMenuItem("中级英雄榜");
        hardRecordItem = new JMenuItem("高级英雄榜");

        easyStartItem = new JMenuItem("简单游戏");
        mediumStartItem = new JMenuItem("中等游戏");
        hardStartItem = new JMenuItem("困难游戏");
        customStartItem = new JMenuItem("自定义游戏");

        startMenu.add(easyStartItem);
        startMenu.add(mediumStartItem);
        startMenu.add(hardStartItem);
        startMenu.add(customStartItem);

        rankingMenu.add(easyRecordItem);
        rankingMenu.add(mediumRecordItem);
        rankingMenu.add(hardRecordItem);

        bar.add(startMenu);
        bar.add(rankingMenu);
        setJMenuBar(bar);
        easyRecordItem.addActionListener(this);
        mediumRecordItem.addActionListener(this);
        hardRecordItem.addActionListener(this);
        easyStartItem.addActionListener(this);
        mediumStartItem.addActionListener(this);
        hardStartItem.addActionListener(this);
        customStartItem.addActionListener(this);
    }

    public AppWindow() {
        //设置窗口标题
        setTitle("扫雷 Made By 软工1901杨旭龙");
        //初始化菜单
        initMenu();
        //初始化雷区
        mineArea = new MineAreaDialog();
        this.add(mineArea, BorderLayout.CENTER);
        //初始化排行榜窗口
        showRecord = new ShowRecordDialog();
        //初始化主窗口
        setBounds(300, 100, 500, 450);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //处理菜单点击事件
        if (e.getSource() == easyRecordItem) {
            showRecord.showRecord(Difficulty.EASY);
        } else if (e.getSource() == mediumRecordItem) {
            showRecord.showRecord(Difficulty.MEDIUM);
        } else if (e.getSource() == hardRecordItem) {
            showRecord.showRecord(Difficulty.HARD);
        } else if (e.getSource() == easyStartItem) {
            mineArea.initMineArea(Difficulty.EASY);
            validate();
        } else if (e.getSource() == mediumStartItem) {
            mineArea.initMineArea(Difficulty.MEDIUM);
            validate();
        } else if (e.getSource() == hardStartItem) {
            mineArea.initMineArea(Difficulty.HARD);
            validate();
        } else if (e.getSource() == customStartItem) {
            new CustomDiffDialog(mineArea::initMineArea);
        }
    }

    //程序入口
    public static void main(String[] args) {
        new AppWindow();
    }
}
