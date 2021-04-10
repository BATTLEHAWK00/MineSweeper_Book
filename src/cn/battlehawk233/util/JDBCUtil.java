package cn.battlehawk233.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * 封装JDBC基础连接
 */
public class JDBCUtil {
    private static final JDBCUtil instance = new JDBCUtil();
    private final String JDBC_URL;
    private final String USERNAME;
    private final String PASSWD;

    //单例模式
    public static JDBCUtil getInstance() {
        return instance;
    }

    //获取数据库连接
    public Connection GetConn() {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWD);
            return connection;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //JDBC初始化
    public JDBCUtil() {
        Properties properties = new Properties();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            properties.load(JDBCUtil.class.getResourceAsStream("jdbcsettings.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBC_URL = (String) properties.get("URL");
        USERNAME = (String) properties.get("Username");
        PASSWD = (String) properties.get("Passwd");
    }

    //封装数据库连接
    public void UseConn(Consumer<Connection> callback) {
        Connection conn = GetConn();
        callback.accept(conn);
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
