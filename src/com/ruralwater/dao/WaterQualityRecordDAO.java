package com.ruralwater.dao;

import com.ruralwater.entity.WaterQualityRecord;
import com.ruralwater.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 水质检测记录 DAO 实现类（原生 JDBC）
 */
public class WaterQualityRecordDAO implements BaseDAO<WaterQualityRecord> {
    
    /**
     * 多条件组合查询（分页、排序、关键字）
     */
    public List<WaterQualityRecord> findByCondition(Integer plantId, String startDate, String endDate, 
                                                     String conclusion, int pageNum, int pageSize) throws Exception {
        StringBuilder sql = new StringBuilder(
            "SELECT wr.*, wp.plant_name, u.real_name as tester_name " +
            "FROM water_quality_records wr " +
            "LEFT JOIN water_plants wp ON wr.plant_id = wp.plant_id " +
            "LEFT JOIN users u ON wr.tester_id = u.user_id " +
            "WHERE 1=1"
        );
        
        List<Object> params = new ArrayList<>();
        
        if (plantId != null && plantId > 0) {
            sql.append(" AND wr.plant_id = ?");
            params.add(plantId);
        }
        
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND wr.sample_time >= ?");
            params.add(startDate);
        }
        
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND wr.sample_time <= ?");
            params.add(endDate);
        }
        
        if (conclusion != null && !conclusion.isEmpty()) {
            sql.append(" AND wr.conclusion = ?");
            params.add(conclusion);
        }
        
        sql.append(" ORDER BY wr.sample_time DESC LIMIT ?, ?");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            rs = pstmt.executeQuery();
            
            List<WaterQualityRecord> list = new ArrayList<>();
            while (rs.next()) {
                WaterQualityRecord record = new WaterQualityRecord();
                record.setRecordId(rs.getInt("record_id"));
                record.setPlantId(rs.getInt("plant_id"));
                record.setSamplePoint(rs.getString("sample_point"));
                record.setSampleTime(rs.getString("sample_time"));
                record.setTesterId(rs.getInt("tester_id"));
                record.setReviewStatus(rs.getString("review_status"));
                record.setConclusion(rs.getString("conclusion"));
                record.setRemark(rs.getString("remark"));
                record.setCreateTime(rs.getString("create_time"));
                record.setPlantName(rs.getString("plant_name"));
                record.setTesterName(rs.getString("tester_name"));
                list.add(record);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 统计符合条件的记录数
     */
    public int getCountByCondition(Integer plantId, String startDate, String endDate, String conclusion) throws Exception {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as count FROM water_quality_records WHERE 1=1"
        );
        
        List<Object> params = new ArrayList<>();
        
        if (plantId != null && plantId > 0) {
            sql.append(" AND plant_id = ?");
            params.add(plantId);
        }
        
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND sample_time >= ?");
            params.add(startDate);
        }
        
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND sample_time <= ?");
            params.add(endDate);
        }
        
        if (conclusion != null && !conclusion.isEmpty()) {
            sql.append(" AND conclusion = ?");
            params.add(conclusion);
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 审核通过（带事务处理）
     */
    public void approveRecord(Integer recordId, Integer reviewerId, String conclusion) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            // 开启事务
            DBUtil.beginTransaction(conn);
            
            String updateSql = "UPDATE water_quality_records SET review_status='approved', " +
                              "reviewer_id=?, review_time=NOW(), conclusion=? WHERE record_id=?";
            
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setInt(1, reviewerId);
            pstmt.setString(2, conclusion);
            pstmt.setInt(3, recordId);
            
            pstmt.executeUpdate();
            
            // 提交事务
            DBUtil.commit(conn);
        } catch (Exception e) {
            // 回滚事务
            if (conn != null) {
                DBUtil.rollback(conn);
            }
            throw e;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int insert(WaterQualityRecord record) throws Exception {
        String sql = "INSERT INTO water_quality_records(plant_id, sample_point, sample_time, " +
                     "tester_id, review_status, conclusion, remark) VALUES(?, ?, ?, ?, 'pending', ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, record.getPlantId());
            pstmt.setString(2, record.getSamplePoint());
            pstmt.setString(3, record.getSampleTime());
            pstmt.setInt(4, record.getTesterId());
            pstmt.setString(5, record.getConclusion());
            pstmt.setString(6, record.getRemark());
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int update(WaterQualityRecord record) throws Exception {
        String sql = "UPDATE water_quality_records SET plant_id=?, sample_point=?, " +
                     "sample_time=?, tester_id=?, conclusion=?, remark=? WHERE record_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, record.getPlantId());
            pstmt.setString(2, record.getSamplePoint());
            pstmt.setString(3, record.getSampleTime());
            pstmt.setInt(4, record.getTesterId());
            pstmt.setString(5, record.getConclusion());
            pstmt.setString(6, record.getRemark());
            pstmt.setInt(7, record.getRecordId());
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int delete(Integer id) throws Exception {
        String sql = "DELETE FROM water_quality_records WHERE record_id=?";
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
    public WaterQualityRecord findById(Integer id) throws Exception {
        String sql = "SELECT wr.*, wp.plant_name, u.real_name as tester_name " +
                     "FROM water_quality_records wr " +
                     "LEFT JOIN water_plants wp ON wr.plant_id = wp.plant_id " +
                     "LEFT JOIN users u ON wr.tester_id = u.user_id " +
                     "WHERE wr.record_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                WaterQualityRecord record = new WaterQualityRecord();
                record.setRecordId(rs.getInt("record_id"));
                record.setPlantId(rs.getInt("plant_id"));
                record.setSamplePoint(rs.getString("sample_point"));
                record.setSampleTime(rs.getString("sample_time"));
                record.setTesterId(rs.getInt("tester_id"));
                record.setReviewStatus(rs.getString("review_status"));
                record.setConclusion(rs.getString("conclusion"));
                record.setRemark(rs.getString("remark"));
                record.setCreateTime(rs.getString("create_time"));
                record.setPlantName(rs.getString("plant_name"));
                record.setTesterName(rs.getString("tester_name"));
                return record;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public List<WaterQualityRecord> findAll() throws Exception {
        String sql = "SELECT wr.*, wp.plant_name, u.real_name as tester_name " +
                     "FROM water_quality_records wr " +
                     "LEFT JOIN water_plants wp ON wr.plant_id = wp.plant_id " +
                     "LEFT JOIN users u ON wr.tester_id = u.user_id " +
                     "ORDER BY wr.sample_time DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<WaterQualityRecord> list = new ArrayList<>();
            while (rs.next()) {
                WaterQualityRecord record = new WaterQualityRecord();
                record.setRecordId(rs.getInt("record_id"));
                record.setPlantId(rs.getInt("plant_id"));
                record.setSamplePoint(rs.getString("sample_point"));
                record.setSampleTime(rs.getString("sample_time"));
                record.setTesterId(rs.getInt("tester_id"));
                record.setReviewStatus(rs.getString("review_status"));
                record.setConclusion(rs.getString("conclusion"));
                record.setRemark(rs.getString("remark"));
                record.setCreateTime(rs.getString("create_time"));
                record.setPlantName(rs.getString("plant_name"));
                record.setTesterName(rs.getString("tester_name"));
                list.add(record);
            }
            return list;
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }
    
    @Override
    public List<WaterQualityRecord> findByPage(int pageNum, int pageSize) throws Exception {
        String sql = "SELECT wr.*, wp.plant_name, u.real_name as tester_name " +
                     "FROM water_quality_records wr " +
                     "LEFT JOIN water_plants wp ON wr.plant_id = wp.plant_id " +
                     "LEFT JOIN users u ON wr.tester_id = u.user_id " +
                     "ORDER BY wr.sample_time DESC LIMIT ?, ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (pageNum - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            
            rs = pstmt.executeQuery();
            
            List<WaterQualityRecord> list = new ArrayList<>();
            while (rs.next()) {
                WaterQualityRecord record = new WaterQualityRecord();
                record.setRecordId(rs.getInt("record_id"));
                record.setPlantId(rs.getInt("plant_id"));
                record.setSamplePoint(rs.getString("sample_point"));
                record.setSampleTime(rs.getString("sample_time"));
                record.setTesterId(rs.getInt("tester_id"));
                record.setReviewStatus(rs.getString("review_status"));
                record.setConclusion(rs.getString("conclusion"));
                record.setRemark(rs.getString("remark"));
                record.setCreateTime(rs.getString("create_time"));
                record.setPlantName(rs.getString("plant_name"));
                record.setTesterName(rs.getString("tester_name"));
                list.add(record);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM water_quality_records";
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
