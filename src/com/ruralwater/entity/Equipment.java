package com.ruralwater.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 设备实体类
 */
public class Equipment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer equipmentId;
    private String equipmentName;
    private String equipmentType;
    private String model;
    private Integer plantId;
    private String purchaseDate;
    private Integer warrantyPeriod;
    private String status; // normal, maintenance, faulty, scrapped
    private String lastMaintenanceDate;
    private String nextMaintenanceDate;
    private String remark;
    private String createTime;
    
    // 关联字段
    private String plantName;
    
    public Equipment() {}
    
    // Getters and Setters
    public Integer getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Integer equipmentId) { this.equipmentId = equipmentId; }
    
    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }
    
    public String getEquipmentType() { return equipmentType; }
    public void setEquipmentType(String equipmentType) { this.equipmentType = equipmentType; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }
    
    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }
    
    public Integer getWarrantyPeriod() { return warrantyPeriod; }
    public void setWarrantyPeriod(Integer warrantyPeriod) { this.warrantyPeriod = warrantyPeriod; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getLastMaintenanceDate() { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(String lastMaintenanceDate) { this.lastMaintenanceDate = lastMaintenanceDate; }
    
    public String getNextMaintenanceDate() { return nextMaintenanceDate; }
    public void setNextMaintenanceDate(String nextMaintenanceDate) { this.nextMaintenanceDate = nextMaintenanceDate; }
    
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }
    
    /**
     * 获取状态文本
     */
    public String getStatusText() {
        switch (status) {
            case "normal": return "正常";
            case "maintenance": return "维护中";
            case "faulty": return "故障";
            case "scrapped": return "报废";
            default: return status;
        }
    }
    
    @Override
    public String toString() {
        return "Equipment{" +
                "equipmentId=" + equipmentId +
                ", equipmentName='" + equipmentName + '\'' +
                ", equipmentType='" + equipmentType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
