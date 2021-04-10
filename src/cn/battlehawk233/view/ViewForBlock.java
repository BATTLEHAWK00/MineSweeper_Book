package cn.battlehawk233.view;

import cn.battlehawk233.model.Block;

public interface ViewForBlock {
    void acceptBlock(Block block);

    void setDataOnView();

    void seeBlockNameOrIcon();

    void seeBlockCover();

    void seeBlockMark();

    Object getBlockCover();
}
