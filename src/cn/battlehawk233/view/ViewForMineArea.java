package cn.battlehawk233.view;

import cn.battlehawk233.model.Block;

public interface ViewForMineArea {
    void updateTimer(int time);

    void updateMarkCount(int cnt);

    void initMines(Block[][] blocks);
}