package com.ruralwater.ui;

import com.ruralwater.entity.User;

import javax.swing.*;
import java.awt.*;

/**
 * 系统管理面板（仅管理员使用）
 */
public class SystemManagePanel extends JPanel {
    
    private User currentUser;
    
    public SystemManagePanel(User user) {
        this.currentUser = user;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 功能按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("管理功能"));
        
        JButton userManageBtn = createStyledButton("用户管理", "管理系统用户");
        userManageBtn.addActionListener(e -> openUserManageDialog());
        buttonPanel.add(userManageBtn);
        
        JButton regionManageBtn = createStyledButton("区域管理", "管理行政区域");
        regionManageBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "区域管理开发中..."));
        buttonPanel.add(regionManageBtn);
        
        JButton sourceManageBtn = createStyledButton("水源管理", "管理水源地信息");
        sourceManageBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "水源管理开发中..."));
        buttonPanel.add(sourceManageBtn);
        
        JButton standardManageBtn = createStyledButton("检测标准管理", "管理水质检测标准");
        standardManageBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "检测标准管理开发中..."));
        buttonPanel.add(standardManageBtn);
        
        JButton logManageBtn = createStyledButton("系统日志", "查看系统操作日志");
        logManageBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "系统日志开发中..."));
        buttonPanel.add(logManageBtn);
        
        JButton dataInitBtn = createStyledButton("数据初始化", "重新生成测试数据");
        dataInitBtn.addActionListener(e -> initData());
        buttonPanel.add(dataInitBtn);
        
        add(buttonPanel, BorderLayout.NORTH);
        
        // 说明面板
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("系统信息"));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        infoPanel.add(new JLabel("当前用户：" + currentUser.getRealName()));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(new JLabel("用户角色：" + getRoleName(currentUser.getRole())));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(new JLabel("系统版本：v1.0"));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(new JLabel("数据库：MySQL 5.5+"));
        
        add(infoPanel, BorderLayout.CENTER);
    }
    
    /**
     * 创建样式按钮
     */
    private JButton createStyledButton(String title, String tooltip) {
        JButton button = new JButton(title);
        button.setToolTipText(tooltip);
        button.setFont(new Font("微软雅黑", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 80));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
    
    /**
     * 获取角色名称
     */
    private String getRoleName(String role) {
        switch (role) {
            case "admin": return "管理员";
            case "operator": return "操作员";
            case "viewer": return "查看员";
            default: return role;
        }
    }
    
    /**
     * 打开用户管理对话框
     */
    private void openUserManageDialog() {
        JDialog dialog = new JDialog((Frame) null, "用户管理", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        dialog.add(new JLabel("用户管理功能开发中...", SwingConstants.CENTER));
        
        dialog.setVisible(true);
    }
    
    /**
     * 数据初始化
     */
    private void initData() {
        int result = JOptionPane.showConfirmDialog(this, 
            "确定要重新初始化测试数据吗？\n此操作将清空现有数据并生成新的测试数据。", 
            "确认初始化", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                // TODO: 调用数据库存储过程初始化数据
                JOptionPane.showMessageDialog(this, 
                    "数据初始化成功！\n请执行 SQL：CALL sp_insert_test_data();", 
                    "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "初始化失败：" + e.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
