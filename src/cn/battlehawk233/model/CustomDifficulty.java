package cn.battlehawk233.model;

/**
 * 自定义难度类
 */
public class CustomDifficulty {
    private final int row;
    private final int column;
    private final int mineCount;

    public CustomDifficulty(int row, int column, int mineCount) {
        this.row = row;
        this.column = column;
        this.mineCount = mineCount;
    }

    public int getColumn() {
        return column;
    }

    public int getMineCount() {
        return mineCount;
    }

    public int getRow() {
        return row;
    }
}
