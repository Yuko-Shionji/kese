package com.ruralwater.service;

import com.ruralwater.dao.WaterQualityDetailDAO;
import com.ruralwater.entity.WaterQualityDetail;

import java.util.List;

/**
 * 水质检测详情服务层
 */
public class WaterQualityDetailService {
    
    private WaterQualityDetailDAO detailDAO = new WaterQualityDetailDAO();
    
    /**
     * 根据记录 ID 查询所有详情
     */
    public List<WaterQualityDetail> getDetailsByRecordId(Integer recordId) throws Exception {
        if (recordId == null || recordId <= 0) {
            throw new IllegalArgumentException("记录 ID 无效");
        }
        return detailDAO.findByRecordId(recordId);
    }
    
    /**
     * 批量添加检测详情（带事务处理）
     */
    public int addDetails(List<WaterQualityDetail> details) throws Exception {
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("检测详情不能为空");
        }
        
        // 验证数据
        for (WaterQualityDetail detail : details) {
            if (detail.getRecordId() == null || detail.getRecordId() <= 0) {
                throw new IllegalArgumentException("记录 ID 无效");
            }
            if (detail.getStandardId() == null || detail.getStandardId() <= 0) {
                throw new IllegalArgumentException("标准 ID 无效");
            }
            if (detail.getMeasuredValue() == null) {
                throw new IllegalArgumentException("测量值不能为空");
            }
        }
        
        return detailDAO.batchInsert(details);
    }
    
    /**
     * 更新检测详情
     */
    public void updateDetail(WaterQualityDetail detail) throws Exception {
        if (detail == null || detail.getDetailId() == null || detail.getDetailId() <= 0) {
            throw new IllegalArgumentException("检测详情无效");
        }
        detailDAO.update(detail);
    }
    
    /**
     * 删除检测详情
     */
    public void deleteDetail(Integer detailId) throws Exception {
        if (detailId == null || detailId <= 0) {
            throw new IllegalArgumentException("检测详情 ID 无效");
        }
        detailDAO.delete(detailId);
    }
    
    /**
     * 统计合格项数量
     */
    public int countQualifiedItems(Integer recordId) throws Exception {
        List<WaterQualityDetail> details = getDetailsByRecordId(recordId);
        int count = 0;
        for (WaterQualityDetail detail : details) {
            if (detail.getIsQualified() != null && detail.getIsQualified() == 1) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 统计不合格项数量
     */
    public int countUnqualifiedItems(Integer recordId) throws Exception {
        List<WaterQualityDetail> details = getDetailsByRecordId(recordId);
        int count = 0;
        for (WaterQualityDetail detail : details) {
            if (detail.getIsQualified() != null && detail.getIsQualified() == 0) {
                count++;
            }
        }
        return count;
    }
}
