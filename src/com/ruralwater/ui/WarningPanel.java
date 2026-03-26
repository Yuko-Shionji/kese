package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.Warning;
import com.ruralwater.entity.WaterPlant;
import com.ruralwater.service.WaterPlantService;
import com.ruralwater.service.WarningService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 预警信息管理面板
 */
public class WarningPanel extends JPanel {
    
    private User currentUser;
    private JTable warningTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> levelCombo;
    private JComboBox<String> statusCombo;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton handleButton;
    private JButton refreshButton;
    
    private WarningService warningService = new WarningService();
    
    public WarningPanel(User user) {
        this.currentUser = user;
        initUI();
        loadData();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 查询面板
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        // 表格面板
        JScrollPane scrollPane = createTablePanel();
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * 创建查询面板
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("查询条件"));
        
        panel.add(new JLabel("预警级别："));
        levelCombo = new JComboBox<>();
        levelCombo.addItem("全部");
        levelCombo.addItem("low");
        levelCombo.addItem("medium");
        levelCombo.addItem("high");
        levelCombo.addItem("critical");
        panel.add(levelCombo);
        
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("状态："));
        statusCombo = new JComboBox<>();
        statusCombo.addItem("全部");
        statusCombo.addItem("active");
        statusCombo.addItem("processed");
        statusCombo.addItem("ignored");
        panel.add(statusCombo);
        
        panel.add(Box.createHorizontalStrut(10));
        searchButton = new JButton("查询");
        searchButton.addActionListener(e -> doSearch());
        panel.add(searchButton);
        
        panel.add(Box.createHorizontalStrut(20));
        
        addButton = new JButton("新增预警");
        addButton.addActionListener(e -> doAdd());
        panel.add(addButton);
        
        editButton = new JButton("编辑预警");
        editButton.addActionListener(e -> doEdit());
        panel.add(editButton);
        
        deleteButton = new JButton("删除预警");
        deleteButton.addActionListener(e -> doDelete());
        panel.add(deleteButton);
        
        panel.add(Box.createHorizontalStrut(20));
        
        handleButton = new JButton("处理");
        handleButton.addActionListener(e -> doHandle());
        panel.add(handleButton);
        
        panel.add(Box.createHorizontalStrut(20));
        
        refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadData());
        panel.add(refreshButton);
        
        return panel;
    }
    
    /**
     * 创建表格面板
     */
    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "水厂", "预警类型", "预警级别", "标题", "内容", 
                               "状态", "处理人", "处理时间", "创建时间"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        warningTable = new JTable(tableModel);
        warningTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        warningTable.setRowHeight(30);
        warningTable.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(warningTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("预警信息列表"));
        
        return scrollPane;
    }
    
    /**
     * 加载数据
     */
    private void loadData() {
        try {
            List<Warning> warnings = warningService.getWarningsByCondition(
                null, null, null, null, 1, 100);
            updateTable(warnings);
            int count = warnings.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("加载完成，共 " + count + " 条预警信息");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 更新表格数据
     */
    private void updateTable(List<Warning> warnings) {
        tableModel.setRowCount(0);
        
        for (Warning w : warnings) {
            Object[] row = {
                w.getWarningId(),
                w.getPlantName() != null ? w.getPlantName() : "-",
                getWarningTypeText(w.getWarningType()),
                getWarningLevelText(w.getWarningLevel()),
                w.getTitle() != null ? w.getTitle() : "-",
                w.getContent() != null ? w.getContent() : "-",
                getStatusText(w.getStatus()),
                w.getHandlerName() != null ? w.getHandlerName() : "-",
                w.getHandleTime() != null ? w.getHandleTime() : "-",
                w.getCreateTime() != null ? w.getCreateTime() : "-"
            };
            tableModel.addRow(row);
        }
    }
    
    private String getWarningTypeText(String type) {
        switch (type) {
            case "quality": return "水质";
            case "equipment": return "设备";
            case "supply": return "供水";
            default: return type;
        }
    }
    
    private String getWarningLevelText(String level) {
        switch (level) {
            case "low": return "低";
            case "medium": return "中";
            case "high": return "高";
            case "critical": return "严重";
            default: return level;
        }
    }
    
    private String getStatusText(String status) {
        switch (status) {
            case "active": return "待处理";
            case "processed": return "已处理";
            case "ignored": return "已忽略";
            default: return status;
        }
    }
    
    private void doSearch() {
        try {
            String warningType = null;
            String selectedLevel = (String) levelCombo.getSelectedItem();
            String warningLevel = null;
            if (!"全部".equals(selectedLevel)) {
                warningLevel = selectedLevel;
            }
            
            String selectedStatus = (String) statusCombo.getSelectedItem();
            String status = null;
            if (!"全部".equals(selectedStatus)) {
                status = selectedStatus;
            }
            
            List<Warning> warnings = warningService.getWarningsByCondition(
                warningType, warningLevel, status, null, 1, 100);
            updateTable(warnings);
            
            int count = warnings.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("查询完成，共找到 " + count + " 条预警");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void doHandle() {
        int selectedRow = warningTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要处理的预警", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer warningId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 6);
        
        if ("已处理".equals(status) || "已忽略".equals(status)) {
            JOptionPane.showMessageDialog(this, "该预警已处理过，无需重复处理", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            HandleWarningDialog dialog = new HandleWarningDialog(owner, warningId, currentUser);
            dialog.setVisible(true);
            
            if (dialog.isHandled()) {
                JOptionPane.showMessageDialog(this, "处理成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("处理了预警信息 #" + warningId);
                }
            }
        }
    }
    
    /**
     * 新增
     */
    private void doAdd() {
        if (!"admin".equals(currentUser.getRole()) && !"operator".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员和操作员可以添加", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            AddWarningDialog dialog = new AddWarningDialog(owner, currentUser);
            dialog.setVisible(true);
            
            if (dialog.isAdded()) {
                JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("添加了新预警信息");
                }
            }
        }
    }
    
    /**
     * 编辑
     */
    private void doEdit() {
        int selectedRow = warningTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的预警", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"admin".equals(currentUser.getRole()) && !"operator".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员和操作员可以编辑", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer warningId = (Integer) tableModel.getValueAt(selectedRow, 0);
        try {
            Warning warning = warningService.getWarningById(warningId);
            Frame owner = MainFrame.getMainFrame(this);
            if (owner != null) {
                EditWarningDialog dialog = new EditWarningDialog(owner, warning, currentUser);
                dialog.setVisible(true);
                
                if (dialog.isEdited()) {
                    JOptionPane.showMessageDialog(this, "编辑成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    MainFrame mainFrame = MainFrame.getMainFrame(this);
                    if (mainFrame != null) {
                        mainFrame.updateStatus("编辑了预警信息 #" + warningId);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "编辑失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 删除
     */
    private void doDelete() {
        int selectedRow = warningTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的预警", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以删除", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer warningId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(this, "确定要删除该预警吗？", "确认删除", 
                                                    JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                warningService.deleteWarning(warningId);
                JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("删除了预警信息 #" + warningId);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

/**
 * 处理预警对话框
 */
class HandleWarningDialog extends JDialog {
    private boolean handled = false;
    
    public HandleWarningDialog(Frame owner, Integer warningId, User currentUser) {
        super(owner, "处理预警 #" + warningId, true);
        initUI(warningId, currentUser);
    }
    
    private void initUI(Integer warningId, User currentUser) {
        setSize(500, 350);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("处理结果："), gbc);
        
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("processed");
        statusCombo.addItem("ignored");
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(statusCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("处理意见："), gbc);
        
        JTextArea opinionArea = new JTextArea(8, 25);
        JScrollPane scrollPane = new JScrollPane(opinionArea);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(scrollPane, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                String status = (String) statusCombo.getSelectedItem();
                String opinion = opinionArea.getText().trim();
                
                if (opinion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "请输入处理意见", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                WarningService warningService = new WarningService();
                warningService.handleWarning(warningId, currentUser.getUserId(), opinion, status);
                
                handled = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "处理失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(confirmBtn);
        
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public boolean isHandled() {
        return handled;
    }
}

/**
 * 新增预警对话框
 */
class AddWarningDialog extends JDialog {
    private boolean added = false;
    private User currentUser;
    
    public AddWarningDialog(Frame owner, User user) {
        super(owner, "新增预警信息", true);
        this.currentUser = user;
        initUI();
    }
    
    private void initUI() {
        setSize(600, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 水厂选择
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("水厂："), gbc);
        JComboBox<Integer> plantCombo = new JComboBox<>();
        loadWaterPlants(plantCombo);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(plantCombo, gbc);
        
        // 预警类型
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("预警类型："), gbc);
        JComboBox<String> typeCombo = new JComboBox<>();
        typeCombo.addItem("quality");
        typeCombo.addItem("equipment");
        typeCombo.addItem("supply");
        typeCombo.addItem("other");
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(typeCombo, gbc);
        
        // 预警级别
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("预警级别："), gbc);
        JComboBox<String> levelCombo = new JComboBox<>();
        levelCombo.addItem("low");
        levelCombo.addItem("medium");
        levelCombo.addItem("high");
        levelCombo.addItem("critical");
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(levelCombo, gbc);
        
        // 标题
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("标题："), gbc);
        JTextField titleField = new JTextField(30);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(titleField, gbc);
        
        // 内容
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("内容："), gbc);
        JTextArea contentArea = new JTextArea(8, 30);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(scrollPane, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                Integer plantId = (Integer) plantCombo.getSelectedItem();
                System.out.println("Selected plantId: " + plantId);
                if (plantId == null || plantId <= 0) {
                    JOptionPane.showMessageDialog(this, "请选择水厂", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String title = titleField.getText().trim();
                String content = contentArea.getText().trim();
                
                if (title.isEmpty() || content.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "标题和内容不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Warning warning = new Warning();
                warning.setPlantId(plantId);
                warning.setWarningType((String) typeCombo.getSelectedItem());
                warning.setWarningLevel((String) levelCombo.getSelectedItem());
                warning.setTitle(title);
                warning.setContent(content);
                warning.setStatus("active");
                
                System.out.println("Creating warning with plantId: " + plantId);
                
                WarningService warningService = new WarningService();
                warningService.addWarning(warning);
                
                added = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        buttonPanel.add(confirmBtn);
        
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public boolean isAdded() {
        return added;
    }
    
    /**
     * 加载水厂列表
     */
    private void loadWaterPlants(JComboBox<Integer> combo) {
        try {
            WaterPlantService plantService = new WaterPlantService();
            List<WaterPlant> plants = plantService.getAllWaterPlants();
            
            combo.removeAllItems();
            if (plants == null || plants.isEmpty()) {
                JOptionPane.showMessageDialog(this, "没有可用的水厂信息，请先添加水厂", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            for (WaterPlant plant : plants) {
                combo.addItem(plant.getPlantId());
            }
            
            // 设置渲染器来直接显示水厂 ID
            combo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, 
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Integer) {
                        setText(value.toString());
                    }
                    return this;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("加载水厂列表失败：" + e.getMessage());
            // 不弹出错误框，避免阻止对话框显示
        }
    }
}

/**
 * 编辑预警对话框
 */
class EditWarningDialog extends JDialog {
    private boolean edited = false;
    private Warning warning;
    
    public EditWarningDialog(Frame owner, Warning warning, User currentUser) {
        super(owner, "编辑预警 #" + warning.getWarningId(), true);
        this.warning = warning;
        initUI();
    }
    
    private void initUI() {
        setSize(600, 450);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 预警类型
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("预警类型："), gbc);
        JComboBox<String> typeCombo = new JComboBox<>();
        typeCombo.addItem("quality");
        typeCombo.addItem("equipment");
        typeCombo.addItem("supply");
        typeCombo.setSelectedItem(warning.getWarningType());
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(typeCombo, gbc);
        
        // 预警级别
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("预警级别："), gbc);
        JComboBox<String> levelCombo = new JComboBox<>();
        levelCombo.addItem("low");
        levelCombo.addItem("medium");
        levelCombo.addItem("high");
        levelCombo.addItem("critical");
        levelCombo.setSelectedItem(warning.getWarningLevel());
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(levelCombo, gbc);
        
        // 标题
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("标题："), gbc);
        JTextField titleField = new JTextField(30);
        titleField.setText(warning.getTitle() != null ? warning.getTitle() : "");
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(titleField, gbc);
        
        // 内容
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("内容："), gbc);
        JTextArea contentArea = new JTextArea(8, 30);
        contentArea.setText(warning.getContent() != null ? warning.getContent() : "");
        JScrollPane scrollPane = new JScrollPane(contentArea);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(scrollPane, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                warning.setWarningType((String) typeCombo.getSelectedItem());
                warning.setWarningLevel((String) levelCombo.getSelectedItem());
                warning.setTitle(titleField.getText().trim());
                warning.setContent(contentArea.getText().trim());
                
                if (warning.getTitle().isEmpty() || warning.getContent().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "标题和内容不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                WarningService warningService = new WarningService();
                warningService.updateWarning(warning);
                
                edited = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "更新失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(confirmBtn);
        
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public boolean isEdited() {
        return edited;
    }
}
