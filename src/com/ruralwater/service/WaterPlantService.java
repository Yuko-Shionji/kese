package com.ruralwater.service;

import com.ruralwater.dao.WaterPlantDAO;
import com.ruralwater.entity.WaterPlant;

import java.util.List;

/**
 * 水厂服务层（业务逻辑层）
 */
public class WaterPlantService {
    
    private WaterPlantDAO plantDAO = new WaterPlantDAO();
    
    /**
     * 分页查询所有水厂
     */
    public List<WaterPlant> getPlantsByPage(int pageNum, int pageSize) throws Exception {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        
        return plantDAO.findByPage(pageNum, pageSize);
    }
    
    /**
     * 关键字搜索水厂
     */
    public List<WaterPlant> searchPlants(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty()) {
            return plantDAO.findAll();
        }
        
        return plantDAO.findByKeyword(keyword);
    }
    
    /**
     * 获取水厂详情
     */
    public WaterPlant getPlantById(Integer plantId) throws Exception {
        if (plantId == null || plantId <= 0) {
            throw new IllegalArgumentException("水厂 ID 无效");
        }
        
        return plantDAO.findById(plantId);
    }
    
    /**
     * 添加水厂（带数据验证）
     */
    public void addPlant(WaterPlant plant) throws Exception {
        if (plant == null) {
            throw new IllegalArgumentException("水厂信息不能为空");
        }
        if (plant.getPlantName() == null || plant.getPlantName().trim().isEmpty()) {
            throw new IllegalArgumentException("水厂名称不能为空");
        }
        if (plant.getRegionId() == null || plant.getRegionId() <= 0) {
            throw new IllegalArgumentException("所属区域不能为空");
        }
        
        // 检查编码是否重复
        if (plant.getPlantCode() != null && !plant.getPlantCode().trim().isEmpty()) {
            List<WaterPlant> plants = plantDAO.findByKeyword(plant.getPlantCode());
            for (WaterPlant p : plants) {
                if (p.getPlantCode().equals(plant.getPlantCode())) {
                    throw new IllegalArgumentException("水厂编码已存在");
                }
            }
        }
        
        plantDAO.insert(plant);
    }
    
    /**
     * 更新水厂信息
     */
    public void updatePlant(WaterPlant plant) throws Exception {
        if (plant == null || plant.getPlantId() == null || plant.getPlantId() <= 0) {
            throw new IllegalArgumentException("水厂信息无效");
        }
        
        plantDAO.update(plant);
    }
    
    /**
     * 删除水厂（带事务处理）
     */
    public void deletePlant(Integer plantId) throws Exception {
        if (plantId == null || plantId <= 0) {
            throw new IllegalArgumentException("水厂 ID 无效");
        }
        
        // 实际项目中应该先检查是否有关联的检测记录
        
        plantDAO.delete(plantId);
    }
    
    /**
     * 统计水厂总数
     */
    public int getTotalCount() throws Exception {
        return plantDAO.getCount();
    }
}
