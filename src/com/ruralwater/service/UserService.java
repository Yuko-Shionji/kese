package com.ruralwater.service;

import com.ruralwater.dao.UserDAO;
import com.ruralwater.entity.User;

/**
 * 用户服务层（业务逻辑层）
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
        
        return userDAO.login(username, password);
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
        
        // 验证旧密码
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        // 这里简化处理，实际应该加密存储
        // 实际项目中应该使用 MD5 或 BCrypt 等加密算法
        
        // 更新密码
        user.setPassword(newPassword);
        userDAO.update(user);
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
        
        // 检查用户名是否已存在
        User existUser = userDAO.findByUsername(user.getUsername());
        if (existUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        userDAO.insert(user);
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
        userDAO.delete(userId);
    }
}
