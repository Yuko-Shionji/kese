package com.ruralwater.dao;

import com.ruralwater.entity.WaterQualityStandard;
import com.ruralwater.util.DBUtil;
import com.ruralwater.util.JdbcHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 水质检测标准 DAO 实现类
 */
public class WaterQualityStandardDAO implements BaseDAO<WaterQualityStandard> {
    
    /**
     * 根据类别查询标准
     */
    public List<WaterQualityStandard> findByCategory(String category) throws Exception {
        String sql = "SELECT * FROM water_quality_standards WHERE category = ? AND is_active = 1 ORDER BY item_name";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            
            List<WaterQualityStandard> list = new ArrayList<>();
            while (rs.next()) {
                WaterQualityStandard standard = new WaterQualityStandard();
                standard.setStandardId(rs.getInt("standard_id"));
                standard.setItemName(rs.getString("item_name"));
                standard.setItemCode(rs.getString("item_code"));
                standard.setUnit(rs.getString("unit"));
                standard.setStandardValue(rs.getBigDecimal("standard_value"));
                standard.setMinValue(rs.getBigDecimal("min_value"));
                standard.setMaxValue(rs.getBigDecimal("max_value"));
                standard.setCategory(rs.getString("category"));
                standard.setDescription(rs.getString("description"));
                standard.setIsActive(rs.getInt("is_active"));
                list.add(standard);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 查询所有激活的标准
     */
    public List<WaterQualityStandard> findActiveStandards() throws Exception {
        String sql = "SELECT * FROM water_quality_standards WHERE is_active = 1 ORDER BY category, item_name";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<WaterQualityStandard> list = new ArrayList<>();
            while (rs.next()) {
                WaterQualityStandard standard = new WaterQualityStandard();
                standard.setStandardId(rs.getInt("standard_id"));
                standard.setItemName(rs.getString("item_name"));
                standard.setItemCode(rs.getString("item_code"));
                standard.setUnit(rs.getString("unit"));
                standard.setStandardValue(rs.getBigDecimal("standard_value"));
                standard.setMinValue(rs.getBigDecimal("min_value"));
                standard.setMaxValue(rs.getBigDecimal("max_value"));
                standard.setCategory(rs.getString("category"));
                standard.setDescription(rs.getString("description"));
                standard.setIsActive(rs.getInt("is_active"));
                list.add(standard);
            }
            return list;
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }
    
    /**
     * 根据关键字搜索标准
     */
    public List<WaterQualityStandard> findByKeyword(String keyword) throws Exception {
        String sql = "SELECT * FROM water_quality_standards " +
                     "WHERE item_name LIKE ? OR item_code LIKE ? " +
                     "ORDER BY category, item_name";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            String likeKeyword = "%" + keyword + "%";
            pstmt.setString(1, likeKeyword);
            pstmt.setString(2, likeKeyword);
            rs = pstmt.executeQuery();
            
            List<WaterQualityStandard> list = new ArrayList<>();
            while (rs.next()) {
                WaterQualityStandard standard = new WaterQualityStandard();
                standard.setStandardId(rs.getInt("standard_id"));
                standard.setItemName(rs.getString("item_name"));
                standard.setItemCode(rs.getString("item_code"));
                standard.setUnit(rs.getString("unit"));
                standard.setStandardValue(rs.getBigDecimal("standard_value"));
                standard.setMinValue(rs.getBigDecimal("min_value"));
                standard.setMaxValue(rs.getBigDecimal("max_value"));
                standard.setCategory(rs.getString("category"));
                standard.setDescription(rs.getString("description"));
                standard.setIsActive(rs.getInt("is_active"));
                list.add(standard);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public WaterQualityStandard findById(Integer id) throws Exception {
        String sql = "SELECT * FROM water_quality_standards WHERE standard_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                WaterQualityStandard standard = new WaterQualityStandard();
                standard.setStandardId(rs.getInt("standard_id"));
                standard.setItemName(rs.getString("item_name"));
                standard.setItemCode(rs.getString("item_code"));
                standard.setUnit(rs.getString("unit"));
                standard.setStandardValue(rs.getBigDecimal("standard_value"));
                standard.setMinValue(rs.getBigDecimal("min_value"));
                standard.setMaxValue(rs.getBigDecimal("max_value"));
                standard.setCategory(rs.getString("category"));
                standard.setDescription(rs.getString("description"));
                standard.setIsActive(rs.getInt("is_active"));
                return standard;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public List<WaterQualityStandard> findAll() throws Exception {
        String sql = "SELECT * FROM water_quality_standards ORDER BY category, item_name";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<WaterQualityStandard> list = new ArrayList<>();
            while (rs.next()) {
                WaterQualityStandard standard = new WaterQualityStandard();
                standard.setStandardId(rs.getInt("standard_id"));
                standard.setItemName(rs.getString("item_name"));
                standard.setItemCode(rs.getString("item_code"));
                standard.setUnit(rs.getString("unit"));
                standard.setStandardValue(rs.getBigDecimal("standard_value"));
                standard.setMinValue(rs.getBigDecimal("min_value"));
                standard.setMaxValue(rs.getBigDecimal("max_value"));
                standard.setCategory(rs.getString("category"));
                standard.setDescription(rs.getString("description"));
                standard.setIsActive(rs.getInt("is_active"));
                list.add(standard);
            }
            return list;
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }
    
    @Override
    public List<WaterQualityStandard> findByPage(int pageNum, int pageSize) throws Exception {
        String sql = "SELECT * FROM water_quality_standards ORDER BY category, item_name LIMIT ?, ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (pageNum - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            
            List<WaterQualityStandard> list = new ArrayList<>();
            while (rs.next()) {
                WaterQualityStandard standard = new WaterQualityStandard();
                standard.setStandardId(rs.getInt("standard_id"));
                standard.setItemName(rs.getString("item_name"));
                standard.setItemCode(rs.getString("item_code"));
                standard.setUnit(rs.getString("unit"));
                standard.setStandardValue(rs.getBigDecimal("standard_value"));
                standard.setMinValue(rs.getBigDecimal("min_value"));
                standard.setMaxValue(rs.getBigDecimal("max_value"));
                standard.setCategory(rs.getString("category"));
                standard.setDescription(rs.getString("description"));
                standard.setIsActive(rs.getInt("is_active"));
                list.add(standard);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM water_quality_standards";
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
    
    public int insert(WaterQualityStandard entity) throws Exception {
        return save(entity);
    }
    
    public int save(WaterQualityStandard entity) throws Exception {
        String sql = "INSERT INTO water_quality_standards (item_name, item_code, unit, standard_value, " +
                     "min_value, max_value, category, description, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return JdbcHelper.executeUpdate(sql,
            entity.getItemName(),
            entity.getItemCode(),
            entity.getUnit(),
            entity.getStandardValue(),
            entity.getMinValue(),
            entity.getMaxValue(),
            entity.getCategory(),
            entity.getDescription(),
            entity.getIsActive() != null ? entity.getIsActive() : 1);
    }
    
    @Override
    public int update(WaterQualityStandard entity) throws Exception {
        String sql = "UPDATE water_quality_standards SET item_name=?, item_code=?, unit=?, " +
                     "standard_value=?, min_value=?, max_value=?, category=?, description=?, " +
                     "is_active=? WHERE standard_id=?";
        return JdbcHelper.executeUpdate(sql,
            entity.getItemName(),
            entity.getItemCode(),
            entity.getUnit(),
            entity.getStandardValue(),
            entity.getMinValue(),
            entity.getMaxValue(),
            entity.getCategory(),
            entity.getDescription(),
            entity.getIsActive(),
            entity.getStandardId());
    }
    
    @Override
    public int delete(Integer id) throws Exception {
        String sql = "DELETE FROM water_quality_standards WHERE standard_id = ?";
        return JdbcHelper.executeUpdate(sql, id);
    }
}
