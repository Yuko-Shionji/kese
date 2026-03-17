package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 系统主界面
 */
public class MainFrame extends JFrame {
    
    private User currentUser;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;
    
    public MainFrame(User user) {
        this.currentUser = user;
        initUI();
    }
    
    private void initUI() {
        setTitle("农村饮水安全监测管理系统");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // 菜单栏
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 顶部欢迎栏
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.setBackground(new Color(41, 128, 185));
        
        JLabel welcomeLabel = new JLabel("欢迎，" + currentUser.getRealName() + " (" + 
                                         getRoleName(currentUser.getRole()) + ")");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        statusLabel = new JLabel("系统就绪");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        topPanel.add(statusLabel, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 选项卡面板
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        // 添加各个功能模块
        addFunctionTabs();
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // 状态栏
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel infoLabel = new JLabel("技术支持：农村饮水安全监测系统 v1.0");
        infoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoLabel.setForeground(Color.GRAY);
        statusBar.add(infoLabel);
        
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // 窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(MainFrame.this, 
                    "确定要退出系统吗？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
    
    /**
     * 创建菜单栏
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // 系统菜单
        JMenu systemMenu = new JMenu("系统");
        systemMenu.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        JMenuItem changePwdItem = new JMenuItem("修改密码");
        changePwdItem.addActionListener(e -> openChangePasswordDialog());
        systemMenu.add(changePwdItem);
        
        systemMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("退出系统");
        exitItem.addActionListener(e -> System.exit(0));
        systemMenu.add(exitItem);
        
        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        helpMenu.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(systemMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    /**
     * 添加功能标签页
     */
    private void addFunctionTabs() {
        // 水厂管理
        WaterPlantPanel plantPanel = new WaterPlantPanel(currentUser);
        tabbedPane.addTab("水厂管理", plantPanel);
        
        // 水质检测
        WaterQualityPanel qualityPanel = new WaterQualityPanel(currentUser);
        tabbedPane.addTab("水质检测", qualityPanel);
        
        // 预警信息
        WarningPanel warningPanel = new WarningPanel(currentUser);
        tabbedPane.addTab("预警信息", warningPanel);
        
        // 系统管理（仅管理员可见）
        if ("admin".equals(currentUser.getRole())) {
            SystemManagePanel sysPanel = new SystemManagePanel(currentUser);
            tabbedPane.addTab("系统管理", sysPanel);
        }
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
     * 打开修改密码对话框
     */
    private void openChangePasswordDialog() {
        JDialog dialog = new JDialog(this, "修改密码", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel oldPwdLabel = new JLabel("旧密码：");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(oldPwdLabel, gbc);
        
        JPasswordField oldPwdField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(oldPwdField, gbc);
        
        JLabel newPwdLabel = new JLabel("新密码：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(newPwdLabel, gbc);
        
        JPasswordField newPwdField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(newPwdField, gbc);
        
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            String oldPwd = new String(oldPwdField.getPassword());
            String newPwd = new String(newPwdField.getPassword());
            
            if (oldPwd.isEmpty() || newPwd.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "密码不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                UserService userService = new UserService();
                userService.changePassword(currentUser.getUserId(), oldPwd, newPwd);
                JOptionPane.showMessageDialog(dialog, "密码修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "修改失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(confirmBtn, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * 显示关于对话框
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this, 
            "农村饮水安全监测管理系统\n版本：v1.0\n\n技术栈：JavaSE + Swing + JDBC + MySQL\n\nCopyright © 2026", 
            "关于系统", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 更新状态栏
     */
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }
}
