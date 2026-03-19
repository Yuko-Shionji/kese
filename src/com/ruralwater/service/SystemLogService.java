package com.ruralwater.service;

import com.ruralwater.entity.SystemLog;
import com.ruralwater.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统日志服务类
 */
public class SystemLogService {
    
    /**
     * 记录系统日志
     */
    public void log(Integer userId, String operation, String module, String content) {
        String sql = "INSERT INTO system_logs(user_id, operation, module, ip_address, content) " +
                     "VALUES(?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, operation);
            pstmt.setString(3, module);
            pstmt.setString(4, getLocalIPAddress());
            pstmt.setString(5, content);
            
            pstmt.executeUpdate();
            System.out.println("日志记录成功：" + operation);
        } catch (Exception e) {
            System.err.println("日志记录失败：" + e.getMessage());
            // 日志记录失败不影响主业务流程
        }
    }
    
    /**
     * 获取本地 IP 地址
     */
    private String getLocalIPAddress() {
        try {
            java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
            return localMachine.getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }
    
    /**
     * 查询最近 N 条日志
     */
    public List<SystemLog> getRecentLogs(int count) throws Exception {
        String sql = "SELECT sl.*, u.real_name as user_name " +
                     "FROM system_logs sl " +
                     "LEFT JOIN users u ON sl.user_id = u.user_id " +
                     "ORDER BY sl.create_time DESC " +
                     "LIMIT ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, count);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<SystemLog> logs = new ArrayList<>();
                while (rs.next()) {
                    SystemLog log = new SystemLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setOperation(rs.getString("operation"));
                    log.setModule(rs.getString("module"));
                    log.setIpAddress(rs.getString("ip_address"));
                    log.setContent(rs.getString("content"));
                    log.setCreateTime(rs.getString("create_time"));
                    log.setUserName(rs.getString("user_name"));
                    logs.add(log);
                }
                return logs;
            }
        }
    }
    
    /**
     * 按模块查询日志
     */
    public List<SystemLog> getLogsByModule(String module, int pageNum, int pageSize) throws Exception {
        String sql = "SELECT sl.*, u.real_name as user_name " +
                     "FROM system_logs sl " +
                     "LEFT JOIN users u ON sl.user_id = u.user_id " +
                     "WHERE sl.module = ? " +
                     "ORDER BY sl.create_time DESC " +
                     "LIMIT ?, ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, module);
            pstmt.setInt(2, (pageNum - 1) * pageSize);
            pstmt.setInt(3, pageSize);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<SystemLog> logs = new ArrayList<>();
                while (rs.next()) {
                    SystemLog log = new SystemLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setOperation(rs.getString("operation"));
                    log.setModule(rs.getString("module"));
                    log.setIpAddress(rs.getString("ip_address"));
                    log.setContent(rs.getString("content"));
                    log.setCreateTime(rs.getString("create_time"));
                    log.setUserName(rs.getString("user_name"));
                    logs.add(log);
                }
                return logs;
            }
        }
    }
    
    /**
     * 按用户 ID 查询日志
     */
    public List<SystemLog> getLogsByUserId(Integer userId, int pageNum, int pageSize) throws Exception {
        String sql = "SELECT sl.*, u.real_name as user_name " +
                     "FROM system_logs sl " +
                     "LEFT JOIN users u ON sl.user_id = u.user_id " +
                     "WHERE sl.user_id = ? " +
                     "ORDER BY sl.create_time DESC " +
                     "LIMIT ?, ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, (pageNum - 1) * pageSize);
            pstmt.setInt(3, pageSize);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<SystemLog> logs = new ArrayList<>();
                while (rs.next()) {
                    SystemLog log = new SystemLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setOperation(rs.getString("operation"));
                    log.setModule(rs.getString("module"));
                    log.setIpAddress(rs.getString("ip_address"));
                    log.setContent(rs.getString("content"));
                    log.setCreateTime(rs.getString("create_time"));
                    log.setUserName(rs.getString("user_name"));
                    logs.add(log);
                }
                return logs;
            }
        }
    }
    
    /**
     * 统计日志总数
     */
    public int getTotalCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM system_logs";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        }
    }
    
    /**
     * 清空过期日志（保留最近 N 天）\n     */
    public void clearOldLogs(int keepDays) throws Exception {
        String sql = "DELETE FROM system_logs WHERE create_time < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, keepDays);
            int deleted = pstmt.executeUpdate();
            System.out.println("已删除 " + deleted + " 条过期日志");
        }
    }
}
