package cn.battlehawk233.view;

import cn.battlehawk233.controller.MinesControllerImpl;
import cn.battlehawk233.model.Block;
import cn.battlehawk233.model.IDifficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MineAreaDialog extends JPanel implements ActionListener, ViewForMineArea {
    private final JButton reStart;
    private MinesControllerImpl minesController;
    private final JPanel pCenter;
    private final JPanel pNorth;
    private final JTextField showTime;
    private final JTextField showMarkedMineCount;
    private final JProgressBar progressBar;
    private final RecordWritingDialog record;
    private int row, column, mineCount;

    public MineAreaDialog() {
        //组件初始化
        record = new RecordWritingDialog();
        reStart = new JButton("重新开始");
        showTime = new JTextField(5);
        showMarkedMineCount = new JTextField(5);
        showTime.setHorizontalAlignment(JTextField.CENTER);
        showMarkedMineCount.setHorizontalAlignment(JTextField.CENTER);
        showMarkedMineCount.setFont(new Font("Arial", Font.BOLD, 16));
        showTime.setFont(new Font("Arial", Font.BOLD, 16));
        pCenter = new JPanel();
        pNorth = new JPanel();
        progressBar = new JProgressBar();
        reStart.addActionListener(this);
        pNorth.add(new JLabel("剩余标记:"));
        pNorth.add(showMarkedMineCount);
        pNorth.add(reStart);
        pNorth.add(new JLabel("用时:"));
        pNorth.add(showTime);
        pNorth.add(progressBar);
        setLayout(new BorderLayout());
        add(pNorth, BorderLayout.NORTH);
        add(pCenter, BorderLayout.CENTER);
    }

    public void initMineArea(IDifficulty difficulty) {
        this.row = difficulty.getRow();
        this.column = difficulty.getColumn();
        this.mineCount = difficulty.getMineCount();
        initMineArea();
        pCenter.setLayout(new GridLayout(row, column));
        progressBar.setMaximum(row * column - mineCount);
        minesController = new MinesControllerImpl(difficulty, this);
        repaint();
    }

    private void initMineArea() {
        pCenter.removeAll();
        progressBar.setValue(0);
        progressBar.setMinimum(0);
        if (minesController != null)
            minesController.Dispose();
        minesController = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reStart) {
            if (minesController == null)
                return;
            minesController.Reset();
            repaint();
            validate();
        }
    }

    @Override
    public void updateTimer(int time) {
        showTime.setText("" + time);
    }

    @Override
    public void updateMarkCount(int cnt) {
        showMarkedMineCount.setText("" + (mineCount - cnt));
    }

    @Override
    public void updateProgress(int cnt) {
        progressBar.setValue(cnt);
    }

    @Override
    public void initMines(Block[][] blocks) {
        for (Block[] i : blocks) {
            for (Block j : i) {
                BlockViewPanel view = new BlockViewPanel();
                view.acceptBlock(j);
                j.setBlockView(view);
                view.setDataOnView();
                view.seeBlockCover();
                pCenter.add(view);
            }
        }
        validate();
    }
}
