package com.ruralwater.service;

import com.ruralwater.dao.WaterQualityStandardDAO;
import com.ruralwater.entity.WaterQualityStandard;

import java.util.List;

/**
 * 水质检测标准服务层
 */
public class WaterQualityStandardService {
    
    private WaterQualityStandardDAO standardDAO = new WaterQualityStandardDAO();
    
    /**
     * 根据类别查询标准
     */
    public List<WaterQualityStandard> getStandardsByCategory(String category) throws Exception {
        return standardDAO.findByCategory(category);
    }
    
    /**
     * 获取所有激活的标准
     */
    public List<WaterQualityStandard> getActiveStandards() throws Exception {
        return standardDAO.findActiveStandards();
    }
    
    /**
     * 获取所有标准（包括未激活的）
     */
    public List<WaterQualityStandard> findAll() throws Exception {
        return standardDAO.findAll();
    }
    
    /**
     * 关键字搜索标准
     */
    public List<WaterQualityStandard> searchStandards(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty()) {
            return standardDAO.findAll();
        }
        return standardDAO.findByKeyword(keyword);
    }
    
    /**
     * 获取标准详情
     */
    public WaterQualityStandard getStandardById(Integer standardId) throws Exception {
        if (standardId == null || standardId <= 0) {
            throw new IllegalArgumentException("标准 ID 无效");
        }
        return standardDAO.findById(standardId);
    }
    
    /**
     * 添加检测标准
     */
    public void addStandard(WaterQualityStandard standard) throws Exception {
        if (standard == null) {
            throw new IllegalArgumentException("标准信息不能为空");
        }
        
        // 验证必填字段
        if (standard.getItemName() == null || standard.getItemName().trim().isEmpty()) {
            throw new IllegalArgumentException("检测项名称不能为空");
        }
        if (standard.getItemCode() == null || standard.getItemCode().trim().isEmpty()) {
            throw new IllegalArgumentException("检测项编码不能为空");
        }
        if (standard.getCategory() == null || standard.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("检测项类别不能为空");
        }
        
        standardDAO.save(standard);
    }
    
    /**
     * 更新检测标准
     */
    public void updateStandard(WaterQualityStandard standard) throws Exception {
        if (standard == null || standard.getStandardId() == null) {
            throw new IllegalArgumentException("标准信息无效");
        }
        
        // 验证必填字段
        if (standard.getItemName() == null || standard.getItemName().trim().isEmpty()) {
            throw new IllegalArgumentException("检测项名称不能为空");
        }
        if (standard.getItemCode() == null || standard.getItemCode().trim().isEmpty()) {
            throw new IllegalArgumentException("检测项编码不能为空");
        }
        if (standard.getCategory() == null || standard.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("检测项类别不能为空");
        }
        
        standardDAO.update(standard);
    }
    
    /**
     * 删除检测标准
     */
    public void deleteStandard(Integer standardId) throws Exception {
        if (standardId == null || standardId <= 0) {
            throw new IllegalArgumentException("标准 ID 无效");
        }
        
        // TODO: 检查是否有检测记录使用该标准
        
        standardDAO.delete(standardId);
    }
    
    /**
     * 获取标准总数
     */
    public int getTotalCount() throws Exception {
        return standardDAO.getCount();
    }
}
