package cn.battlehawk233.dao;


import cn.battlehawk233.model.Difficulty;
import cn.battlehawk233.util.JDBCUtil;
import cn.battlehawk233.util.SQLOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 原类名:RecordOrShowRecord
 * 改为:RecordDAO 规范化命名
 * 数据库通信类
 * 根据实验书重构
 */
public class RecordDAO {
    private static final RecordDAO instance = new RecordDAO();
    private final String TABLE_NAME = "t_minesweeper";
    private final int HERO_NUMBER = 3;

    //单例模式
    public static RecordDAO getInstance() {
        return instance;
    }

    public RecordDAO() {
        Connection conn = JDBCUtil.getInstance().GetConn();
        String sql = String.format("CREATE TABLE %s (p_name varchar(50),p_time int,p_diff varchar(10))", TABLE_NAME);
        PreparedStatement sta = null;
        try {
            sta = conn.prepareStatement(sql);
            sta.executeUpdate();
            conn.close();
        } catch (Exception ignored) {

        }
    }

    private int verifyScore(int time, Difficulty difficulty) {
        AtomicInteger amount = new AtomicInteger();
        JDBCUtil.getInstance().UseConn(connection -> {
            SQLOperation operation = new SQLOperation();
            operation.setSQL(String.format("SELECT * FROM %s WHERE p_time < %d and p_diff = '%s'", TABLE_NAME, time, difficulty.name()));
            List<Map<String, Object>> dict = operation.ExecuteQuery();
            amount.set(dict.size());
        });
        return amount.get();
    }

    public boolean addRecord(String name, int time, Difficulty difficulty) {
        AtomicBoolean ok = new AtomicBoolean(false);
        int amount = verifyScore(time, difficulty);
        if (amount >= HERO_NUMBER) {
            ok.set(false);
        } else {
            JDBCUtil.getInstance().UseConn(connection -> {
                String sql = String.format("INSERT INTO %s VALUES ('%s',%d,'%s')", TABLE_NAME, name, time, difficulty.name());
                SQLOperation operation = new SQLOperation();
                operation.setSQL(sql);
                operation.ExecuteUpdate();
                ok.set(true);
            });
        }
        return ok.get();
    }

    public List<Map<String, Object>> queryRecord(Difficulty difficulty) {
        AtomicReference<List<Map<String, Object>>> record = new AtomicReference<>();
        JDBCUtil.getInstance().UseConn(connection -> {
            String sql = String.format("SELECT * FROM %s WHERE p_diff='%s' ORDER BY p_time", TABLE_NAME, difficulty.name());
            SQLOperation operation = new SQLOperation();
            operation.setSQL(sql);
            record.set(operation.ExecuteQuery());
        });
        return record.get();
    }
}
