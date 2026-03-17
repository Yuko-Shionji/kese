package com.ruralwater.entity;

import java.io.Serializable;

/**
 * 区域实体类
 */
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer regionId;
    private String regionCode;
    private String regionName;
    private Integer parentId;
    private String level; // province, city, district, town, village
    private String fullPath;
    
    public Region() {}
    
    public Region(Integer regionId, String regionCode, String regionName, String level) {
        this.regionId = regionId;
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.level = level;
    }
    
    // Getters and Setters
    public Integer getRegionId() { return regionId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
    
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    
    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getFullPath() { return fullPath; }
    public void setFullPath(String fullPath) { this.fullPath = fullPath; }
    
    @Override
    public String toString() {
        return "Region{" +
                "regionId=" + regionId +
                ", regionCode='" + regionCode + '\'' +
                ", regionName='" + regionName + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
