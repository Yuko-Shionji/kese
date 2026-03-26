package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.service.UserService;
import com.ruralwater.util.UIStyles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 登录界面
 */
public class LoginFrame extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JButton registerButton;
    
    private UserService userService = new UserService();
    
    public LoginFrame() {
        initUI();
    }
    
    private void initUI() {
        setTitle("农村饮水安全监测管理系统 - 用户登录");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // 设置窗口图标
        try {
            ImageIcon icon = new ImageIcon("icon.png");
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            // 忽略图标加载失败
        }
        
        // 主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标题
        JLabel titleLabel = new JLabel("农村饮水安全监测管理系统", SwingConstants.CENTER);
        titleLabel.setFont(UIStyles.FONT_TITLE);
        titleLabel.setForeground(UIStyles.PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // 副标题
        JLabel subTitleLabel = new JLabel("Rural Drinking Water Safety Monitoring System", SwingConstants.CENTER);
        subTitleLabel.setFont(new Font("微软雅黑", Font.ITALIC, 14));
        subTitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(subTitleLabel, gbc);
        
        // 用户名标签
        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setFont(UIStyles.FONT_BODY);
        usernameLabel.setForeground(UIStyles.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(usernameLabel, gbc);
        
        // 用户名输入框
        usernameField = UIStyles.createTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(usernameField, gbc);
        
        // 密码标签
        JLabel passwordLabel = new JLabel("密 码：");
        passwordLabel.setFont(UIStyles.FONT_BODY);
        passwordLabel.setForeground(UIStyles.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(passwordLabel, gbc);
        
        // 密码输入框
        passwordField = UIStyles.createPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(passwordField, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        
        // 登录按钮
        loginButton = UIStyles.createPrimaryButton("登录");
        loginButton.setPreferredSize(new Dimension(120, 40));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);
        
        // 取消按钮
        cancelButton = new JButton("退出");
        cancelButton.setFont(UIStyles.FONT_BODY);
        cancelButton.setBackground(UIStyles.TEXT_SECONDARY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(cancelButton, gbc);
        
        // 提示信息
        JLabel tipLabel = new JLabel("💡 默认账号：admin / admin123", SwingConstants.CENTER);
        tipLabel.setFont(UIStyles.FONT_SMALL);
        tipLabel.setForeground(UIStyles.TEXT_SECONDARY);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(tipLabel, gbc);
        
        // 注册链接
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setOpaque(false);
        registerButton = new JButton("没有账号？立即注册");
        registerButton.setFont(UIStyles.FONT_SMALL);
        registerButton.setPreferredSize(new Dimension(160, 35));
        registerButton.setBackground(UIStyles.PRIMARY_DARK);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        mainPanel.add(linkPanel, gbc);
        
        add(mainPanel);
        
        // 事件监听
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // 回车键登录
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });
        
        // 注册按钮事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterFrame();
            }
        });
    }
    
    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名！", "提示", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入密码！", "提示", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        try {
            User user = userService.login(username, password);
            
            if (user != null) {
                this.dispose();
                // 打开主界面
                MainFrame mainFrame = new MainFrame(user);
                mainFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "登录失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * 打开注册界面
     */
    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
    }
}
