package com.ruralwater.service;

import com.ruralwater.dao.WaterQualityRecordDAO;
import com.ruralwater.entity.WaterQualityDetail;
import com.ruralwater.entity.WaterQualityRecord;

import java.util.List;

/**
 * 水质检测记录服务层（业务逻辑层）
 */
public class WaterQualityService {
    
    private WaterQualityRecordDAO recordDAO = new WaterQualityRecordDAO();
    
    /**
     * 分页查询检测记录（支持多条件）
     */
    public List<WaterQualityRecord> getRecordsByCondition(Integer plantId, String startDate, 
                                                           String endDate, String conclusion, 
                                                           int pageNum, int pageSize) throws Exception {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        
        return recordDAO.findByCondition(plantId, startDate, endDate, conclusion, pageNum, pageSize);
    }
    
    /**
     * 获取检测记录详情
     */
    public WaterQualityRecord getRecordById(Integer recordId) throws Exception {
        if (recordId == null || recordId <= 0) {
            throw new IllegalArgumentException("记录 ID 无效");
        }
        
        return recordDAO.findById(recordId);
    }
    
    /**
     * 添加检测记录（带事务处理）
     */
    public void addRecord(WaterQualityRecord record, List<WaterQualityDetail> details) throws Exception {
        if (record == null) {
            throw new IllegalArgumentException("检测记录不能为空");
        }
        if (record.getPlantId() == null || record.getPlantId() <= 0) {
            throw new IllegalArgumentException("水厂 ID 无效");
        }
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("检测详情不能为空");
        }
        
        // 实际项目中应该使用事务处理，同时插入记录和详情
        
        recordDAO.insert(record);
        
        // TODO: 插入检测详情
        // detailDAO.insert(detail);
    }
    
    /**
     * 审核检测记录
     */
    public void approveRecord(Integer recordId, Integer reviewerId, String conclusion) throws Exception {
        if (recordId == null || recordId <= 0) {
            throw new IllegalArgumentException("记录 ID 无效");
        }
        if (reviewerId == null || reviewerId <= 0) {
            throw new IllegalArgumentException("审核人 ID 无效");
        }
        if (!"qualified".equals(conclusion) && !"unqualified".equals(conclusion)) {
            throw new IllegalArgumentException("结论无效");
        }
        
        recordDAO.approveRecord(recordId, reviewerId, conclusion);
    }
    
    /**
     * 统计符合条件的记录数
     */
    public int getCountByCondition(Integer plantId, String startDate, String endDate, String conclusion) throws Exception {
        return recordDAO.getCountByCondition(plantId, startDate, endDate, conclusion);
    }
}
