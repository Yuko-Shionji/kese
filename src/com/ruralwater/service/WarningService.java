package com.ruralwater.service;

import com.ruralwater.dao.WarningDAO;
import com.ruralwater.entity.Warning;

import java.util.List;

/**
 * 预警信息服务层
 */
public class WarningService {
    
    private WarningDAO warningDAO = new WarningDAO();
    
    /**
     * 分页查询预警信息
     */
    public List<Warning> getWarningsByCondition(String warningType, String warningLevel, 
                                                String status, Integer plantId, 
                                                int pageNum, int pageSize) throws Exception {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        
        return warningDAO.findByCondition(warningType, warningLevel, status, plantId, pageNum, pageSize);
    }
    
    /**
     * 获取预警详情
     */
    public Warning getWarningById(Integer warningId) throws Exception {
        if (warningId == null || warningId <= 0) {
            throw new IllegalArgumentException("预警 ID 无效");
        }
        return warningDAO.findById(warningId);
    }
    
    /**
     * 添加预警
     */
    public void addWarning(Warning warning) throws Exception {
        if (warning == null) {
            throw new IllegalArgumentException("预警信息不能为空");
        }
        if (warning.getPlantId() == null || warning.getPlantId() <= 0) {
            throw new IllegalArgumentException("水厂 ID 无效");
        }
        warningDAO.insert(warning);
    }
    
    /**
     * 处理预警
     */
    public void handleWarning(Integer warningId, Integer handlerId, String handleResult, String status) throws Exception {
        if (warningId == null || warningId <= 0) {
            throw new IllegalArgumentException("预警 ID 无效");
        }
        if (handlerId == null || handlerId <= 0) {
            throw new IllegalArgumentException("处理人 ID 无效");
        }
        warningDAO.handleWarning(warningId, handlerId, handleResult, status);
    }
    
    /**
     * 删除预警
     */
    public void deleteWarning(Integer warningId) throws Exception {
        if (warningId == null || warningId <= 0) {
            throw new IllegalArgumentException("预警 ID 无效");
        }
        warningDAO.delete(warningId);
    }
    
    /**
     * 统计预警总数
     */
    public int getTotalCount() throws Exception {
        return warningDAO.getCount();
    }
    
    /**
     * 统计活跃预警数（待处理）
     */
    public int getActiveWarnings() throws Exception {
        return warningDAO.getCountByCondition(null, null, "active", null);
    }
    
    /**
     * 统计已处理预警数
     */
    public int getProcessedWarnings() throws Exception {
        return warningDAO.getCountByCondition(null, null, "processed", null);
    }
}
