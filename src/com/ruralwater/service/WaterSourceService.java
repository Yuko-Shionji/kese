package com.ruralwater.service;

import com.ruralwater.dao.WaterSourceDAO;
import com.ruralwater.entity.WaterSource;

import java.util.List;

/**
 * 水源服务层
 */
public class WaterSourceService {
    
    private WaterSourceDAO waterSourceDAO = new WaterSourceDAO();
    
    /**
     * 根据类型查询水源
     */
    public List<WaterSource> getSourcesByType(String sourceType) throws Exception {
        return waterSourceDAO.findByType(sourceType);
    }
    
    /**
     * 根据区域查询水源
     */
    public List<WaterSource> getSourcesByRegion(Integer regionId) throws Exception {
        return waterSourceDAO.findByRegionId(regionId);
    }
    
    /**
     * 关键字搜索水源
     */
    public List<WaterSource> searchSources(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty()) {
            return waterSourceDAO.findAll();
        }
        return waterSourceDAO.findByKeyword(keyword);
    }
    
    /**
     * 获取水源详情
     */
    public WaterSource getSourceById(Integer sourceId) throws Exception {
        if (sourceId == null || sourceId <= 0) {
            throw new IllegalArgumentException("水源 ID 无效");
        }
        return waterSourceDAO.findById(sourceId);
    }
    
    /**
     * 添加水源
     */
    public void addSource(WaterSource source) throws Exception {
        if (source == null) {
            throw new IllegalArgumentException("水源信息不能为空");
        }
        
        // 验证必填字段
        if (source.getSourceName() == null || source.getSourceName().trim().isEmpty()) {
            throw new IllegalArgumentException("水源名称不能为空");
        }
        if (source.getSourceType() == null || source.getSourceType().trim().isEmpty()) {
            throw new IllegalArgumentException("水源类型不能为空");
        }
        if (source.getRegionId() == null || source.getRegionId() <= 0) {
            throw new IllegalArgumentException("所属区域不能为空");
        }
        
        waterSourceDAO.save(source);
    }
    
    /**
     * 更新水源
     */
    public void updateSource(WaterSource source) throws Exception {
        if (source == null || source.getSourceId() == null) {
            throw new IllegalArgumentException("水源信息无效");
        }
        
        // 验证必填字段
        if (source.getSourceName() == null || source.getSourceName().trim().isEmpty()) {
            throw new IllegalArgumentException("水源名称不能为空");
        }
        if (source.getSourceType() == null || source.getSourceType().trim().isEmpty()) {
            throw new IllegalArgumentException("水源类型不能为空");
        }
        if (source.getRegionId() == null || source.getRegionId() <= 0) {
            throw new IllegalArgumentException("所属区域不能为空");
        }
        
        waterSourceDAO.update(source);
    }
    
    /**
     * 删除水源
     */
    public void deleteSource(Integer sourceId) throws Exception {
        if (sourceId == null || sourceId <= 0) {
            throw new IllegalArgumentException("水源 ID 无效");
        }
        
        // TODO: 检查是否有水厂使用该水源
        
        waterSourceDAO.delete(sourceId);
    }
    
    /**
     * 获取水源总数
     */
    public int getTotalCount() throws Exception {
        return waterSourceDAO.getCount();
    }
}
