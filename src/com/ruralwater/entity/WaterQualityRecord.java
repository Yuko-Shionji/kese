package com.ruralwater.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水质检测记录实体类
 */
public class WaterQualityRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer recordId;
    private Integer plantId;
    private String samplePoint;
    private String sampleTime;
    private Integer testerId;
    private String reviewStatus; // pending, approved, rejected
    private Integer reviewerId;
    private String reviewTime;
    private BigDecimal totalScore;
    private String conclusion; // qualified, unqualified
    private String remark;
    private String createTime;
    
    // 关联字段（非数据库字段）
    private String plantName;
    private String testerName;
    
    public WaterQualityRecord() {}
    
    public WaterQualityRecord(Integer recordId, Integer plantId, String sampleTime) {
        this.recordId = recordId;
        this.plantId = plantId;
        this.sampleTime = sampleTime;
    }
    
    // Getters and Setters
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }
    
    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }
    
    public String getSamplePoint() { return samplePoint; }
    public void setSamplePoint(String samplePoint) { this.samplePoint = samplePoint; }
    
    public String getSampleTime() { return sampleTime; }
    public void setSampleTime(String sampleTime) { this.sampleTime = sampleTime; }
    
    public Integer getTesterId() { return testerId; }
    public void setTesterId(Integer testerId) { this.testerId = testerId; }
    
    public String getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(String reviewStatus) { this.reviewStatus = reviewStatus; }
    
    public Integer getReviewerId() { return reviewerId; }
    public void setReviewerId(Integer reviewerId) { this.reviewerId = reviewerId; }
    
    public String getReviewTime() { return reviewTime; }
    public void setReviewTime(String reviewTime) { this.reviewTime = reviewTime; }
    
    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    
    public String getConclusion() { return conclusion; }
    public void setConclusion(String conclusion) { this.conclusion = conclusion; }
    
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }
    
    public String getTesterName() { return testerName; }
    public void setTesterName(String testerName) { this.testerName = testerName; }
    
    @Override
    public String toString() {
        return "WaterQualityRecord{" +
                "recordId=" + recordId +
                ", plantId=" + plantId +
                ", sampleTime='" + sampleTime + '\'' +
                ", conclusion='" + conclusion + '\'' +
                '}';
    }
}
