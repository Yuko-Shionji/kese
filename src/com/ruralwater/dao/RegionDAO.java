package com.ruralwater.dao;

import com.ruralwater.entity.Region;
import com.ruralwater.util.DBUtil;
import com.ruralwater.util.JdbcHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域 DAO 实现类
 */
public class RegionDAO implements BaseDAO<Region> {
    
    /**
     * 根据层级查询区域
     */
    public List<Region> findByLevel(String level) throws Exception {
        String sql = "SELECT * FROM regions WHERE level = ? ORDER BY region_code";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, level);
            rs = pstmt.executeQuery();
            
            List<Region> list = new ArrayList<>();
            while (rs.next()) {
                Region region = new Region();
                region.setRegionId(rs.getInt("region_id"));
                region.setRegionCode(rs.getString("region_code"));
                region.setRegionName(rs.getString("region_name"));
                region.setParentId(rs.getInt("parent_id"));
                region.setLevel(rs.getString("level"));
                region.setFullPath(rs.getString("full_path"));
                list.add(region);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 根据父 ID 查询子区域
     */
    public List<Region> findByParentId(Integer parentId) throws Exception {
        String sql = "SELECT * FROM regions WHERE parent_id = ? ORDER BY region_code";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, parentId != null ? parentId : 0);
            rs = pstmt.executeQuery();
            
            List<Region> list = new ArrayList<>();
            while (rs.next()) {
                Region region = new Region();
                region.setRegionId(rs.getInt("region_id"));
                region.setRegionCode(rs.getString("region_code"));
                region.setRegionName(rs.getString("region_name"));
                region.setParentId(rs.getInt("parent_id"));
                region.setLevel(rs.getString("level"));
                region.setFullPath(rs.getString("full_path"));
                list.add(region);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 根据关键字搜索区域
     */
    public List<Region> findByKeyword(String keyword) throws Exception {
        String sql = "SELECT * FROM regions WHERE region_name LIKE ? OR region_code LIKE ? ORDER BY region_code";
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
            
            List<Region> list = new ArrayList<>();
            while (rs.next()) {
                Region region = new Region();
                region.setRegionId(rs.getInt("region_id"));
                region.setRegionCode(rs.getString("region_code"));
                region.setRegionName(rs.getString("region_name"));
                region.setParentId(rs.getInt("parent_id"));
                region.setLevel(rs.getString("level"));
                region.setFullPath(rs.getString("full_path"));
                list.add(region);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public Region findById(Integer id) throws Exception {
        String sql = "SELECT * FROM regions WHERE region_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Region region = new Region();
                region.setRegionId(rs.getInt("region_id"));
                region.setRegionCode(rs.getString("region_code"));
                region.setRegionName(rs.getString("region_name"));
                region.setParentId(rs.getInt("parent_id"));
                region.setLevel(rs.getString("level"));
                region.setFullPath(rs.getString("full_path"));
                return region;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public List<Region> findAll() throws Exception {
        String sql = "SELECT * FROM regions ORDER BY region_code";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<Region> list = new ArrayList<>();
            while (rs.next()) {
                Region region = new Region();
                region.setRegionId(rs.getInt("region_id"));
                region.setRegionCode(rs.getString("region_code"));
                region.setRegionName(rs.getString("region_name"));
                region.setParentId(rs.getInt("parent_id"));
                region.setLevel(rs.getString("level"));
                region.setFullPath(rs.getString("full_path"));
                list.add(region);
            }
            return list;
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }
    
    @Override
    public List<Region> findByPage(int pageNum, int pageSize) throws Exception {
        String sql = "SELECT * FROM regions ORDER BY region_code LIMIT ?, ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (pageNum - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            
            List<Region> list = new ArrayList<>();
            while (rs.next()) {
                Region region = new Region();
                region.setRegionId(rs.getInt("region_id"));
                region.setRegionCode(rs.getString("region_code"));
                region.setRegionName(rs.getString("region_name"));
                region.setParentId(rs.getInt("parent_id"));
                region.setLevel(rs.getString("level"));
                region.setFullPath(rs.getString("full_path"));
                list.add(region);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM regions";
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
    
    public int insert(Region entity) throws Exception {
        return save(entity);
    }
    
    public int save(Region entity) throws Exception {
        String sql = "INSERT INTO regions (region_code, region_name, parent_id, level, full_path) VALUES (?, ?, ?, ?, ?)";
        return JdbcHelper.executeUpdate(sql, 
            entity.getRegionCode(),
            entity.getRegionName(),
            entity.getParentId() != null ? entity.getParentId() : 0,
            entity.getLevel(),
            entity.getFullPath());
    }
    
    @Override
    public int update(Region entity) throws Exception {
        String sql = "UPDATE regions SET region_code=?, region_name=?, parent_id=?, level=?, full_path=? WHERE region_id=?";
        return JdbcHelper.executeUpdate(sql,
            entity.getRegionCode(),
            entity.getRegionName(),
            entity.getParentId(),
            entity.getLevel(),
            entity.getFullPath(),
            entity.getRegionId());
    }
    
    @Override
    public int delete(Integer id) throws Exception {
        String sql = "DELETE FROM regions WHERE region_id = ?";
        return JdbcHelper.executeUpdate(sql, id);
    }
}
