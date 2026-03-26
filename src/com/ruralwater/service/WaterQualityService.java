package com.ruralwater.service;

import com.ruralwater.dao.WaterQualityRecordDAO;
import com.ruralwater.entity.WaterQualityDetail;
import com.ruralwater.entity.WaterQualityRecord;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 水质检测记录服务层（业务逻辑层）
 * 增强版：添加统计分析和数据验证功能
 */
public class WaterQualityService {
    
    private WaterQualityRecordDAO recordDAO = new WaterQualityRecordDAO();
    private WaterQualityDetailService detailService = new WaterQualityDetailService();
    
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
     * 获取检测记录详情（包含检测项详情）
     */
    public Map<String, Object> getRecordWithDetails(Integer recordId) throws Exception {
        if (recordId == null || recordId <= 0) {
            throw new IllegalArgumentException("记录 ID 无效");
        }
        
        Map<String, Object> result = new HashMap<>();
        WaterQualityRecord record = recordDAO.findById(recordId);
        List<WaterQualityDetail> details = detailService.getDetailsByRecordId(recordId);
        
        result.put("record", record);
        result.put("details", details);
        result.put("qualifiedCount", detailService.countQualifiedItems(recordId));
        result.put("unqualifiedCount", detailService.countUnqualifiedItems(recordId));
        
        return result;
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
        System.out.println("[WaterQualityService] 开始添加记录...");
        
        if (record == null) {
            throw new IllegalArgumentException("检测记录不能为空");
        }
        if (record.getPlantId() == null || record.getPlantId() <= 0) {
            throw new IllegalArgumentException("水厂 ID 无效");
        }
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("检测详情不能为空");
        }
        
        System.out.println("[WaterQualityService] 验证通过，准备开启事务...");
        
        // 开启事务处理
        java.sql.Connection conn = null;
        try {
            conn = com.ruralwater.util.DBUtil.getConnection();
            System.out.println("[WaterQualityService] 数据库连接获取成功");
            
            com.ruralwater.util.DBUtil.beginTransaction(conn);
            System.out.println("[WaterQualityService] 事务开启成功");
            
            // 插入主记录
            int recordResult = recordDAO.insertWithConnection(record, conn);
            System.out.println("[WaterQualityService] 主记录插入成功，影响行数：" + recordResult);
            
            // 获取生成的记录 ID
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            Integer recordId = null;
            if (rs.next()) {
                recordId = rs.getInt(1);
                System.out.println("[WaterQualityService] 生成的记录 ID: " + recordId);
            }
            rs.close();
            stmt.close();
            
            if (recordId != null) {
                // 为所有详情设置记录 ID
                for (WaterQualityDetail detail : details) {
                    detail.setRecordId(recordId);
                }
                
                System.out.println("[WaterQualityService] 准备批量插入详情...");
                // 批量插入详情
                detailService.addDetails(details);
                System.out.println("[WaterQualityService] 详情插入成功");
            }
            
            // 提交事务
            com.ruralwater.util.DBUtil.commit(conn);
            System.out.println("[WaterQualityService] 事务提交成功");
        } catch (Exception e) {
            System.err.println("[WaterQualityService] 发生异常：" + e.getMessage());
            e.printStackTrace();
            // 回滚事务
            if (conn != null) {
                com.ruralwater.util.DBUtil.rollback(conn);
                System.out.println("[WaterQualityService] 事务已回滚");
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (Exception e) {}
            }
        }
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
    
    /**
     * 统计检测记录总数
     */
    public int getTotalCount() throws Exception {
        return recordDAO.getCount();
    }
    
    /**
     * 统计分析：计算合格率
     */
    public Map<String, Object> calculatePassRate(Integer plantId, String startDate, String endDate) throws Exception {
        Map<String, Object> result = new HashMap<>();
        
        List<WaterQualityRecord> records = getRecordsByCondition(plantId, startDate, endDate, null, 1, 1000);
        
        int totalRecords = records.size();
        int qualifiedCount = 0;
        int unqualifiedCount = 0;
        
        for (WaterQualityRecord record : records) {
            if ("qualified".equals(record.getConclusion())) {
                qualifiedCount++;
            } else if ("unqualified".equals(record.getConclusion())) {
                unqualifiedCount++;
            }
        }
        
        BigDecimal passRate = totalRecords > 0 ? 
            new BigDecimal(qualifiedCount).multiply(new BigDecimal("100"))
                .divide(new BigDecimal(totalRecords), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        
        result.put("totalRecords", totalRecords);
        result.put("qualifiedCount", qualifiedCount);
        result.put("unqualifiedCount", unqualifiedCount);
        result.put("passRate", passRate.setScale(2, BigDecimal.ROUND_HALF_UP));
        
        return result;
    }
    
    /**
     * 获取最近检测记录（最新 N 条）
     */
    public List<WaterQualityRecord> getRecentRecords(int count) throws Exception {
        return recordDAO.findByPage(1, count);
    }
    
    /**
     * 删除检测记录
     */
    public void deleteRecord(Integer recordId) throws Exception {
        if (recordId == null || recordId <= 0) {
            throw new IllegalArgumentException("记录 ID 无效");
        }
        recordDAO.delete(recordId);
    }
    
    /**
     * 更新检测记录
     */
    public void updateRecord(WaterQualityRecord record) throws Exception {
        if (record == null || record.getRecordId() == null || record.getRecordId() <= 0) {
            throw new IllegalArgumentException("记录信息无效");
        }
        recordDAO.update(record);
    }
}
