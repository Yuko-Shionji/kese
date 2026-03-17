package com.ruralwater.dao;

import com.ruralwater.entity.WaterPlant;
import com.ruralwater.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 水厂 DAO 实现类（原生 JDBC）
 */
public class WaterPlantDAO implements BaseDAO<WaterPlant> {
    
    /**
     * 关键字模糊查询（支持多表关联）
     */
    public List<WaterPlant> findByKeyword(String keyword) throws Exception {
        String sql = "SELECT wp.*, r.region_name, ws.source_name " +
                     "FROM water_plants wp " +
                     "LEFT JOIN regions r ON wp.region_id = r.region_id " +
                     "LEFT JOIN water_sources ws ON wp.source_id = ws.source_id " +
                     "WHERE wp.plant_name LIKE ? OR wp.plant_code LIKE ? " +
                     "ORDER BY wp.plant_id DESC";
        
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
            
            List<WaterPlant> list = new ArrayList<>();
            while (rs.next()) {
                WaterPlant plant = new WaterPlant();
                plant.setPlantId(rs.getInt("plant_id"));
                plant.setPlantName(rs.getString("plant_name"));
                plant.setPlantCode(rs.getString("plant_code"));
                plant.setRegionId(rs.getInt("region_id"));
                plant.setSourceId(rs.getInt("source_id"));
                plant.setAddress(rs.getString("address"));
                plant.setDesignCapacity(rs.getBigDecimal("design_capacity"));
                plant.setActualCapacity(rs.getBigDecimal("actual_capacity"));
                plant.setServicePopulation(rs.getInt("service_population"));
                plant.setContactPerson(rs.getString("contact_person"));
                plant.setContactPhone(rs.getString("contact_phone"));
                plant.setStatus(rs.getInt("status"));
                plant.setBuildDate(rs.getString("build_date"));
                plant.setCreateTime(rs.getString("create_time"));
                plant.setRegionName(rs.getString("region_name"));
                plant.setSourceName(rs.getString("source_name"));
                list.add(plant);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 按区域分页查询（跨表关联）
     */
    public List<WaterPlant> findByRegionAndPage(Integer regionId, int pageNum, int pageSize) throws Exception {
        String sql = "SELECT wp.*, r.region_name, ws.source_name " +
                     "FROM water_plants wp " +
                     "LEFT JOIN regions r ON wp.region_id = r.region_id " +
                     "LEFT JOIN water_sources ws ON wp.source_id = ws.source_id " +
                     "WHERE wp.region_id = ? " +
                     "ORDER BY wp.plant_id DESC LIMIT ?, ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, regionId);
            pstmt.setInt(2, (pageNum - 1) * pageSize);
            pstmt.setInt(3, pageSize);
            
            rs = pstmt.executeQuery();
            
            List<WaterPlant> list = new ArrayList<>();
            while (rs.next()) {
                WaterPlant plant = new WaterPlant();
                plant.setPlantId(rs.getInt("plant_id"));
                plant.setPlantName(rs.getString("plant_name"));
                plant.setPlantCode(rs.getString("plant_code"));
                plant.setRegionId(rs.getInt("region_id"));
                plant.setSourceId(rs.getInt("source_id"));
                plant.setAddress(rs.getString("address"));
                plant.setDesignCapacity(rs.getBigDecimal("design_capacity"));
                plant.setActualCapacity(rs.getBigDecimal("actual_capacity"));
                plant.setServicePopulation(rs.getInt("service_population"));
                plant.setContactPerson(rs.getString("contact_person"));
                plant.setContactPhone(rs.getString("contact_phone"));
                plant.setStatus(rs.getInt("status"));
                plant.setBuildDate(rs.getString("build_date"));
                plant.setCreateTime(rs.getString("create_time"));
                plant.setRegionName(rs.getString("region_name"));
                plant.setSourceName(rs.getString("source_name"));
                list.add(plant);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public int insert(WaterPlant plant) throws Exception {
        String sql = "INSERT INTO water_plants(plant_name, plant_code, region_id, source_id, address, " +
                     "design_capacity, service_population, contact_person, contact_phone) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, plant.getPlantName());
            pstmt.setString(2, plant.getPlantCode());
            pstmt.setInt(3, plant.getRegionId());
            pstmt.setInt(4, plant.getSourceId());
            pstmt.setString(5, plant.getAddress());
            pstmt.setBigDecimal(6, plant.getDesignCapacity());
            pstmt.setInt(7, plant.getServicePopulation());
            pstmt.setString(8, plant.getContactPerson());
            pstmt.setString(9, plant.getContactPhone());
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int update(WaterPlant plant) throws Exception {
        String sql = "UPDATE water_plants SET plant_name=?, plant_code=?, region_id=?, source_id=?, " +
                     "address=?, design_capacity=?, actual_capacity=?, service_population=?, " +
                     "contact_person=?, contact_phone=?, status=? WHERE plant_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, plant.getPlantName());
            pstmt.setString(2, plant.getPlantCode());
            pstmt.setInt(3, plant.getRegionId());
            pstmt.setInt(4, plant.getSourceId());
            pstmt.setString(5, plant.getAddress());
            pstmt.setBigDecimal(6, plant.getDesignCapacity());
            pstmt.setBigDecimal(7, plant.getActualCapacity());
            pstmt.setInt(8, plant.getServicePopulation());
            pstmt.setString(9, plant.getContactPerson());
            pstmt.setString(10, plant.getContactPhone());
            pstmt.setInt(11, plant.getStatus());
            pstmt.setInt(12, plant.getPlantId());
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int delete(Integer id) throws Exception {
        String sql = "DELETE FROM water_plants WHERE plant_id=?";
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
    public WaterPlant findById(Integer id) throws Exception {
        String sql = "SELECT wp.*, r.region_name, ws.source_name " +
                     "FROM water_plants wp " +
                     "LEFT JOIN regions r ON wp.region_id = r.region_id " +
                     "LEFT JOIN water_sources ws ON wp.source_id = ws.source_id " +
                     "WHERE wp.plant_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                WaterPlant plant = new WaterPlant();
                plant.setPlantId(rs.getInt("plant_id"));
                plant.setPlantName(rs.getString("plant_name"));
                plant.setPlantCode(rs.getString("plant_code"));
                plant.setRegionId(rs.getInt("region_id"));
                plant.setSourceId(rs.getInt("source_id"));
                plant.setAddress(rs.getString("address"));
                plant.setDesignCapacity(rs.getBigDecimal("design_capacity"));
                plant.setActualCapacity(rs.getBigDecimal("actual_capacity"));
                plant.setServicePopulation(rs.getInt("service_population"));
                plant.setContactPerson(rs.getString("contact_person"));
                plant.setContactPhone(rs.getString("contact_phone"));
                plant.setStatus(rs.getInt("status"));
                plant.setBuildDate(rs.getString("build_date"));
                plant.setCreateTime(rs.getString("create_time"));
                plant.setRegionName(rs.getString("region_name"));
                plant.setSourceName(rs.getString("source_name"));
                return plant;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public List<WaterPlant> findAll() throws Exception {
        String sql = "SELECT wp.*, r.region_name, ws.source_name " +
                     "FROM water_plants wp " +
                     "LEFT JOIN regions r ON wp.region_id = r.region_id " +
                     "LEFT JOIN water_sources ws ON wp.source_id = ws.source_id " +
                     "ORDER BY wp.plant_id DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<WaterPlant> list = new ArrayList<>();
            while (rs.next()) {
                WaterPlant plant = new WaterPlant();
                plant.setPlantId(rs.getInt("plant_id"));
                plant.setPlantName(rs.getString("plant_name"));
                plant.setPlantCode(rs.getString("plant_code"));
                plant.setRegionId(rs.getInt("region_id"));
                plant.setSourceId(rs.getInt("source_id"));
                plant.setAddress(rs.getString("address"));
                plant.setDesignCapacity(rs.getBigDecimal("design_capacity"));
                plant.setActualCapacity(rs.getBigDecimal("actual_capacity"));
                plant.setServicePopulation(rs.getInt("service_population"));
                plant.setContactPerson(rs.getString("contact_person"));
                plant.setContactPhone(rs.getString("contact_phone"));
                plant.setStatus(rs.getInt("status"));
                plant.setBuildDate(rs.getString("build_date"));
                plant.setCreateTime(rs.getString("create_time"));
                plant.setRegionName(rs.getString("region_name"));
                plant.setSourceName(rs.getString("source_name"));
                list.add(plant);
            }
            return list;
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }
    
    @Override
    public List<WaterPlant> findByPage(int pageNum, int pageSize) throws Exception {
        String sql = "SELECT wp.*, r.region_name, ws.source_name " +
                     "FROM water_plants wp " +
                     "LEFT JOIN regions r ON wp.region_id = r.region_id " +
                     "LEFT JOIN water_sources ws ON wp.source_id = ws.source_id " +
                     "ORDER BY wp.plant_id DESC LIMIT ?, ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (pageNum - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            
            rs = pstmt.executeQuery();
            
            List<WaterPlant> list = new ArrayList<>();
            while (rs.next()) {
                WaterPlant plant = new WaterPlant();
                plant.setPlantId(rs.getInt("plant_id"));
                plant.setPlantName(rs.getString("plant_name"));
                plant.setPlantCode(rs.getString("plant_code"));
                plant.setRegionId(rs.getInt("region_id"));
                plant.setSourceId(rs.getInt("source_id"));
                plant.setAddress(rs.getString("address"));
                plant.setDesignCapacity(rs.getBigDecimal("design_capacity"));
                plant.setActualCapacity(rs.getBigDecimal("actual_capacity"));
                plant.setServicePopulation(rs.getInt("service_population"));
                plant.setContactPerson(rs.getString("contact_person"));
                plant.setContactPhone(rs.getString("contact_phone"));
                plant.setStatus(rs.getInt("status"));
                plant.setBuildDate(rs.getString("build_date"));
                plant.setCreateTime(rs.getString("create_time"));
                plant.setRegionName(rs.getString("region_name"));
                plant.setSourceName(rs.getString("source_name"));
                list.add(plant);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM water_plants";
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
