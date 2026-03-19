package com.ruralwater.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 数据库连接工具类（原生 JDBC）
 * 支持配置文件加载和连接池管理
 * 增强版：添加日志记录和错误处理
 */
public class DBUtil {
    
    // 数据库配置（从配置文件读取）
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String DRIVER;
    
    // 连接池配置（可选）
    private static int INITIAL_SIZE = 5;
    private static int MAX_ACTIVE = 20;
    private static int MIN_IDLE = 2;
    private static long MAX_WAIT = 10000;
    
    static {
        loadConfig();
        try {
            // 加载 MySQL 驱动
            Class.forName(DRIVER != null ? DRIVER : "com.mysql.jdbc.Driver");
            SimpleLogger.info("数据库驱动加载成功！");
        } catch (ClassNotFoundException e) {
            SimpleLogger.error("MySQL 驱动加载失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 从配置文件加载数据库配置
     */
    private static void loadConfig() {
        try {
            Properties props = new Properties();
            InputStream input = DBUtil.class.getClassLoader()
                .getResourceAsStream("com/ruralwater/config/database.properties");
            
            if (input == null) {
                // 如果类路径下没有，尝试从 config 目录加载
                input = new java.io.FileInputStream("config/database.properties");
            }
            
            if (input != null) {
                props.load(input);
                URL = props.getProperty("db.url");
                USERNAME = props.getProperty("db.username");
                PASSWORD = props.getProperty("db.password");
                DRIVER = props.getProperty("db.driver");
                
                // 加载连接池配置
                try {
                    INITIAL_SIZE = Integer.parseInt(props.getProperty("db.initialSize", "5"));
                    MAX_ACTIVE = Integer.parseInt(props.getProperty("db.maxActive", "20"));
                    MIN_IDLE = Integer.parseInt(props.getProperty("db.minIdle", "2"));
                    MAX_WAIT = Long.parseLong(props.getProperty("db.maxWait", "10000"));
                } catch (NumberFormatException e) {
                    // 使用默认值
                }
                
                input.close();
                SimpleLogger.debug("数据库配置加载成功！URL: " + URL);
            } else {
                // 使用默认配置
                useDefaultConfig();
            }
        } catch (Exception e) {
            SimpleLogger.warn("配置文件加载失败，使用默认配置：" + e.getMessage());
            useDefaultConfig();
        }
    }
    
    /**
     * 使用默认配置
     */
    private static void useDefaultConfig() {
        URL = "jdbc:mysql://localhost:3306/rural_water_db?useSSL=false&characterEncoding=utf8";
        USERNAME = "root";
        PASSWORD = "";  // 空密码
        DRIVER = "com.mysql.jdbc.Driver";
    }
    
    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            SimpleLogger.debug("数据库连接创建成功");
            return conn;
        } catch (SQLException e) {
            SimpleLogger.error("数据库连接失败：" + e.getMessage() + "\nURL: " + URL + "\nUsername: " + USERNAME);
            throw e;
        }
    }
    
    /**
     * 测试数据库连接
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                DatabaseMetaData metaData = conn.getMetaData();
                SimpleLogger.info("数据库产品：" + metaData.getDatabaseProductName() + 
                                 " 版本：" + metaData.getDatabaseProductVersion());
                close(conn, null);
                return true;
            }
            return false;
        } catch (Exception e) {
            SimpleLogger.error("数据库连接测试失败：" + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 关闭资源
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 关闭资源（无 ResultSet）
     */
    public static void close(Connection conn, Statement stmt) {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 开启事务
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
        }
    }
    
    /**
     * 提交事务
     */
    public static void commit(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
        }
    }
    
    /**
     * 回滚事务
     */
    public static void rollback(Connection conn) throws SQLException {
        if (conn != null) {
            conn.rollback();
        }
    }
}
