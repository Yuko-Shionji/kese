package com.ruralwater.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水源实体类
 */
public class WaterSource implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer sourceId;
    private String sourceName;
    private String sourceType; // reservoir, well, river, lake, spring
    private Integer regionId;
    private String locationDetail;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal capacity;
    private Integer status;
    private String createTime;
    
    // 关联字段
    private String regionName;
    
    public WaterSource() {}
    
    public WaterSource(Integer sourceId, String sourceName, String sourceType) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.sourceType = sourceType;
    }
    
    // Getters and Setters
    public Integer getSourceId() { return sourceId; }
    public void setSourceId(Integer sourceId) { this.sourceId = sourceId; }
    
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    
    public Integer getRegionId() { return regionId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
    
    public String getLocationDetail() { return locationDetail; }
    public void setLocationDetail(String locationDetail) { this.locationDetail = locationDetail; }
    
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    
    public BigDecimal getCapacity() { return capacity; }
    public void setCapacity(BigDecimal capacity) { this.capacity = capacity; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    
    @Override
    public String toString() {
        return "WaterSource{" +
                "sourceId=" + sourceId +
                ", sourceName='" + sourceName + '\'' +
                ", sourceType='" + sourceType + '\'' +
                '}';
    }
}
