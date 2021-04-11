package cn.battlehawk233.model;

import cn.battlehawk233.controller.MinesControllerImpl;
import cn.battlehawk233.view.ViewForBlock;

/**
 * 雷区单元格Bean
 */
public class Block {
    private String name;
    private int aroundMineNumber;
    private int x;
    private int y;
    private boolean isMine = false;
    private boolean isMark = false;
    private boolean isOpen = false;
    private ViewForBlock blockView;
    private MinesControllerImpl controller;

    public Block(int x, int y, MinesControllerImpl controller) {
        this.x = x;
        this.y = y;
        this.controller = controller;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setAroundMineNumber(int aroundMineNumber) {
        this.aroundMineNumber = aroundMineNumber;
    }

    public void setBlockView(ViewForBlock blockView) {
        this.blockView = blockView;
    }

    public void setMark(boolean mark) {
        isMark = mark;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isMark() {
        return isMark;
    }

    public int getAroundMineNumber() {
        return aroundMineNumber;
    }

    public String getName() {
        return name;
    }

    public ViewForBlock getBlockView() {
        return blockView;
    }

    public MinesControllerImpl getController() {
        return controller;
    }
}
