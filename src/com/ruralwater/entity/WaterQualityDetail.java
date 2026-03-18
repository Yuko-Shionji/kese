package com.ruralwater.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水质检测详情实体类
 */
public class WaterQualityDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer detailId;
    private Integer recordId;
    private Integer standardId;
    private BigDecimal measuredValue;
    private Integer isQualified;
    private String remark;
    
    // 关联字段（非数据库字段）
    private String itemName;
    private String unit;
    private String itemCode;
    private BigDecimal standardValue;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String category;
    
    public WaterQualityDetail() {}
    
    public WaterQualityDetail(Integer detailId, Integer recordId, Integer standardId) {
        this.detailId = detailId;
        this.recordId = recordId;
        this.standardId = standardId;
    }
    
    // Getters and Setters
    public Integer getDetailId() { return detailId; }
    public void setDetailId(Integer detailId) { this.detailId = detailId; }
    
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }
    
    public Integer getStandardId() { return standardId; }
    public void setStandardId(Integer standardId) { this.standardId = standardId; }
    
    public BigDecimal getMeasuredValue() { return measuredValue; }
    public void setMeasuredValue(BigDecimal measuredValue) { this.measuredValue = measuredValue; }
    
    public Integer getIsQualified() { return isQualified; }
    public void setIsQualified(Integer isQualified) { this.isQualified = isQualified; }
    
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    
    public BigDecimal getStandardValue() { return standardValue; }
    public void setStandardValue(BigDecimal standardValue) { this.standardValue = standardValue; }
    
    public BigDecimal getMinValue() { return minValue; }
    public void setMinValue(BigDecimal minValue) { this.minValue = minValue; }
    
    public BigDecimal getMaxValue() { return maxValue; }
    public void setMaxValue(BigDecimal maxValue) { this.maxValue = maxValue; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public String toString() {
        return "WaterQualityDetail{" +
                "detailId=" + detailId +
                ", recordId=" + recordId +
                ", standardId=" + standardId +
                ", measuredValue=" + measuredValue +
                ", isQualified=" + isQualified +
                '}';
    }
}
