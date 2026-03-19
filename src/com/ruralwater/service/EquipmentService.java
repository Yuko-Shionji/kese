package com.ruralwater.service;

import com.ruralwater.dao.EquipmentDAO;
import com.ruralwater.entity.Equipment;

import java.util.List;

/**
 * 设备服务层
 */
public class EquipmentService {
    
    private EquipmentDAO equipmentDAO = new EquipmentDAO();
    
    /**
     * 分页查询设备列表
     */
    public List<Equipment> getEquipmentsByPage(int pageNum, int pageSize) throws Exception {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        return equipmentDAO.findByPage(pageNum, pageSize);
    }
    
    /**
     * 根据水厂 ID 查询设备列表
     */
    public List<Equipment> getEquipmentsByPlantId(Integer plantId) throws Exception {
        return equipmentDAO.findByPlantId(plantId);
    }
    
    /**
     * 根据状态查询设备列表
     */
    public List<Equipment> getEquipmentsByStatus(String status) throws Exception {
        return equipmentDAO.findByStatus(status);
    }
    
    /**
     * 获取设备详情
     */
    public Equipment getEquipmentById(Integer equipmentId) throws Exception {
        if (equipmentId == null || equipmentId <= 0) {
            throw new IllegalArgumentException("设备 ID 无效");
        }
        return equipmentDAO.findById(equipmentId);
    }
    
    /**
     * 添加设备
     */
    public void addEquipment(Equipment equipment) throws Exception {
        if (equipment == null) {
            throw new IllegalArgumentException("设备信息不能为空");
        }
        if (equipment.getEquipmentName() == null || equipment.getEquipmentName().trim().isEmpty()) {
            throw new IllegalArgumentException("设备名称不能为空");
        }
        equipmentDAO.insert(equipment);
    }
    
    /**
     * 更新设备
     */
    public void updateEquipment(Equipment equipment) throws Exception {
        if (equipment == null || equipment.getEquipmentId() == null || equipment.getEquipmentId() <= 0) {
            throw new IllegalArgumentException("设备信息无效");
        }
        equipmentDAO.update(equipment);
    }
    
    /**
     * 删除设备
     */
    public void deleteEquipment(Integer equipmentId) throws Exception {
        if (equipmentId == null || equipmentId <= 0) {
            throw new IllegalArgumentException("设备 ID 无效");
        }
        equipmentDAO.delete(equipmentId);
    }
    
    /**
     * 统计设备总数
     */
    public int getTotalCount() throws Exception {
        return equipmentDAO.getCount();
    }
    
    /**
     * 统计各状态设备数量
     */
    public int countByStatus(String status) throws Exception {
        return equipmentDAO.countByStatus(status);
    }
    
    /**
     * 统计正常设备数量
     */
    public int countNormalEquipments() throws Exception {
        return countByStatus("normal");
    }
    
    /**
     * 统计维护中设备数量
     */
    public int countMaintenanceEquipments() throws Exception {
        return countByStatus("maintenance");
    }
    
    /**
     * 统计故障设备数量
     */
    public int countFaultyEquipments() throws Exception {
        return countByStatus("faulty");
    }
}
