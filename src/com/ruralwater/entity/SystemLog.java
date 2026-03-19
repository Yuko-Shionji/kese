package com.ruralwater.entity;

import java.io.Serializable;

/**
 * 系统日志实体类
 */
public class SystemLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer logId;
    private Integer userId;
    private String operation;
    private String module;
    private String ipAddress;
    private String content;
    private String createTime;
    
    // 关联字段
    private String userName;
    
    public SystemLog() {}
    
    // Getters and Setters
    public Integer getLogId() { return logId; }
    public void setLogId(Integer logId) { this.logId = logId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    @Override
    public String toString() {
        return "SystemLog{" +
                "logId=" + logId +
                ", operation='" + operation + '\'' +
                ", module='" + module + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
