package com.ruralwater.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水质检测标准实体类
 */
public class WaterQualityStandard implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer standardId;
    private String itemName;
    private String itemCode;
    private String unit;
    private BigDecimal standardValue;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String category; // physical, chemical, biological, radiological
    private String description;
    private Integer isActive;
    
    public WaterQualityStandard() {}
    
    public WaterQualityStandard(Integer standardId, String itemName, String itemCode) {
        this.standardId = standardId;
        this.itemName = itemName;
        this.itemCode = itemCode;
    }
    
    // Getters and Setters
    public Integer getStandardId() { return standardId; }
    public void setStandardId(Integer standardId) { this.standardId = standardId; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public BigDecimal getStandardValue() { return standardValue; }
    public void setStandardValue(BigDecimal standardValue) { this.standardValue = standardValue; }
    
    public BigDecimal getMinValue() { return minValue; }
    public void setMinValue(BigDecimal minValue) { this.minValue = minValue; }
    
    public BigDecimal getMaxValue() { return maxValue; }
    public void setMaxValue(BigDecimal maxValue) { this.maxValue = maxValue; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
    
    @Override
    public String toString() {
        return "WaterQualityStandard{" +
                "standardId=" + standardId +
                ", itemName='" + itemName + '\'' +
                ", itemCode='" + itemCode + '\'' +
                '}';
    }
}
