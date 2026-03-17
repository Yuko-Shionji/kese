package com.ruralwater.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水厂实体类
 */
public class WaterPlant implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer plantId;
    private String plantName;
    private String plantCode;
    private Integer regionId;
    private Integer sourceId;
    private String address;
    private BigDecimal designCapacity;
    private BigDecimal actualCapacity;
    private Integer servicePopulation;
    private String contactPerson;
    private String contactPhone;
    private Integer status;
    private String buildDate;
    private String createTime;
    
    // 关联字段（非数据库字段）
    private String regionName;
    private String sourceName;
    
    public WaterPlant() {}
    
    public WaterPlant(Integer plantId, String plantName, String plantCode) {
        this.plantId = plantId;
        this.plantName = plantName;
        this.plantCode = plantCode;
    }
    
    // Getters and Setters
    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }
    
    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }
    
    public String getPlantCode() { return plantCode; }
    public void setPlantCode(String plantCode) { this.plantCode = plantCode; }
    
    public Integer getRegionId() { return regionId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
    
    public Integer getSourceId() { return sourceId; }
    public void setSourceId(Integer sourceId) { this.sourceId = sourceId; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public BigDecimal getDesignCapacity() { return designCapacity; }
    public void setDesignCapacity(BigDecimal designCapacity) { this.designCapacity = designCapacity; }
    
    public BigDecimal getActualCapacity() { return actualCapacity; }
    public void setActualCapacity(BigDecimal actualCapacity) { this.actualCapacity = actualCapacity; }
    
    public Integer getServicePopulation() { return servicePopulation; }
    public void setServicePopulation(Integer servicePopulation) { this.servicePopulation = servicePopulation; }
    
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public String getBuildDate() { return buildDate; }
    public void setBuildDate(String buildDate) { this.buildDate = buildDate; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    
    @Override
    public String toString() {
        return "WaterPlant{" +
                "plantId=" + plantId +
                ", plantName='" + plantName + '\'' +
                ", plantCode='" + plantCode + '\'' +
                '}';
    }
}
