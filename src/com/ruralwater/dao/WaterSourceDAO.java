package com.ruralwater.dao;

import com.ruralwater.entity.WaterSource;
import com.ruralwater.util.DBUtil;
import com.ruralwater.util.JdbcHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 水源 DAO 实现类
 */
public class WaterSourceDAO implements BaseDAO<WaterSource> {
    
    /**
     * 根据类型查询水源
     */
    public List<WaterSource> findByType(String sourceType) throws Exception {
        String sql = "SELECT ws.*, r.region_name FROM water_sources ws " +
                     "LEFT JOIN regions r ON ws.region_id = r.region_id " +
                     "WHERE ws.source_type = ? ORDER BY ws.source_name";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sourceType);
            rs = pstmt.executeQuery();
            
            List<WaterSource> list = new ArrayList<>();
            while (rs.next()) {
                WaterSource source = new WaterSource();
                source.setSourceId(rs.getInt("source_id"));
                source.setSourceName(rs.getString("source_name"));
                source.setSourceType(rs.getString("source_type"));
                source.setRegionId(rs.getInt("region_id"));
                source.setLocationDetail(rs.getString("location_detail"));
                source.setLongitude(rs.getBigDecimal("longitude"));
                source.setLatitude(rs.getBigDecimal("latitude"));
                source.setCapacity(rs.getBigDecimal("capacity"));
                source.setStatus(rs.getInt("status"));
                source.setCreateTime(rs.getString("create_time"));
                source.setRegionName(rs.getString("region_name"));
                list.add(source);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 根据区域 ID 查询水源
     */
    public List<WaterSource> findByRegionId(Integer regionId) throws Exception {
        String sql = "SELECT ws.*, r.region_name FROM water_sources ws " +
                     "LEFT JOIN regions r ON ws.region_id = r.region_id " +
                     "WHERE ws.region_id = ? ORDER BY ws.source_name";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, regionId != null ? regionId : 0);
            rs = pstmt.executeQuery();
            
            List<WaterSource> list = new ArrayList<>();
            while (rs.next()) {
                WaterSource source = new WaterSource();
                source.setSourceId(rs.getInt("source_id"));
                source.setSourceName(rs.getString("source_name"));
                source.setSourceType(rs.getString("source_type"));
                source.setRegionId(rs.getInt("region_id"));
                source.setLocationDetail(rs.getString("location_detail"));
                source.setLongitude(rs.getBigDecimal("longitude"));
                source.setLatitude(rs.getBigDecimal("latitude"));
                source.setCapacity(rs.getBigDecimal("capacity"));
                source.setStatus(rs.getInt("status"));
                source.setCreateTime(rs.getString("create_time"));
                source.setRegionName(rs.getString("region_name"));
                list.add(source);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 根据关键字搜索水源
     */
    public List<WaterSource> findByKeyword(String keyword) throws Exception {
        String sql = "SELECT ws.*, r.region_name FROM water_sources ws " +
                     "LEFT JOIN regions r ON ws.region_id = r.region_id " +
                     "WHERE ws.source_name LIKE ? OR ws.location_detail LIKE ? " +
                     "ORDER BY ws.source_name";
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
            
            List<WaterSource> list = new ArrayList<>();
            while (rs.next()) {
                WaterSource source = new WaterSource();
                source.setSourceId(rs.getInt("source_id"));
                source.setSourceName(rs.getString("source_name"));
                source.setSourceType(rs.getString("source_type"));
                source.setRegionId(rs.getInt("region_id"));
                source.setLocationDetail(rs.getString("location_detail"));
                source.setLongitude(rs.getBigDecimal("longitude"));
                source.setLatitude(rs.getBigDecimal("latitude"));
                source.setCapacity(rs.getBigDecimal("capacity"));
                source.setStatus(rs.getInt("status"));
                source.setCreateTime(rs.getString("create_time"));
                source.setRegionName(rs.getString("region_name"));
                list.add(source);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public WaterSource findById(Integer id) throws Exception {
        String sql = "SELECT ws.*, r.region_name FROM water_sources ws " +
                     "LEFT JOIN regions r ON ws.region_id = r.region_id " +
                     "WHERE ws.source_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                WaterSource source = new WaterSource();
                source.setSourceId(rs.getInt("source_id"));
                source.setSourceName(rs.getString("source_name"));
                source.setSourceType(rs.getString("source_type"));
                source.setRegionId(rs.getInt("region_id"));
                source.setLocationDetail(rs.getString("location_detail"));
                source.setLongitude(rs.getBigDecimal("longitude"));
                source.setLatitude(rs.getBigDecimal("latitude"));
                source.setCapacity(rs.getBigDecimal("capacity"));
                source.setStatus(rs.getInt("status"));
                source.setCreateTime(rs.getString("create_time"));
                source.setRegionName(rs.getString("region_name"));
                return source;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public List<WaterSource> findAll() throws Exception {
        String sql = "SELECT ws.*, r.region_name FROM water_sources ws " +
                     "LEFT JOIN regions r ON ws.region_id = r.region_id " +
                     "ORDER BY ws.source_name";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<WaterSource> list = new ArrayList<>();
            while (rs.next()) {
                WaterSource source = new WaterSource();
                source.setSourceId(rs.getInt("source_id"));
                source.setSourceName(rs.getString("source_name"));
                source.setSourceType(rs.getString("source_type"));
                source.setRegionId(rs.getInt("region_id"));
                source.setLocationDetail(rs.getString("location_detail"));
                source.setLongitude(rs.getBigDecimal("longitude"));
                source.setLatitude(rs.getBigDecimal("latitude"));
                source.setCapacity(rs.getBigDecimal("capacity"));
                source.setStatus(rs.getInt("status"));
                source.setCreateTime(rs.getString("create_time"));
                source.setRegionName(rs.getString("region_name"));
                list.add(source);
            }
            return list;
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }
    
    @Override
    public List<WaterSource> findByPage(int pageNum, int pageSize) throws Exception {
        String sql = "SELECT ws.*, r.region_name FROM water_sources ws " +
                     "LEFT JOIN regions r ON ws.region_id = r.region_id " +
                     "ORDER BY ws.source_name LIMIT ?, ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (pageNum - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            
            List<WaterSource> list = new ArrayList<>();
            while (rs.next()) {
                WaterSource source = new WaterSource();
                source.setSourceId(rs.getInt("source_id"));
                source.setSourceName(rs.getString("source_name"));
                source.setSourceType(rs.getString("source_type"));
                source.setRegionId(rs.getInt("region_id"));
                source.setLocationDetail(rs.getString("location_detail"));
                source.setLongitude(rs.getBigDecimal("longitude"));
                source.setLatitude(rs.getBigDecimal("latitude"));
                source.setCapacity(rs.getBigDecimal("capacity"));
                source.setStatus(rs.getInt("status"));
                source.setCreateTime(rs.getString("create_time"));
                source.setRegionName(rs.getString("region_name"));
                list.add(source);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM water_sources";
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
    
    public int insert(WaterSource entity) throws Exception {
        return save(entity);
    }
    
    public int save(WaterSource entity) throws Exception {
        String sql = "INSERT INTO water_sources (source_name, source_type, region_id, location_detail, " +
                     "longitude, latitude, capacity, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return JdbcHelper.executeUpdate(sql,
            entity.getSourceName(),
            entity.getSourceType(),
            entity.getRegionId(),
            entity.getLocationDetail(),
            entity.getLongitude(),
            entity.getLatitude(),
            entity.getCapacity(),
            entity.getStatus() != null ? entity.getStatus() : 1);
    }
    
    @Override
    public int update(WaterSource entity) throws Exception {
        String sql = "UPDATE water_sources SET source_name=?, source_type=?, region_id=?, " +
                     "location_detail=?, longitude=?, latitude=?, capacity=?, status=? WHERE source_id=?";
        return JdbcHelper.executeUpdate(sql,
            entity.getSourceName(),
            entity.getSourceType(),
            entity.getRegionId(),
            entity.getLocationDetail(),
            entity.getLongitude(),
            entity.getLatitude(),
            entity.getCapacity(),
            entity.getStatus(),
            entity.getSourceId());
    }
    
    @Override
    public int delete(Integer id) throws Exception {
        String sql = "DELETE FROM water_sources WHERE source_id = ?";
        return JdbcHelper.executeUpdate(sql, id);
    }
}
