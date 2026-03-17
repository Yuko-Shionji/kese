package com.ruralwater.dao;

import java.util.List;

/**
 * 基础 DAO 接口（体现面向对象的多态特性）
 */
public interface BaseDAO<T> {
    
    /**
     * 插入记录
     */
    int insert(T entity) throws Exception;
    
    /**
     * 更新记录
     */
    int update(T entity) throws Exception;
    
    /**
     * 删除记录
     */
    int delete(Integer id) throws Exception;
    
    /**
     * 根据 ID 查询
     */
    T findById(Integer id) throws Exception;
    
    /**
     * 查询所有记录
     */
    List<T> findAll() throws Exception;
    
    /**
     * 分页查询
     */
    List<T> findByPage(int pageNum, int pageSize) throws Exception;
    
    /**
     * 统计总数
     */
    int getCount() throws Exception;
}
