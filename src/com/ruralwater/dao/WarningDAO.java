package com.ruralwater.dao;

import com.ruralwater.entity.Warning;
import com.ruralwater.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 预警信息 DAO 实现类（原生 JDBC）
 */
public class WarningDAO implements BaseDAO<Warning> {
    
    /**
     * 多条件组合查询（分页、排序）
     */
    public List<Warning> findByCondition(String warningType, String warningLevel, 
                                         String status, Integer plantId, 
                                         int pageNum, int pageSize) throws Exception {
        StringBuilder sql = new StringBuilder(
            "SELECT w.*, wp.plant_name, u.real_name as handler_name " +
            "FROM warnings w " +
            "LEFT JOIN water_plants wp ON w.plant_id = wp.plant_id " +
            "LEFT JOIN users u ON w.handler_id = u.user_id " +
            "WHERE 1=1"
        );
        
        List<Object> params = new ArrayList<>();
        
        if (warningType != null && !warningType.isEmpty()) {
            sql.append(" AND w.warning_type = ?");
            params.add(warningType);
        }
        
        if (warningLevel != null && !warningLevel.isEmpty()) {
            sql.append(" AND w.warning_level = ?");
            params.add(warningLevel);
        }
        
        if (status != null && !status.isEmpty()) {
            sql.append(" AND w.status = ?");
            params.add(status);
        }
        
        if (plantId != null && plantId > 0) {
            sql.append(" AND w.plant_id = ?");
            params.add(plantId);
        }
        
        sql.append(" ORDER BY w.create_time DESC LIMIT ?, ?");
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
            
            List<Warning> list = new ArrayList<>();
            while (rs.next()) {
                Warning warning = new Warning();
                warning.setWarningId(rs.getInt("warning_id"));
                warning.setRecordId(rs.getObject("record_id") != null ? rs.getInt("record_id") : null);
                warning.setPlantId(rs.getInt("plant_id"));
                warning.setWarningType(rs.getString("warning_type"));
                warning.setWarningLevel(rs.getString("warning_level"));
                warning.setTitle(rs.getString("title"));
                warning.setContent(rs.getString("content"));
                warning.setStatus(rs.getString("status"));
                warning.setHandlerId(rs.getObject("handler_id") != null ? rs.getInt("handler_id") : null);
                warning.setHandleTime(rs.getString("handle_time"));
                warning.setHandleResult(rs.getString("handle_result"));
                warning.setCreateTime(rs.getString("create_time"));
                warning.setPlantName(rs.getString("plant_name"));
                warning.setHandlerName(rs.getString("handler_name"));
                list.add(warning);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 统计符合条件的记录数
     */
    public int getCountByCondition(String warningType, String warningLevel, 
                                   String status, Integer plantId) throws Exception {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as count FROM warnings WHERE 1=1"
        );
        
        List<Object> params = new ArrayList<>();
        
        if (warningType != null && !warningType.isEmpty()) {
            sql.append(" AND warning_type = ?");
            params.add(warningType);
        }
        
        if (warningLevel != null && !warningLevel.isEmpty()) {
            sql.append(" AND warning_level = ?");
            params.add(warningLevel);
        }
        
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        
        if (plantId != null && plantId > 0) {
            sql.append(" AND plant_id = ?");
            params.add(plantId);
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
     * 处理预警
     */
    public void handleWarning(Integer warningId, Integer handlerId, 
                             String handleResult, String status) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            DBUtil.beginTransaction(conn);
            
            String updateSql = "UPDATE warnings SET status=?, handler_id=?, " +
                              "handle_time=NOW(), handle_result=? WHERE warning_id=?";
            
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, status);
            pstmt.setInt(2, handlerId);
            pstmt.setString(3, handleResult);
            pstmt.setInt(4, warningId);
            
            pstmt.executeUpdate();
            
            DBUtil.commit(conn);
        } catch (Exception e) {
            if (conn != null) {
                DBUtil.rollback(conn);
            }
            throw e;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int insert(Warning warning) throws Exception {
        String sql = "INSERT INTO warnings(plant_id, warning_type, " +
                    "warning_level, title, content, status) VALUES(?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, warning.getPlantId());
            pstmt.setString(2, warning.getWarningType());
            pstmt.setString(3, warning.getWarningLevel());
            pstmt.setString(4, warning.getTitle());
            pstmt.setString(5, warning.getContent());
            pstmt.setString(6, warning.getStatus());
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int update(Warning warning) throws Exception {
        String sql = "UPDATE warnings SET warning_type=?, warning_level=?, " +
                    "title=?, content=?, status=?, handle_result=? WHERE warning_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, warning.getWarningType());
            pstmt.setString(2, warning.getWarningLevel());
            pstmt.setString(3, warning.getTitle());
            pstmt.setString(4, warning.getContent());
            pstmt.setString(5, warning.getStatus());
            pstmt.setString(6, warning.getHandleResult());
            pstmt.setInt(7, warning.getWarningId());
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int delete(Integer id) throws Exception {
        String sql = "DELETE FROM warnings WHERE warning_id=?";
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
    public Warning findById(Integer id) throws Exception {
        String sql = "SELECT w.*, wp.plant_name, u.real_name as handler_name " +
                    "FROM warnings w " +
                    "LEFT JOIN water_plants wp ON w.plant_id = wp.plant_id " +
                    "LEFT JOIN users u ON w.handler_id = u.user_id " +
                    "WHERE w.warning_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Warning warning = new Warning();
                warning.setWarningId(rs.getInt("warning_id"));
                warning.setRecordId(rs.getObject("record_id") != null ? rs.getInt("record_id") : null);
                warning.setPlantId(rs.getInt("plant_id"));
                warning.setWarningType(rs.getString("warning_type"));
                warning.setWarningLevel(rs.getString("warning_level"));
                warning.setTitle(rs.getString("title"));
                warning.setContent(rs.getString("content"));
                warning.setStatus(rs.getString("status"));
                warning.setHandlerId(rs.getObject("handler_id") != null ? rs.getInt("handler_id") : null);
                warning.setHandleTime(rs.getString("handle_time"));
                warning.setHandleResult(rs.getString("handle_result"));
                warning.setCreateTime(rs.getString("create_time"));
                warning.setPlantName(rs.getString("plant_name"));
                warning.setHandlerName(rs.getString("handler_name"));
                return warning;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public List<Warning> findAll() throws Exception {
        String sql = "SELECT w.*, wp.plant_name, u.real_name as handler_name " +
                    "FROM warnings w " +
                    "LEFT JOIN water_plants wp ON w.plant_id = wp.plant_id " +
                    "LEFT JOIN users u ON w.handler_id = u.user_id " +
                    "ORDER BY w.create_time DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<Warning> list = new ArrayList<>();
            while (rs.next()) {
                Warning warning = new Warning();
                warning.setWarningId(rs.getInt("warning_id"));
                warning.setRecordId(rs.getObject("record_id") != null ? rs.getInt("record_id") : null);
                warning.setPlantId(rs.getInt("plant_id"));
                warning.setWarningType(rs.getString("warning_type"));
                warning.setWarningLevel(rs.getString("warning_level"));
                warning.setTitle(rs.getString("title"));
                warning.setContent(rs.getString("content"));
                warning.setStatus(rs.getString("status"));
                warning.setHandlerId(rs.getObject("handler_id") != null ? rs.getInt("handler_id") : null);
                warning.setHandleTime(rs.getString("handle_time"));
                warning.setHandleResult(rs.getString("handle_result"));
                warning.setCreateTime(rs.getString("create_time"));
                warning.setPlantName(rs.getString("plant_name"));
                warning.setHandlerName(rs.getString("handler_name"));
                list.add(warning);
            }
            return list;
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }
    
    @Override
    public List<Warning> findByPage(int pageNum, int pageSize) throws Exception {
        return findByCondition(null, null, null, null, pageNum, pageSize);
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM warnings";
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
