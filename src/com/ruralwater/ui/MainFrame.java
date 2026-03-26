package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterQualityRecord;
import com.ruralwater.service.*;
import com.ruralwater.util.UIStyles;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

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
        
        // 设置窗口图标
        try {
            ImageIcon icon = new ImageIcon("icon.png");
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            // 忽略图标加载失败
        }
        
        // 菜单栏
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 顶部欢迎栏
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        topPanel.setBackground(UIStyles.PRIMARY_COLOR);
        
        JLabel welcomeLabel = new JLabel("欢迎，" + currentUser.getRealName() + " (" + 
                                         getRoleName(currentUser.getRole()) + ")");
        welcomeLabel.setFont(UIStyles.FONT_HEADING);
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        statusLabel = new JLabel("系统就绪 | " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        statusLabel.setFont(UIStyles.FONT_SMALL);
        statusLabel.setForeground(Color.WHITE);
        topPanel.add(statusLabel, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 选项卡面板
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(UIStyles.FONT_BODY);
        tabbedPane.setBackground(UIStyles.BACKGROUND_LIGHT);
        
        // 添加各个功能模块
        addFunctionTabs();
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // 状态栏
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        statusBar.setBackground(UIStyles.BACKGROUND_LIGHT);
        
        JLabel infoLabel = new JLabel("© 2026 农村饮水安全监测系统 v2.0 | 技术支持：系统管理员");
        infoLabel.setFont(UIStyles.FONT_SMALL);
        infoLabel.setForeground(UIStyles.TEXT_SECONDARY);
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
        // 首页仪表板
        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("📊 首页", dashboardPanel);
        
        // 统计分析
        StatisticsPanel statsPanel = new StatisticsPanel();
        tabbedPane.addTab("📈 统计分析", statsPanel);
        
        // 水厂管理
        WaterPlantPanel plantPanel = new WaterPlantPanel(currentUser);
        tabbedPane.addTab("💧 水厂管理", plantPanel);
        
        // 水质检测
        WaterQualityPanel qualityPanel = new WaterQualityPanel(currentUser);
        tabbedPane.addTab("🔬 水质检测", qualityPanel);
        
        // 预警信息
        WarningPanel warningPanel = new WarningPanel(currentUser);
        tabbedPane.addTab("⚠️ 预警信息", warningPanel);
        
        // 系统管理（仅管理员可见）
        if ("admin".equals(currentUser.getRole())) {
            SystemManagePanel sysPanel = new SystemManagePanel(currentUser);
            tabbedPane.addTab("⚙️ 系统管理", sysPanel);
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
            "农村饮水安全监测管理系统\n版本：v1.0\n\n技术栈：JavaSE + Swing + JDBC + MySQL\n\n功能模块：\n• 首页仪表板 - 数据统计与可视化\n• 水厂管理 - 水厂信息维护\n• 水质检测 - 检测记录与审核\n• 预警信息 - 异常自动预警\n• 系统管理 - 用户与日志管理\n\nCopyright © 2026", 
            "关于系统", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 创建首页仪表板面板
     */
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 顶部统计卡片区域
        JPanel statsPanel = createStatsCardsPanel();
        panel.add(statsPanel, BorderLayout.NORTH);
        
        // 中间图表区域
        JPanel chartsPanel = createChartsPanel();
        panel.add(chartsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建统计卡片区
     */
    private JPanel createStatsCardsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 120));
        
        try {
            // 水厂数量
            WaterPlantService plantService = new WaterPlantService();
            int plantCount = plantService.getTotalCount();
            panel.add(createStatCard("水厂总数", String.valueOf(plantCount), new Color(41, 128, 185)));
            
            // 检测记录数
            WaterQualityService qualityService = new WaterQualityService();
            int recordCount = qualityService.getTotalCount();
            panel.add(createStatCard("检测记录", String.valueOf(recordCount), new Color(46, 204, 113)));
            
            // 预警信息数
            WarningService warningService = new WarningService();
            int warningCount = warningService.getActiveWarnings();
            panel.add(createStatCard("待处理预警", String.valueOf(warningCount), new Color(231, 76, 60)));
            
            // 设备总数
            EquipmentService equipmentService = new EquipmentService();
            int equipmentCount = equipmentService.getTotalCount();
            panel.add(createStatCard("设备总数", String.valueOf(equipmentCount), new Color(241, 196, 15)));
            
        } catch (Exception e) {
            e.printStackTrace();
            // 如果加载失败，显示默认值
            for (int i = 0; i < 4; i++) {
                panel.add(createStatCard("统计项", "-", Color.GRAY));
            }
        }
        
        return panel;
    }
    
    /**
     * 创建统计卡片
     */
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = UIStyles.createCardPanel(color.brighter());
        card.setLayout(new BorderLayout(5, 5));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(UIStyles.FONT_HEADING);
        titleLabel.setForeground(color.darker());
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
        valueLabel.setForeground(color.darker());
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * 创建图表区域
     */
    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // 左侧：最近检测记录
        panel.add(createRecentRecordsPanel());
        
        // 右侧：系统信息
        panel.add(createSystemInfoPanel());
        
        return panel;
    }
    
    /**
     * 创建最近检测记录面板
     */
    private JScrollPane createRecentRecordsPanel() {
        String[] columnNames = {"ID", "水厂", "采样时间", "结论"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableModel);
        table.setFont(UIStyles.FONT_SMALL);
        table.setRowHeight(32);
        table.setSelectionBackground(UIStyles.PRIMARY_LIGHT);
        table.setGridColor(UIStyles.BORDER_COLOR);
        
        try {
            WaterQualityService qualityService = new WaterQualityService();
            List<WaterQualityRecord> records = qualityService.getRecentRecords(10);
            
            for (WaterQualityRecord record : records) {
                Object[] row = {
                    record.getRecordId(),
                    record.getPlantName() != null ? record.getPlantName() : "-",
                    record.getSampleTime() != null ? record.getSampleTime().substring(0, 10) : "-",
                    "qualified".equals(record.getConclusion()) ? "合格" : "不合格"
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_COLOR),
                "最近检测记录",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                UIStyles.FONT_HEADING,
                UIStyles.TEXT_PRIMARY
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        return scrollPane;
    }
    
    /**
     * 创建系统信息面板
     */
    private JPanel createSystemInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // 当前用户
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(createInfoLabel("当前用户:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(createInfoValue(currentUser.getRealName()), gbc);
        
        // 角色
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(createInfoLabel("角色:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(createInfoValue(getRoleName(currentUser.getRole())), gbc);
        
        // 登录时间
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(createInfoLabel("登录时间:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(createInfoValue(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())), gbc);
        
        // 系统运行时间
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(createInfoLabel("系统运行时间:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(createInfoValue(getUptime()), gbc);
        
        // 数据库连接状态
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(createInfoLabel("数据库状态:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        boolean connected = com.ruralwater.util.DBUtil.testConnection();
        JLabel statusLabel = createInfoValue(connected ? "✓ 已连接" : "✗ 未连接");
        statusLabel.setForeground(connected ? UIStyles.SUCCESS_COLOR : UIStyles.DANGER_COLOR);
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        panel.add(statusLabel, gbc);
        
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_COLOR),
                "系统信息",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                UIStyles.FONT_HEADING,
                UIStyles.TEXT_PRIMARY
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        return panel;
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.BOLD, 13));
        return label;
    }
    
    private JLabel createInfoValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        return label;
    }
    
    private String getUptime() {
        long uptime = java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime();
        long hours = uptime / (1000 * 60 * 60);
        long minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60);
        return hours + "小时 " + minutes + "分钟";
    }
    
    /**
     * 更新状态栏
     */
    public void updateStatus(String message) {
        if (statusLabel != null) {
            SwingUtilities.invokeLater(() -> statusLabel.setText(message));
        }
    }
    
    /**
     * 安全地获取 MainFrame 实例
     */
    public static MainFrame getMainFrame(Component component) {
        Window window = SwingUtilities.getWindowAncestor(component);
        if (window instanceof MainFrame) {
            return (MainFrame) window;
        }
        return null;
    }
}
