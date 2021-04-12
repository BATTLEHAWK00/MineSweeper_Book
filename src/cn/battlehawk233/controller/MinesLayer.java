package cn.battlehawk233.controller;

import cn.battlehawk233.model.Block;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MinesLayer {
    private static final MinesLayer layMines = new MinesLayer();

    public static MinesLayer getInstance() {
        return layMines;
    }

    private MinesLayer() {
    }

    public void InitBlock(Block[][] block) {
        //初始化所有单元格
        for (Block[] blocks : block) {
            for (Block value : blocks) {
                value.setMine(false);
            }
        }
    }

    public void layMines(Block[][] block, int mineCount) {
        //随机布雷
        InitBlock(block);
        int row = block.length;
        int column = block[0].length;
        List<Block> list = new LinkedList<>();
        for (Block[] blocks : block) {
            list.addAll(Arrays.asList(blocks).subList(0, column));
        }
        while (mineCount > 0) {
            int size = list.size();
            int randomIndex = (int) (Math.random() * size);
            Block b = list.get(randomIndex);
            b.setMine(true);
            list.remove(randomIndex);
            mineCount--;
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (block[i][j].isMine()) {
                    block[i][j].setOpen(false);
                    block[i][j].setMark(false);
                } else {
                    int mineNumber = 0;
                    for (int k = Math.max(i - 1, 0); k <= Math.min(i + 1, row - 1); k++) {
                        for (int t = Math.max(j - 1, 0); t <= Math.min(j + 1, column - 1); t++) {
                            if (block[k][t].isMine())
                                mineNumber++;
                        }
                    }
                    block[i][j].setOpen(false);
                    block[i][j].setMark(false);
                    block[i][j].setAroundMineNumber(mineNumber);
                }
            }
        }
    }
}
