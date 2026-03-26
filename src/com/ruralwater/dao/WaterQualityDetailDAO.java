package com.ruralwater.dao;

import com.ruralwater.entity.WaterQualityDetail;
import com.ruralwater.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 水质检测详情 DAO 实现类
 */
public class WaterQualityDetailDAO implements BaseDAO<WaterQualityDetail> {
    
    /**
     * 根据记录 ID 查询所有详情
     */
    public List<WaterQualityDetail> findByRecordId(Integer recordId) throws Exception {
        String sql = "SELECT wq.*, ws.item_name, ws.item_code, ws.unit, " +
                     "ws.standard_value, ws.min_value, ws.max_value, ws.category " +
                     "FROM water_quality_details wq " +
                     "LEFT JOIN water_quality_standards ws ON wq.standard_id = ws.standard_id " +
                     "WHERE wq.record_id = ? " +
                     "ORDER BY ws.category, ws.item_name";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recordId);
            
            rs = pstmt.executeQuery();
            
            List<WaterQualityDetail> list = new ArrayList<>();
            while (rs.next()) {
                WaterQualityDetail detail = new WaterQualityDetail();
                detail.setDetailId(rs.getInt("detail_id"));
                detail.setRecordId(rs.getInt("record_id"));
                detail.setStandardId(rs.getInt("standard_id"));
                detail.setMeasuredValue(rs.getBigDecimal("measured_value"));
                detail.setIsQualified(rs.getInt("is_qualified"));
                detail.setRemark(rs.getString("remark"));
                
                // 关联标准字段
                detail.setItemName(rs.getString("item_name"));
                detail.setItemCode(rs.getString("item_code"));
                detail.setUnit(rs.getString("unit"));
                detail.setStandardValue(rs.getBigDecimal("standard_value"));
                detail.setMinValue(rs.getBigDecimal("min_value"));
                detail.setMaxValue(rs.getBigDecimal("max_value"));
                detail.setCategory(rs.getString("category"));
                
                list.add(detail);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 批量插入检测详情（带事务处理）
     */
    public int batchInsert(List<WaterQualityDetail> details) throws Exception {
        if (details == null || details.isEmpty()) {
            return 0;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            DBUtil.beginTransaction(conn);
            
            // 临时禁用触发器
            stmt = conn.createStatement();
            stmt.execute("SET @OLD_TMP_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0");
            
            String sql = "INSERT INTO water_quality_details(record_id, standard_id, measured_value, is_qualified, remark) " +
                         "VALUES(?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            
            for (WaterQualityDetail detail : details) {
                pstmt.setInt(1, detail.getRecordId());
                pstmt.setInt(2, detail.getStandardId());
                pstmt.setBigDecimal(3, detail.getMeasuredValue());
                pstmt.setInt(4, detail.getIsQualified() != null ? detail.getIsQualified() : 1);
                pstmt.setString(5, detail.getRemark() != null ? detail.getRemark() : "");
                pstmt.addBatch();
            }
            
            System.out.println("[WaterQualityDetailDAO] 执行批量插入，记录数：" + details.size());
            int[] results = pstmt.executeBatch();
            System.out.println("[WaterQualityDetailDAO] 批量插入完成，成功数：" + results.length);
            
            // 恢复触发器
            stmt.execute("SET FOREIGN_KEY_CHECKS=@OLD_TMP_FOREIGN_KEY_CHECKS");
            
            DBUtil.commit(conn);
            System.out.println("[WaterQualityDetailDAO] 事务提交成功");
            
            return results.length;
        } catch (Exception e) {
            System.err.println("[WaterQualityDetailDAO] 发生异常：" + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                DBUtil.rollback(conn);
                System.out.println("[WaterQualityDetailDAO] 事务已回滚");
            }
            throw e;
        } finally {
            DBUtil.close(conn, pstmt);
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }
        }
    }
    
    @Override
    public int insert(WaterQualityDetail detail) throws Exception {
        String sql = "INSERT INTO water_quality_details(record_id, standard_id, measured_value, is_qualified, remark) " +
                     "VALUES(?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, detail.getRecordId());
            pstmt.setInt(2, detail.getStandardId());
            pstmt.setBigDecimal(3, detail.getMeasuredValue());
            pstmt.setInt(4, detail.getIsQualified() != null ? detail.getIsQualified() : 1);
            pstmt.setString(5, detail.getRemark() != null ? detail.getRemark() : "");
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int update(WaterQualityDetail detail) throws Exception {
        String sql = "UPDATE water_quality_details SET measured_value=?, is_qualified=?, remark=? " +
                     "WHERE detail_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, detail.getMeasuredValue());
            pstmt.setInt(2, detail.getIsQualified());
            pstmt.setString(3, detail.getRemark());
            pstmt.setInt(4, detail.getDetailId());
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int delete(Integer id) throws Exception {
        String sql = "DELETE FROM water_quality_details WHERE detail_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public WaterQualityDetail findById(Integer id) throws Exception {
        String sql = "SELECT wq.*, ws.item_name, ws.item_code, ws.unit, " +
                     "ws.standard_value, ws.min_value, ws.max_value, ws.category " +
                     "FROM water_quality_details wq " +
                     "LEFT JOIN water_quality_standards ws ON wq.standard_id = ws.standard_id " +
                     "WHERE wq.detail_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                WaterQualityDetail detail = new WaterQualityDetail();
                detail.setDetailId(rs.getInt("detail_id"));
                detail.setRecordId(rs.getInt("record_id"));
                detail.setStandardId(rs.getInt("standard_id"));
                detail.setMeasuredValue(rs.getBigDecimal("measured_value"));
                detail.setIsQualified(rs.getInt("is_qualified"));
                detail.setRemark(rs.getString("remark"));
                detail.setItemName(rs.getString("item_name"));
                detail.setItemCode(rs.getString("item_code"));
                detail.setUnit(rs.getString("unit"));
                detail.setStandardValue(rs.getBigDecimal("standard_value"));
                detail.setMinValue(rs.getBigDecimal("min_value"));
                detail.setMaxValue(rs.getBigDecimal("max_value"));
                detail.setCategory(rs.getString("category"));
                return detail;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public List<WaterQualityDetail> findAll() throws Exception {
        return findByRecordId(null);
    }
    
    @Override
    public List<WaterQualityDetail> findByPage(int pageNum, int pageSize) throws Exception {
        // 详情表通常按记录查询，此方法不常用
        return new ArrayList<>();
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM water_quality_details";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }
}
