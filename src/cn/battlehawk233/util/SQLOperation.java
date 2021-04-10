package cn.battlehawk233.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 封装JDBC功能，增强代码重用性
 */
public class SQLOperation {
    private String SQL;
    private PreparedStatement statement;

    //执行数据库查询
    public List<Map<String, Object>> ExecuteQuery() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        JDBCUtil.getInstance().UseConn(connection -> {
            try {
                statement = connection.prepareStatement(SQL);
                ResultSet resultSet = statement.executeQuery();
                ResultSetMetaData meta = resultSet.getMetaData();
                while (resultSet.next()) {
                    Map<String, Object> mp = new HashMap<>();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        mp.put(meta.getColumnName(i), resultSet.getObject(i));
                    }
                    mapList.add(mp);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        return mapList;
    }

    //执行数据库更新
    public void ExecuteUpdate() {
        JDBCUtil.getInstance().UseConn(connection -> {
            try {
                statement = connection.prepareStatement(SQL);
                statement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void setSQL(String SQL) {
        this.SQL = SQL;
    }
}
