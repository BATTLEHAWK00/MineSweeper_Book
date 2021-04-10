package cn.battlehawk233.controller;

import cn.battlehawk233.model.Block;

public interface IMinesController {
    void onMineScout(Block block);

    void onMineMark(Block block);

    void Dispose();

    void Reset();

    void verifyWin();
}
