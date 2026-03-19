package com.ruralwater.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC 工具助手类（简化资源操作）
 * 提供自动化的资源管理和简化的 JDBC 操作方法
 */
public class JdbcHelper {
    
    /**
     * 函数式接口：用于执行带参数的 SQL 操作
     */
    @FunctionalInterface
    public interface SqlExecutor<T> {
        T execute(PreparedStatement pstmt) throws SQLException;
    }
    
    /**
     * 函数式接口：用于处理 ResultSet
     */
    @FunctionalInterface
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }
    
    /**
     * 执行更新操作（INSERT, UPDATE, DELETE）
     * @param sql SQL 语句
     * @param params 参数列表
     * @return 影响的行数
     */
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        return execute(sql, pstmt -> {
            setParams(pstmt, params);
            return pstmt.executeUpdate();
        });
    }
    
    /**
     * 执行查询操作
     * @param <T> 返回类型
     * @param sql SQL 语句
     * @param handler ResultSet 处理器
     * @param params 参数列表
     * @return 查询结果
     */
    public static <T> T executeQuery(String sql, ResultSetHandler<T> handler, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            setParams(pstmt, params);
            rs = pstmt.executeQuery();
            return handler.handle(rs);
        } finally {
            close(rs, pstmt, conn);
        }
    }
    
    /**
     * 通用执行方法
     * @param <T> 返回类型
     * @param executor SQL 执行器
     * @return 执行结果
     */
    private static <T> T execute(String sql, SqlExecutor<T> executor, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            setParams(pstmt, params);
            return executor.execute(pstmt);
        } finally {
            close(null, pstmt, null);
        }
    }
    
    /**
     * 设置 PreparedStatement 参数
     */
    private static void setParams(PreparedStatement pstmt, Object[] params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }
    }
    
    /**
     * 关闭资源（静态工具方法）
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
