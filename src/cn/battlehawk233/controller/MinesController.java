package cn.battlehawk233.controller;

import cn.battlehawk233.model.Block;
import cn.battlehawk233.model.CustomDifficulty;
import cn.battlehawk233.model.Difficulty;
import cn.battlehawk233.view.RecordWritingWindow;
import cn.battlehawk233.view.ViewForMineArea;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;


/**
 * 雷区全局管理类
 */
public class MinesController implements IMinesController {
    private Difficulty difficulty;
    private int row, column, mineCount;
    private int markCount = 0;
    private int timeElapsed = 0;
    private RecordWritingWindow record = new RecordWritingWindow();
    private List<Block> blockList = new ArrayList<>();
    private Block[][] blocks;
    private List<Consumer<Object>> timerEvt = new ArrayList<>();
    private ViewForMineArea mineArea;
    private Timer timer = new Timer(1000, (evt) -> {
        for (Consumer<Object> i : timerEvt)
            i.accept(evt);
    });
    private boolean isStarted = false;

    //初始化雷区
    public MinesController(Difficulty difficulty, ViewForMineArea mineArea) {
        this.difficulty = difficulty;
        this.mineArea = mineArea;
        if (difficulty != Difficulty.CUSTOM) {
            this.row = difficulty.getRow();
            this.column = difficulty.getColumn();
            this.mineCount = difficulty.getMineCount();
        }
        initController();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getMineCount() {
        return mineCount;
    }

    public MinesController(CustomDifficulty difficulty, ViewForMineArea mineArea) {
        this.row = difficulty.getRow();
        this.column = difficulty.getColumn();
        this.mineCount = difficulty.getMineCount();
        this.difficulty = Difficulty.CUSTOM;
        this.mineArea = mineArea;
        initController();
    }

    private void initController() {
        blocks = new Block[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                Block block = new Block(i, j, this);
                blockList.add(block);
                blocks[i][j] = block;
            }
        }
        LayMines.getInstance().layMines(blocks, mineCount);
        mineArea.initMines(blocks);
        mineArea.updateMarkCount(0);
        mineArea.updateTimer(0);
        RegisterTimer(obj -> timeElapsed++);
        RegisterTimer(obj -> mineArea.updateTimer(timeElapsed));
    }

    //注册时钟事件
    public void RegisterTimer(Consumer<Object> action) {
        timerEvt.add(action);
    }

    //连锁反应
    public List<Block> getNonMineBlocksAround(Block block) {
        Block[][] blocks = new Block[row][column];
        List<Block> list = new ArrayList<>();
        for (Block i : this.blockList) {
            blocks[i.getX()][i.getY()] = i;
        }
        //BFS搜索临近单元
        Queue<Block> q = new LinkedList<>();
        q.add(block);
        while (!q.isEmpty()) {
            Block b = q.remove();
            list.add(b);
            int x = b.getX();
            int y = b.getY();
            int[] Xs = {-1, 0, 1, 0, -1, 1, -1, 1};
            int[] Ys = {0, 1, 0, -1, -1, 1, 1, -1};
            for (int i = 0; i < Xs.length; i++) {
                int px = x + Xs[i], py = y + Ys[i];
                if (px < 0 || px >= row)
                    continue;
                if (py < 0 || py >= column)
                    continue;
                if (!blocks[px][py].isMine() && !blocks[px][py].isMark() && !list.contains(blocks[px][py]))
                    if (blocks[px][py].getAroundMineNumber() != 0)
                        list.add(blocks[px][py]);
                    else {
                        list.add(blocks[px][py]);
                        q.add(blocks[px][py]);
                    }
            }
        }
        return list;
    }

    //处理挖雷事件
    @Override
    public void onMineScout(Block block) {
        if (block.isMark())
            return;
        if (!isStarted) {
            timer.start();
            isStarted = true;
        }
        if (block.isMine()) {
            GameEnd();
        } else {
            if (block.getAroundMineNumber() == 0) {
                List<Block> list = getNonMineBlocksAround(block);
                for (Block i : list) {
                    i.getBlockView().seeBlockNameOrIcon();
                    i.setOpen(true);
                }
            } else {
                block.getBlockView().seeBlockNameOrIcon();
                block.setOpen(true);
            }
        }
        verifyWin();
    }

    //处理游戏结束
    public void GameEnd() {
        timer.stop();
        isStarted = false;
        timeElapsed = 0;
        mineArea.updateTimer(0);
        markCount = 0;
        mineArea.updateMarkCount(markCount);
        JOptionPane.showMessageDialog((JPanel) mineArea, "Boom!!!\n你输了!");
        for (Block i : blockList) {
            i.getBlockView().seeBlockNameOrIcon();
        }
    }

    //处理单元格标记
    @Override
    public void onMineMark(Block block) {
        if (block.isMark()) {
            markCount--;
            block.setMark(false);
        } else {
            if (markCount == getMineCount())
                return;
            markCount++;
            block.setMark(true);
        }
        mineArea.updateMarkCount(markCount);
        block.getBlockView().seeBlockMark();
        verifyWin();
    }

    //处理析构对象
    @Override
    public void Dispose() {
        timer.stop();
        for (ActionListener actionListener : timer.getActionListeners()) {
            timer.removeActionListener(actionListener);
        }
    }

    //重置雷区和计时
    @Override
    public void Reset() {
        timer.stop();
        isStarted = false;
        timeElapsed = 0;
        mineArea.updateTimer(0);
        markCount = 0;
        mineArea.updateMarkCount(markCount);
        for (Block i : blockList) {
            i.setOpen(false);
            i.setMark(false);
            i.getBlockView().seeBlockCover();
            i.getBlockView().seeBlockMark();
        }
    }

    //检测玩家是否赢得该场游戏
    @Override
    public void verifyWin() {
        boolean isOk = true;

        int number = 0, total = column * row - mineCount;
        for (Block i : blockList)
            if (i.isOpen()) {
                number++;
            }
        if (number != total)
            isOk = false;

        if (isOk) {
            System.out.println("You Win!");
            timer.stop();
            JOptionPane.showMessageDialog((JPanel) mineArea, "你赢了!耗时:" + timeElapsed + "秒");
            if (difficulty != Difficulty.CUSTOM) {
                record.setDifficulty(difficulty);
                record.setTime(timeElapsed);
                record.setVisible(true);
            }
        }
    }
}
