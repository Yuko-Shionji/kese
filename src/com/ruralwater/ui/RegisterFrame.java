package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * 用户注册界面
 */
public class RegisterFrame extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField realNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton cancelButton;
    
    private UserService userService = new UserService();
    
    public RegisterFrame() {
        initUI();
    }
    
    private void initUI() {
        setTitle("农村饮水安全监测管理系统 - 用户注册");
        setSize(500, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // 主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标题
        JLabel titleLabel = new JLabel("用户注册", SwingConstants.CENTER);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // 用户名标签
        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(usernameLabel, gbc);
        
        // 用户名输入框
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("宋体", Font.PLAIN, 14));
        usernameField.setForeground(Color.BLACK);
        usernameField.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(usernameField, gbc);
        
        // 真实姓名标签
        JLabel realNameLabel = new JLabel("真实姓名：");
        realNameLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        realNameLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(realNameLabel, gbc);
        
        // 真实姓名输入框
        realNameField = new JTextField(20);
        realNameField.setFont(new Font("宋体", Font.PLAIN, 14));
        realNameField.setForeground(Color.BLACK);
        realNameField.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(realNameField, gbc);
        
        // 密码标签
        JLabel passwordLabel = new JLabel("密 码：");
        passwordLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(passwordLabel, gbc);
        
        // 密码输入框
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("宋体", Font.PLAIN, 14));
        passwordField.setForeground(Color.BLACK);
        passwordField.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(passwordField, gbc);
        
        // 确认密码标签
        JLabel confirmPasswordLabel = new JLabel("确认密码：");
        confirmPasswordLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(confirmPasswordLabel, gbc);
        
        // 确认密码输入框
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("宋体", Font.PLAIN, 14));
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(confirmPasswordField, gbc);
        
        // 手机号标签
        JLabel phoneLabel = new JLabel("手机号：");
        phoneLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        phoneLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(phoneLabel, gbc);
        
        // 手机号输入框
        phoneField = new JTextField(20);
        phoneField.setFont(new Font("宋体", Font.PLAIN, 14));
        phoneField.setForeground(Color.BLACK);
        phoneField.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 5;
        mainPanel.add(phoneField, gbc);
        
        // 邮箱标签
        JLabel emailLabel = new JLabel("邮箱：");
        emailLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        emailLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(emailLabel, gbc);
        
        // 邮箱输入框
        emailField = new JTextField(20);
        emailField.setFont(new Font("宋体", Font.PLAIN, 14));
        emailField.setForeground(Color.BLACK);
        emailField.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 6;
        mainPanel.add(emailField, gbc);
        
        // 角色标签
        JLabel roleLabel = new JLabel("角色：");
        roleLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        roleLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(roleLabel, gbc);
        
        // 角色下拉框
        String[] roles = {"viewer", "operator", "admin"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("宋体", Font.PLAIN, 14));
        roleComboBox.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 7;
        mainPanel.add(roleComboBox, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        
        // 注册按钮
        registerButton = new JButton("注册");
        registerButton.setFont(new Font("宋体", Font.PLAIN, 14));
        registerButton.setPreferredSize(new Dimension(100, 35));
        registerButton.setBackground(new Color(41, 128, 185));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(registerButton, gbc);
        
        // 取消按钮
        cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("宋体", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(189, 195, 199));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 8;
        mainPanel.add(cancelButton, gbc);
        
        add(mainPanel);
        
        // 事件监听
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRegister();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void doRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String realName = realNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();
        
        // 验证用户名
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名！", "提示", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        if (username.length() < 3 || username.length() > 20) {
            JOptionPane.showMessageDialog(this, "用户名长度必须在 3-20 位之间！", "提示", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        if (!Pattern.matches("^[a-zA-Z0-9_]+$", username)) {
            JOptionPane.showMessageDialog(this, "用户名只能包含字母、数字和下划线！", "提示", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        // 验证真实姓名
        if (realName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入真实姓名！", "提示", JOptionPane.WARNING_MESSAGE);
            realNameField.requestFocus();
            return;
        }
        
        // 验证密码
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入密码！", "提示", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "密码长度不能少于 6 位！", "提示", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致！", "提示", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        // 验证手机号
        if (!phone.isEmpty() && !Pattern.matches("^1[3-9]\\d{9}$", phone)) {
            JOptionPane.showMessageDialog(this, "请输入正确的手机号！", "提示", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return;
        }
        
        // 验证邮箱
        if (!email.isEmpty() && !Pattern.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", email)) {
            JOptionPane.showMessageDialog(this, "请输入正确的邮箱格式！", "提示", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        try {
            // 检查用户名是否已存在
            User existingUser = userService.findByUsername(username);
            if (existingUser != null) {
                JOptionPane.showMessageDialog(this, "该用户名已被注册！", "提示", JOptionPane.WARNING_MESSAGE);
                usernameField.requestFocus();
                return;
            }
            
            // 创建用户对象
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRealName(realName);
            user.setRole(role);
            user.setPhone(phone);
            user.setEmail(email);
            user.setStatus(1); // 默认启用状态
            
            // 执行注册
            int result = userService.register(user);
            
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "注册成功！请登录。", "成功", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // 关闭注册窗口
            } else {
                JOptionPane.showMessageDialog(this, "注册失败，请稍后重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "注册失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
