package com.ruralwater.dao;

import com.ruralwater.entity.Equipment;
import com.ruralwater.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备 DAO 实现类
 */
public class EquipmentDAO implements BaseDAO<Equipment> {
    
    /**
     * 根据水厂 ID 查询设备列表
     */
    public List<Equipment> findByPlantId(Integer plantId) throws Exception {
        StringBuilder sql = new StringBuilder(
            "SELECT e.*, wp.plant_name " +
            "FROM equipments e " +
            "LEFT JOIN water_plants wp ON e.plant_id = wp.plant_id " +
            "WHERE 1=1"
        );
        
        List<Object> params = new ArrayList<>();
        
        if (plantId != null && plantId > 0) {
            sql.append(" AND e.plant_id = ?");
            params.add(plantId);
        }
        
        sql.append(" ORDER BY e.equipment_name");
        
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
            
            List<Equipment> list = new ArrayList<>();
            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setEquipmentId(rs.getInt("equipment_id"));
                equipment.setEquipmentName(rs.getString("equipment_name"));
                equipment.setEquipmentType(rs.getString("equipment_type"));
                equipment.setModel(rs.getString("model"));
                equipment.setPlantId(rs.getInt("plant_id"));
                equipment.setPurchaseDate(rs.getString("purchase_date"));
                equipment.setWarrantyPeriod(rs.getInt("warranty_period"));
                equipment.setStatus(rs.getString("status"));
                equipment.setLastMaintenanceDate(rs.getString("last_maintenance_date"));
                equipment.setNextMaintenanceDate(rs.getString("next_maintenance_date"));
                equipment.setRemark(rs.getString("remark"));
                equipment.setCreateTime(rs.getString("create_time"));
                equipment.setPlantName(rs.getString("plant_name"));
                list.add(equipment);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 按状态查询设备
     */
    public List<Equipment> findByStatus(String status) throws Exception {
        String sql = "SELECT e.*, wp.plant_name " +
                     "FROM equipments e " +
                     "LEFT JOIN water_plants wp ON e.plant_id = wp.plant_id " +
                     "WHERE e.status = ? " +
                     "ORDER BY e.equipment_name";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            
            rs = pstmt.executeQuery();
            
            List<Equipment> list = new ArrayList<>();
            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setEquipmentId(rs.getInt("equipment_id"));
                equipment.setEquipmentName(rs.getString("equipment_name"));
                equipment.setEquipmentType(rs.getString("equipment_type"));
                equipment.setModel(rs.getString("model"));
                equipment.setPlantId(rs.getInt("plant_id"));
                equipment.setPurchaseDate(rs.getString("purchase_date"));
                equipment.setWarrantyPeriod(rs.getInt("warranty_period"));
                equipment.setStatus(rs.getString("status"));
                equipment.setLastMaintenanceDate(rs.getString("last_maintenance_date"));
                equipment.setNextMaintenanceDate(rs.getString("next_maintenance_date"));
                equipment.setRemark(rs.getString("remark"));
                equipment.setCreateTime(rs.getString("create_time"));
                equipment.setPlantName(rs.getString("plant_name"));
                list.add(equipment);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 统计各状态设备数量
     */
    public int countByStatus(String status) throws Exception {
        String sql = "SELECT COUNT(*) as count FROM equipments WHERE status = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public int insert(Equipment equipment) throws Exception {
        String sql = "INSERT INTO equipments(equipment_name, equipment_type, model, plant_id, " +
                     "purchase_date, warranty_period, status, last_maintenance_date, " +
                     "next_maintenance_date, remark) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, equipment.getEquipmentName());
            pstmt.setString(2, equipment.getEquipmentType());
            pstmt.setString(3, equipment.getModel());
            pstmt.setInt(4, equipment.getPlantId());
            pstmt.setString(5, equipment.getPurchaseDate());
            pstmt.setInt(6, equipment.getWarrantyPeriod());
            pstmt.setString(7, equipment.getStatus() != null ? equipment.getStatus() : "normal");
            pstmt.setString(8, equipment.getLastMaintenanceDate());
            pstmt.setString(9, equipment.getNextMaintenanceDate());
            pstmt.setString(10, equipment.getRemark() != null ? equipment.getRemark() : "");
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int update(Equipment equipment) throws Exception {
        String sql = "UPDATE equipments SET equipment_name=?, equipment_type=?, model=?, " +
                     "plant_id=?, purchase_date=?, warranty_period=?, status=?, " +
                     "last_maintenance_date=?, next_maintenance_date=?, remark=? " +
                     "WHERE equipment_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, equipment.getEquipmentName());
            pstmt.setString(2, equipment.getEquipmentType());
            pstmt.setString(3, equipment.getModel());
            pstmt.setInt(4, equipment.getPlantId());
            pstmt.setString(5, equipment.getPurchaseDate());
            pstmt.setInt(6, equipment.getWarrantyPeriod());
            pstmt.setString(7, equipment.getStatus());
            pstmt.setString(8, equipment.getLastMaintenanceDate());
            pstmt.setString(9, equipment.getNextMaintenanceDate());
            pstmt.setString(10, equipment.getRemark());
            pstmt.setInt(11, equipment.getEquipmentId());
            
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    @Override
    public int delete(Integer id) throws Exception {
        String sql = "DELETE FROM equipments WHERE equipment_id=?";
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
    public Equipment findById(Integer id) throws Exception {
        String sql = "SELECT e.*, wp.plant_name " +
                     "FROM equipments e " +
                     "LEFT JOIN water_plants wp ON e.plant_id = wp.plant_id " +
                     "WHERE e.equipment_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setEquipmentId(rs.getInt("equipment_id"));
                equipment.setEquipmentName(rs.getString("equipment_name"));
                equipment.setEquipmentType(rs.getString("equipment_type"));
                equipment.setModel(rs.getString("model"));
                equipment.setPlantId(rs.getInt("plant_id"));
                equipment.setPurchaseDate(rs.getString("purchase_date"));
                equipment.setWarrantyPeriod(rs.getInt("warranty_period"));
                equipment.setStatus(rs.getString("status"));
                equipment.setLastMaintenanceDate(rs.getString("last_maintenance_date"));
                equipment.setNextMaintenanceDate(rs.getString("next_maintenance_date"));
                equipment.setRemark(rs.getString("remark"));
                equipment.setCreateTime(rs.getString("create_time"));
                equipment.setPlantName(rs.getString("plant_name"));
                return equipment;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public List<Equipment> findAll() throws Exception {
        return findByPlantId(null);
    }
    
    @Override
    public List<Equipment> findByPage(int pageNum, int pageSize) throws Exception {
        String sql = "SELECT e.*, wp.plant_name " +
                     "FROM equipments e " +
                     "LEFT JOIN water_plants wp ON e.plant_id = wp.plant_id " +
                     "ORDER BY e.equipment_name " +
                     "LIMIT ?, ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (pageNum - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            
            rs = pstmt.executeQuery();
            
            List<Equipment> list = new ArrayList<>();
            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setEquipmentId(rs.getInt("equipment_id"));
                equipment.setEquipmentName(rs.getString("equipment_name"));
                equipment.setEquipmentType(rs.getString("equipment_type"));
                equipment.setModel(rs.getString("model"));
                equipment.setPlantId(rs.getInt("plant_id"));
                equipment.setPurchaseDate(rs.getString("purchase_date"));
                equipment.setWarrantyPeriod(rs.getInt("warranty_period"));
                equipment.setStatus(rs.getString("status"));
                equipment.setLastMaintenanceDate(rs.getString("last_maintenance_date"));
                equipment.setNextMaintenanceDate(rs.getString("next_maintenance_date"));
                equipment.setRemark(rs.getString("remark"));
                equipment.setCreateTime(rs.getString("create_time"));
                equipment.setPlantName(rs.getString("plant_name"));
                list.add(equipment);
            }
            return list;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    @Override
    public int getCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM equipments";
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
