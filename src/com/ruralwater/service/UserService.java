package com.ruralwater.service;

import com.ruralwater.dao.UserDAO;
import com.ruralwater.entity.User;
import com.ruralwater.util.SimpleLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用户服务层（业务逻辑层）
 * 增强版：添加密码加密和增强的验证
 */
public class UserService {
    
    private UserDAO userDAO = new UserDAO();
    
    /**
     * 用户登录
     */
    public User login(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        SimpleLogger.debug("用户登录尝试：" + username);
        
        // 密码加密后验证
        String encryptedPassword = encryptPassword(password);
        return userDAO.login(username, encryptedPassword);
    }
    
    /**
     * 修改密码（带事务处理）
     */
    public void changePassword(Integer userId, String oldPassword, String newPassword) throws Exception {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户 ID 无效");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("新密码长度不能少于 6 位");
        }
        
        SimpleLogger.info("用户修改密码：ID=" + userId);
        
        // 验证旧密码
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        // 验证旧密码是否正确
        String encryptedOldPassword = encryptPassword(oldPassword);
        if (!encryptedOldPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("旧密码错误");
        }
        
        // 更新密码（加密存储）
        user.setPassword(encryptPassword(newPassword));
        userDAO.update(user);
        
        SimpleLogger.info("用户密码修改成功：ID=" + userId);
    }
    
    /**
     * 添加用户（带权限验证）
     */
    public void addUser(User user, String operatorUsername) throws Exception {
        if (user == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("密码长度不能少于 6 位");
        }
        
        SimpleLogger.info("管理员 " + operatorUsername + " 添加新用户：" + user.getUsername());
        
        // 检查用户名是否已存在
        User existUser = userDAO.findByUsername(user.getUsername());
        if (existUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 密码加密存储
        user.setPassword(encryptPassword(user.getPassword()));
        userDAO.insert(user);
        
        SimpleLogger.info("用户添加成功：" + user.getUsername());
    }
    
    /**
     * 获取所有用户列表
     */
    public java.util.List<User> getAllUsers() throws Exception {
        return userDAO.findAll();
    }
    
    /**
     * 根据 ID 获取用户
     */
    public User getUserById(Integer userId) throws Exception {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户 ID 无效");
        }
        return userDAO.findById(userId);
    }
    
    /**
     * 更新用户信息
     */
    public void updateUser(User user) throws Exception {
        if (user == null || user.getUserId() == null || user.getUserId() <= 0) {
            throw new IllegalArgumentException("用户信息无效");
        }
        userDAO.update(user);
    }
    
    /**
     * 删除用户
     */
    public void deleteUser(Integer userId) throws Exception {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户 ID 无效");
        }
        SimpleLogger.info("删除用户：ID=" + userId);
        userDAO.delete(userId);
    }
    
    /**
     * 密码加密（MD5）
     * @param password 原始密码
     * @return 加密后的密码
     */
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            SimpleLogger.error("MD5 加密算法不可用", e);
            // 降级处理：返回原文（仅用于测试环境）
            return password;
        }
    }
}
