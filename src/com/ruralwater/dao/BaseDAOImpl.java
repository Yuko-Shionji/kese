package com.ruralwater.dao;

import com.ruralwater.util.DBUtil;
import com.ruralwater.util.JdbcHelper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用 DAO 基类（模板方法模式）
 * 封装通用的 CRUD 操作，减少代码重复
 * @param <T> 实体类型
 */
public abstract class BaseDAOImpl<T> implements BaseDAO<T> {
    
    /**
     * 获取表名
     * @return 表名
     */
    protected abstract String getTableName();
    
    /**
     * 从 ResultSet 创建实体对象
     * @param rs ResultSet
     * @return 实体对象
     * @throws SQLException SQL 异常
     */
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    
    /**
     * 获取主键列名
     * @return 主键列名
     */
    protected String getPrimaryKeyColumn() {
        return "id";
    }
    
    /**
     * 根据 ID 查询实体
     * @param id ID
     * @return 实体对象，不存在返回 null
     * @throws Exception 异常
     */
    @Override
    public T findById(Integer id) throws Exception {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getPrimaryKeyColumn() + " = ?";
        
        return JdbcHelper.executeQuery(sql, rs -> {
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
            return null;
        }, id);
    }
    
    /**
     * 查询所有记录
     * @return 记录列表
     * @throws Exception 异常
     */
    @Override
    public List<T> findAll() throws Exception {
        String sql = "SELECT * FROM " + getTableName() + " ORDER BY " + getPrimaryKeyColumn() + " DESC";
        
        return JdbcHelper.executeQuery(sql, rs -> {
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
            return list;
        });
    }
    
    /**
     * 分页查询
     * @param pageNum 页码（从 1 开始）
     * @param pageSize 每页大小
     * @return 记录列表
     * @throws Exception 异常
     */
    @Override
    public List<T> findByPage(int pageNum, int pageSize) throws Exception {
        String sql = "SELECT * FROM " + getTableName() + " ORDER BY " + getPrimaryKeyColumn() + " DESC LIMIT ?, ?";
        
        return JdbcHelper.executeQuery(sql, rs -> {
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
            return list;
        }, (pageNum - 1) * pageSize, pageSize);
    }
    
    /**
     * 统计总数
     * @return 总记录数
     * @throws Exception 异常
     */
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM " + getTableName();
        
        return JdbcHelper.executeQuery(sql, rs -> {
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        });
    }
    
    /**
     * 插入记录
     * @param entity 实体对象
     * @return 影响的行数
     * @throws Exception 异常
     */
    @Override
    public int insert(T entity) throws Exception {
        String columns = getInsertColumns(entity);
        String placeholders = getPlaceholders(columns.split(",").length);
        String values = getInsertValues(entity);
        
        String sql = "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + placeholders + ")";
        
        // 子类实现具体的插入逻辑
        return executeInsert(sql, entity, values);
    }
    
    /**
     * 更新记录
     * @param entity 实体对象
     * @return 影响的行数
     * @throws Exception 异常
     */
    @Override
    public int update(T entity) throws Exception {
        // 子类实现具体的更新逻辑
        return executeUpdate(entity);
    }
    
    /**
     * 删除记录
     * @param id 记录 ID
     * @return 影响的行数
     * @throws Exception 异常
     */
    @Override
    public int delete(Integer id) throws Exception {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getPrimaryKeyColumn() + " = ?";
        return JdbcHelper.executeUpdate(sql, id);
    }
    
    /**
     * 执行插入操作（子类实现）
     * @param sql SQL 语句
     * @param entity 实体对象
     * @param values 值字符串
     * @return 影响的行数
     * @throws Exception 异常
     */
    protected abstract int executeInsert(String sql, T entity, String values) throws Exception;
    
    /**
     * 执行更新操作（子类实现）
     * @param entity 实体对象
     * @return 影响的行数
     * @throws Exception 异常
     */
    protected abstract int executeUpdate(T entity) throws Exception;
    
    /**
     * 获取插入的列名
     * @param entity 实体对象
     * @return 列名字符串
     */
    protected String getInsertColumns(T entity) {
        // 默认实现，子类可以重写
        return "";
    }
    
    /**
     * 获取插入的值
     * @param entity 实体对象
     * @return 值字符串
     */
    protected String getInsertValues(T entity) {
        // 默认实现，子类可以重写
        return "";
    }
    
    /**
     * 生成占位符字符串
     * @param count 占位符数量
     * @return 占位符字符串（如："?, ?, ?"）
     */
    protected String getPlaceholders(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(", ");
            sb.append("?");
        }
        return sb.toString();
    }
    
    /**
     * 在指定连接中执行批量操作
     * @param conn 数据库连接
     * @param sql SQL 语句
     * @param params 参数数组
     * @return 影响的行数
     * @throws SQLException SQL 异常
     */
    protected int executeWithConnection(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            return pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
}
