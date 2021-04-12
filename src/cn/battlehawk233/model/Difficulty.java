package cn.battlehawk233.model;

/**
 * 将难度传参重构为难度枚举类型
 * 增加代码重用性
 */
public enum Difficulty implements IDifficulty {
    EASY(9, 9, 10),
    MEDIUM(16, 16, 40),
    HARD(22, 30, 99);

    private final int row;
    private final int column;
    private final int mineCount;

    Difficulty(int row, int column, int mineCount) {
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

    @Override
    public String getName() {
        return name();
    }

    public int getRow() {
        return row;
    }
}
