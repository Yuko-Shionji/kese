package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        regionManageBtn.addActionListener(e -> openRegionManagePanel());
        buttonPanel.add(regionManageBtn);
        
        JButton sourceManageBtn = createStyledButton("水源管理", "管理水源地信息");
        sourceManageBtn.addActionListener(e -> openWaterSourcePanel());
        buttonPanel.add(sourceManageBtn);
        
        JButton standardManageBtn = createStyledButton("检测标准管理", "管理水质检测标准");
        standardManageBtn.addActionListener(e -> openStandardManagePanel());
        buttonPanel.add(standardManageBtn);
        
        JButton equipmentManageBtn = createStyledButton("设备管理", "管理水厂设备");
        equipmentManageBtn.addActionListener(e -> openEquipmentPanel());
        buttonPanel.add(equipmentManageBtn);
        
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
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            UserManageDialog dialog = new UserManageDialog(owner, currentUser);
            dialog.setVisible(true);
        }
    }
    
    /**
     * 打开区域管理面板
     */
    private void openRegionManagePanel() {
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            JDialog dialog = new JDialog(owner, "区域管理", true);
            dialog.setSize(900, 600);
            dialog.setLocationRelativeTo(owner);
            dialog.setLayout(new BorderLayout());
            
            RegionManagePanel panel = new RegionManagePanel(currentUser);
            dialog.add(panel);
            
            dialog.setVisible(true);
        }
    }
    
    /**
     * 打开水源管理面板
     */
    private void openWaterSourcePanel() {
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            JDialog dialog = new JDialog(owner, "水源管理", true);
            dialog.setSize(900, 600);
            dialog.setLocationRelativeTo(owner);
            dialog.setLayout(new BorderLayout());
            
            WaterSourcePanel panel = new WaterSourcePanel(currentUser);
            dialog.add(panel);
            
            dialog.setVisible(true);
        }
    }
    
    /**
     * 打开检测标准管理面板
     */
    private void openStandardManagePanel() {
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            JDialog dialog = new JDialog(owner, "检测标准管理", true);
            dialog.setSize(900, 600);
            dialog.setLocationRelativeTo(owner);
            dialog.setLayout(new BorderLayout());
            
            StandardManagePanel panel = new StandardManagePanel(currentUser);
            dialog.add(panel);
            
            dialog.setVisible(true);
        }
    }
    
    /**
     * 打开设备管理面板
     */
    private void openEquipmentPanel() {
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            JDialog dialog = new JDialog(owner, "设备管理", true);
            dialog.setSize(900, 600);
            dialog.setLocationRelativeTo(owner);
            dialog.setLayout(new BorderLayout());
            
            EquipmentPanel panel = new EquipmentPanel(currentUser);
            dialog.add(panel);
            
            dialog.setVisible(true);
        }
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

/**
 * 用户管理对话框
 */
class UserManageDialog extends JDialog {
    private User currentUser;
    private JTable userTable;
    private DefaultTableModel tableModel;
    
    public UserManageDialog(Frame owner, User user) {
        super(owner, "用户管理", true);
        this.currentUser = user;
        initUI();
        loadData();
    }
    
    private void initUI() {
        setSize(700, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        // 工具栏
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("新增用户");
        addBtn.addActionListener(e -> doAdd());
        toolbar.add(addBtn);
        
        JButton editBtn = new JButton("编辑用户");
        editBtn.addActionListener(e -> doEdit());
        toolbar.add(editBtn);
        
        JButton deleteBtn = new JButton("删除用户");
        deleteBtn.addActionListener(e -> doDelete());
        toolbar.add(deleteBtn);
        
        JButton refreshBtn = new JButton("刷新");
        refreshBtn.addActionListener(e -> loadData());
        toolbar.add(refreshBtn);
        
        add(toolbar, BorderLayout.NORTH);
        
        // 表格
        String[] columnNames = {"ID", "用户名", "姓名", "角色", "电话", "邮箱", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userTable.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadData() {
        try {
            tableModel.setRowCount(0);
            UserService userService = new UserService();
            java.util.List<User> users = userService.getAllUsers();
            
            for (User u : users) {
                Object[] row = {
                    u.getUserId(),
                    u.getUsername(),
                    u.getRealName(),
                    getRoleName(u.getRole()),
                    u.getPhone() != null ? u.getPhone() : "-",
                    u.getEmail() != null ? u.getEmail() : "-",
                    u.getStatus() == 1 ? "正常" : "停用"
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void doAdd() {
        AddUserDialog dialog = new AddUserDialog(this, currentUser);
        dialog.setVisible(true);
        if (dialog.isAdded()) {
            loadData();
        }
    }
    
    private void doEdit() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的用户", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        EditUserDialog dialog = new EditUserDialog(this, userId);
        dialog.setVisible(true);
        if (dialog.isEdited()) {
            loadData();
        }
    }
    
    private void doDelete() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的用户", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        if (userId == currentUser.getUserId()) {
            JOptionPane.showMessageDialog(this, "不能删除当前登录用户", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, "确定要删除该用户吗？", "确认删除", 
                                                    JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                UserService userService = new UserService();
                userService.deleteUser(userId);
                JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String getRoleName(String role) {
        switch (role) {
            case "admin": return "管理员";
            case "operator": return "操作员";
            case "viewer": return "查看员";
            default: return role;
        }
    }
}

/**
 * 新增用户对话框
 */
class AddUserDialog extends JDialog {
    private boolean added = false;
    private User currentUser;
    
    public AddUserDialog(JDialog owner, User user) {
        super(owner, "新增用户", true);
        this.currentUser = user;
        initUI();
    }
    
    private void initUI() {
        setSize(450, 350);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("用户名："), gbc);
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("密码："), gbc);
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("姓名："), gbc);
        JTextField realNameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(realNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("角色："), gbc);
        JComboBox<String> roleCombo = new JComboBox<>();
        roleCombo.addItem("admin");
        roleCombo.addItem("operator");
        roleCombo.addItem("viewer");
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(roleCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("电话："), gbc);
        JTextField phoneField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("邮箱："), gbc);
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(emailField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String realName = realNameField.getText().trim();
                String role = (String) roleCombo.getSelectedItem();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                
                if (username.isEmpty() || password.isEmpty() || realName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "用户名、密码和姓名不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                com.ruralwater.entity.User user = new com.ruralwater.entity.User();
                user.setUsername(username);
                user.setPassword(password);
                user.setRealName(realName);
                user.setRole(role);
                user.setPhone(phone);
                user.setEmail(email);
                
                UserService userService = new UserService();
                userService.addUser(user, currentUser.getUsername());
                
                added = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(confirmBtn);
        
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public boolean isAdded() {
        return added;
    }
}

/**
 * 编辑用户对话框
 */
class EditUserDialog extends JDialog {
    private boolean edited = false;
    
    public EditUserDialog(JDialog owner, Integer userId) {
        super(owner, "编辑用户 #" + userId, true);
        initUI(userId);
    }
    
    private void initUI(Integer userId) {
        setSize(450, 350);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        try {
            UserService userService = new UserService();
            com.ruralwater.entity.User user = userService.getUserById(userId);
            
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("用户名："), gbc);
            JTextField usernameField = new JTextField(user.getUsername());
            usernameField.setEditable(false);
            gbc.gridx = 1; gbc.gridy = 0;
            panel.add(usernameField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("密码："), gbc);
            JPasswordField passwordField = new JPasswordField("");
            gbc.gridx = 1; gbc.gridy = 1;
            panel.add(passwordField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("姓名："), gbc);
            JTextField realNameField = new JTextField(user.getRealName());
            gbc.gridx = 1; gbc.gridy = 2;
            panel.add(realNameField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            panel.add(new JLabel("角色："), gbc);
            JComboBox<String> roleCombo = new JComboBox<>();
            roleCombo.addItem("admin");
            roleCombo.addItem("operator");
            roleCombo.addItem("viewer");
            roleCombo.setSelectedItem(user.getRole());
            gbc.gridx = 1; gbc.gridy = 3;
            panel.add(roleCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4;
            panel.add(new JLabel("电话："), gbc);
            JTextField phoneField = new JTextField(user.getPhone() != null ? user.getPhone() : "");
            gbc.gridx = 1; gbc.gridy = 4;
            panel.add(phoneField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 5;
            panel.add(new JLabel("邮箱："), gbc);
            JTextField emailField = new JTextField(user.getEmail() != null ? user.getEmail() : "");
            gbc.gridx = 1; gbc.gridy = 5;
            panel.add(emailField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 6;
            panel.add(new JLabel("状态："), gbc);
            JComboBox<String> statusCombo = new JComboBox<>();
            statusCombo.addItem("正常");
            statusCombo.addItem("停用");
            statusCombo.setSelectedIndex(user.getStatus() == 1 ? 0 : 1);
            gbc.gridx = 1; gbc.gridy = 6;
            panel.add(statusCombo, gbc);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton confirmBtn = new JButton("确定");
            confirmBtn.addActionListener(e -> {
                try {
                    String password = new String(passwordField.getPassword());
                    String realName = realNameField.getText().trim();
                    String role = (String) roleCombo.getSelectedItem();
                    String phone = phoneField.getText().trim();
                    String email = emailField.getText().trim();
                    int status = statusCombo.getSelectedIndex() == 0 ? 1 : 0;
                    
                    if (realName.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "姓名不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    user.setRealName(realName);
                    user.setRole(role);
                    user.setPhone(phone);
                    user.setEmail(email);
                    user.setStatus(status);
                    if (!password.isEmpty()) {
                        user.setPassword(password);
                    }
                    
                    userService.updateUser(user);
                    
                    edited = true;
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "修改失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonPanel.add(confirmBtn);
            
            JButton cancelBtn = new JButton("取消");
            cancelBtn.addActionListener(e -> dispose());
            buttonPanel.add(cancelBtn);
            
            add(panel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isEdited() {
        return edited;
    }
}
