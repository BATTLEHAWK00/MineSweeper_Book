package cn.battlehawk233.controller;

import cn.battlehawk233.model.Block;
import cn.battlehawk233.model.Difficulty;
import cn.battlehawk233.model.IDifficulty;
import cn.battlehawk233.util.AudioUtil;
import cn.battlehawk233.view.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.function.Consumer;


/**
 * 雷区全局控制类
 * Controller
 */
public class MinesControllerImpl implements MinesController {
    private IDifficulty difficulty;
    private int row, column, mineCount;
    private int markCount = 0;
    private int openCount = 0;
    private int timeElapsed = 0;
    private RecordWritingDialog record = new RecordWritingDialog();
    private List<Block> blockList = new ArrayList<>();
    private Block[][] blocks;
    private List<Consumer<Object>> timerEvt = new ArrayList<>();
    private ViewForMineArea mineArea;
    private Timer timer = new Timer(1000, (evt) -> {
        for (Consumer<Object> i : timerEvt)
            i.accept(evt);
    });
    private boolean isStarted = false;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getMineCount() {
        return mineCount;
    }

    public MinesControllerImpl(IDifficulty difficulty, ViewForMineArea mineArea) {
        this.row = difficulty.getRow();
        this.column = difficulty.getColumn();
        this.mineCount = difficulty.getMineCount();
        this.difficulty = difficulty;
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
        MinesLayer.getInstance().layMines(blocks, mineCount);
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
    public Set<Block> getNonMineBlocksAround(Block block) {
        //BFS初始化
        Set<Block> list = new HashSet<>();
        Queue<Block> q = new LinkedList<>();
        q.add(block);
        list.add(block);
        while (!q.isEmpty()) {
            //取出队列元素
            Block current = q.remove();
            list.add(current);
            int x = current.getX();
            int y = current.getY();
            //定义方向向量
            int[] Xs = {-1, 0, 1, 0, -1, 1, -1, 1};
            int[] Ys = {0, 1, 0, -1, -1, 1, 1, -1};
            for (int i = 0; i < Xs.length; i++) {
                //边界处理
                int px = x + Xs[i], py = y + Ys[i];
                if (px < 0 || px >= row)
                    continue;
                if (py < 0 || py >= column)
                    continue;
                //遍历周围单元
                Block b = blocks[px][py];
                if (!b.isMine() && !b.isMark() && !list.contains(b) && !b.isOpen()) {
                    if (b.getAroundMineNumber() == 0)
                        q.add(b);
                    list.add(b);
                }
            }
        }
        return list;
    }

    //处理挖雷事件
    @Override
    public void onMineScout(Block block) {
        //无效条件处理(标记方块不能被点击)
        if (block.isMark())
            return;
        //若第一个方块被点击，则开始计时
        if (!isStarted) {
            timer.start();
            isStarted = true;
        }
        if (block.isMine()) {
            //玩家遇雷时的处理
            AudioUtil.getInstance().PlaySound(BlockViewPanel.mineSound);
            JOptionPane.showMessageDialog((JPanel) mineArea, "Boom!!!\n你输了!");
            GameEnd();
        } else {
            //玩家未遇雷时的处理
            AudioUtil.getInstance().PlaySound(BlockViewPanel.normalSound);
            Set<Block> list = new HashSet<Block>() {{
                add(block);
            }};
            //若该单元没有雷，则触发连锁反应
            if (block.getAroundMineNumber() == 0) {
                list = getNonMineBlocksAround(block);
            }
            for (Block i : list) {
                i.getBlockView().seeBlockNameOrIcon();
                i.setOpen(true);
                openCount++;
            }
            //视图层进度条更新
            mineArea.updateProgress(openCount);
        }
        //判断游戏是否胜利
        verifyWin();
    }

    //处理游戏结束
    public void GameEnd() {
        //重置数据及视图层
        timer.stop();
        isStarted = false;
        timeElapsed = 0;
        mineArea.updateTimer(0);
        markCount = 0;
        openCount = 0;
        mineArea.updateMarkCount(markCount);
        mineArea.updateProgress(0);
        for (Block i : blockList) {
            i.getBlockView().seeBlockNameOrIcon();
        }
    }

    //处理单元格标记
    @Override
    public void onMineMark(Block block) {
        if (block.isMark()) {
            //取消标记
            markCount--;
            block.setMark(false);
        } else {
            //打上标记
            if (markCount == getMineCount())
                return;
            AudioUtil.getInstance().PlaySound(BlockViewPanel.markSound);
            markCount++;
            block.setMark(true);
        }
        //更新视图层
        mineArea.updateMarkCount(markCount);
        block.getBlockView().seeBlockMark();
    }

    //处理析构对象
    @Override
    public void Dispose() {
        //移除计时器事件
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
        openCount = 0;
        mineArea.updateMarkCount(markCount);
        mineArea.updateProgress(0);
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
        if (openCount == column * row - mineCount) {
            //处理游戏胜利
            System.out.println("You Win!");
            timer.stop();
            JOptionPane.showMessageDialog((JPanel) mineArea, "你赢了!耗时:" + timeElapsed + "秒");
            if (!difficulty.getName().equals("CUSTOM")) {
                //弹出成绩记录窗口
                record.setDifficulty(difficulty);
                record.setTime(timeElapsed);
                record.setVisible(true);
                //询问是否进行下一关
                new NextLevelDialog((obj) -> {
                    if (difficulty == Difficulty.EASY)
                        ((MineAreaDialog) mineArea).initMineArea(Difficulty.MEDIUM);
                    else if (difficulty == Difficulty.MEDIUM)
                        ((MineAreaDialog) mineArea).initMineArea(Difficulty.HARD);
                });
            }
            GameEnd();
        }
    }
}
