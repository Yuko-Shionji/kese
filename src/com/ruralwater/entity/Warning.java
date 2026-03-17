package com.ruralwater.entity;

import java.io.Serializable;

/**
 * 预警信息实体类
 */
public class Warning implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer warningId;
    private Integer recordId;
    private Integer plantId;
    private String warningType; // quality, equipment, supply, other
    private String warningLevel; // low, medium, high, critical
    private String title;
    private String content;
    private String status; // active, processed, ignored
    private Integer handlerId;
    private String handleTime;
    private String handleResult;
    private String createTime;
    
    // 关联字段（非数据库字段）
    private String plantName;
    private String handlerName;
    
    public Warning() {}
    
    public Warning(Integer warningId, Integer plantId, String warningType, String warningLevel) {
        this.warningId = warningId;
        this.plantId = plantId;
        this.warningType = warningType;
        this.warningLevel = warningLevel;
    }
    
    // Getters and Setters
    public Integer getWarningId() { return warningId; }
    public void setWarningId(Integer warningId) { this.warningId = warningId; }
    
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }
    
    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }
    
    public String getWarningType() { return warningType; }
    public void setWarningType(String warningType) { this.warningType = warningType; }
    
    public String getWarningLevel() { return warningLevel; }
    public void setWarningLevel(String warningLevel) { this.warningLevel = warningLevel; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getHandlerId() { return handlerId; }
    public void setHandlerId(Integer handlerId) { this.handlerId = handlerId; }
    
    public String getHandleTime() { return handleTime; }
    public void setHandleTime(String handleTime) { this.handleTime = handleTime; }
    
    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }
    
    public String getHandlerName() { return handlerName; }
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }
    
    @Override
    public String toString() {
        return "Warning{" +
                "warningId=" + warningId +
                ", plantId=" + plantId +
                ", warningType='" + warningType + '\'' +
                ", warningLevel='" + warningLevel + '\'' +
                '}';
    }
}
