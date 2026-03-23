package com.ruralwater.ui;

import com.ruralwater.entity.Equipment;
import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterPlant;
import com.ruralwater.service.EquipmentService;
import com.ruralwater.service.WaterPlantService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 设备管理面板
 */
public class EquipmentPanel extends JPanel {
    
    private User currentUser;
    private JTable equipmentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> plantCombo;
    private JComboBox<String> statusCombo;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    private EquipmentService equipmentService = new EquipmentService();
    
    public EquipmentPanel(User user) {
        this.currentUser = user;
        initUI();
        loadData();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 顶部工具栏
        JPanel toolbarPanel = createToolbarPanel();
        add(toolbarPanel, BorderLayout.NORTH);
        
        // 表格面板
        JScrollPane scrollPane = createTablePanel();
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * 创建工具栏面板
     */
    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("操作"));
        
        JLabel plantLabel = new JLabel("水厂：");
        panel.add(plantLabel);
        
        plantCombo = new JComboBox<>();
        plantCombo.setPreferredSize(new Dimension(150, 25));
        loadPlantCombo();
        panel.add(plantCombo);
        
        JLabel statusLabel = new JLabel("状态：");
        panel.add(statusLabel);
        
        statusCombo = new JComboBox<>();
        statusCombo.addItem("全部");
        statusCombo.addItem("normal");
        statusCombo.addItem("maintenance");
        statusCombo.addItem("faulty");
        statusCombo.addItem("scrapped");
        statusCombo.setPreferredSize(new Dimension(120, 25));
        panel.add(statusCombo);
        
        searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> doSearch());
        panel.add(searchButton);
        
        panel.add(Box.createHorizontalStrut(20));
        
        addButton = new JButton("新增");
        addButton.addActionListener(e -> doAdd());
        panel.add(addButton);
        
        editButton = new JButton("编辑");
        editButton.addActionListener(e -> doEdit());
        panel.add(editButton);
        
        deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> doDelete());
        panel.add(deleteButton);
        
        panel.add(Box.createHorizontalStrut(20));
        
        refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadData());
        panel.add(refreshButton);
        
        return panel;
    }
    
    /**
     * 加载水厂下拉框
     */
    private void loadPlantCombo() {
        try {
            plantCombo.removeAllItems();
            plantCombo.addItem("全部");
            WaterPlantService plantService = new WaterPlantService();
            List<WaterPlant> plants = plantService.getPlantsByPage(1, 100);
            for (WaterPlant plant : plants) {
                plantCombo.addItem(plant.getPlantName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建表格面板
     */
    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "设备名称", "设备类型", "型号", "所属水厂", 
                               "购买日期", "保修期", "状态", "上次维护", "下次维护", "备注"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        equipmentTable = new JTable(tableModel);
        equipmentTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        equipmentTable.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(equipmentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("设备列表"));
        
        return scrollPane;
    }
    
    /**
     * 加载数据
     */
    private void loadData() {
        try {
            List<Equipment> equipments = equipmentService.getEquipmentsByPage(1, 100);
            updateTable(equipments);
            
            int count = equipments.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("加载完成，共 " + count + " 台设备");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 更新表格数据
     */
    private void updateTable(List<Equipment> equipments) {
        tableModel.setRowCount(0);
        
        for (Equipment e : equipments) {
            Object[] row = {
                e.getEquipmentId(),
                e.getEquipmentName(),
                e.getEquipmentType() != null ? e.getEquipmentType() : "-",
                e.getModel() != null ? e.getModel() : "-",
                e.getPlantName() != null ? e.getPlantName() : "-",
                e.getPurchaseDate() != null ? e.getPurchaseDate() : "-",
                e.getWarrantyPeriod() != null ? e.getWarrantyPeriod() + "月" : "-",
                e.getStatusText(),
                e.getLastMaintenanceDate() != null ? e.getLastMaintenanceDate() : "-",
                e.getNextMaintenanceDate() != null ? e.getNextMaintenanceDate() : "-",
                e.getRemark() != null ? e.getRemark() : "-"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * 搜索
     */
    private void doSearch() {
        try {
            Integer plantId = null;
            String selectedPlant = (String) plantCombo.getSelectedItem();
            if (selectedPlant != null && !"全部".equals(selectedPlant)) {
                WaterPlantService plantService = new WaterPlantService();
                List<WaterPlant> plants = plantService.searchPlants(selectedPlant);
                for (WaterPlant plant : plants) {
                    if (plant.getPlantName().equals(selectedPlant)) {
                        plantId = plant.getPlantId();
                        break;
                    }
                }
            }
            
            String status = null;
            String selectedStatus = (String) statusCombo.getSelectedItem();
            if (selectedStatus != null && !"全部".equals(selectedStatus)) {
                status = selectedStatus;
            }
            
            List<Equipment> equipments = equipmentService.getEquipmentsByPage(1, 100);
            
            // 内存过滤（实际应该由 DAO 层完成）
            if (plantId != null || status != null) {
                final Integer finalPlantId = plantId;
                final String finalStatus = status;
                equipments.removeIf(e -> {
                    if (finalPlantId != null && !finalPlantId.equals(e.getPlantId())) {
                        return true;
                    }
                    if (finalStatus != null && !finalStatus.equals(e.getStatus())) {
                        return true;
                    }
                    return false;
                });
            }
            
            updateTable(equipments);
            
            int count = equipments.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("查询完成，共找到 " + count + " 台设备");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
        
        AddEquipmentDialog dialog = new AddEquipmentDialog((Frame) SwingUtilities.getWindowAncestor(this), currentUser);
        dialog.setVisible(true);
        
        if (dialog.isAdded()) {
            JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("添加了新设备");
            }
        }
    }
    
    /**
     * 编辑
     */
    private void doEdit() {
        int selectedRow = equipmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的设备", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer equipmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        if (!"admin".equals(currentUser.getRole()) && !"operator".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员和操作员可以编辑", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        EditEquipmentDialog dialog = new EditEquipmentDialog((Frame) SwingUtilities.getWindowAncestor(this), equipmentId, currentUser);
        dialog.setVisible(true);
        
        if (dialog.isEdited()) {
            JOptionPane.showMessageDialog(this, "编辑成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("编辑了设备 #" + equipmentId);
            }
        }
    }
    
    /**
     * 删除
     */
    private void doDelete() {
        int selectedRow = equipmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的设备", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以删除", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer equipmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        int result = JOptionPane.showConfirmDialog(this, "确定要删除该设备吗？", "确认删除", 
                                                    JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                equipmentService.deleteEquipment(equipmentId);
                JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("删除了设备 #" + equipmentId);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

/**
 * 新增设备对话框
 */
class AddEquipmentDialog extends JDialog {
    private boolean added = false;
    private User currentUser;
    
    public AddEquipmentDialog(Frame owner, User user) {
        super(owner, "新增设备", true);
        this.currentUser = user;
        initUI();
    }
    
    private void initUI() {
        setSize(500, 450);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 设备名称
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("设备名称："), gbc);
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(nameField, gbc);
        
        // 设备类型
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("设备类型："), gbc);
        JComboBox<String> typeCombo = new JComboBox<>();
        typeCombo.addItem("泵");
        typeCombo.addItem("阀门");
        typeCombo.addItem("传感器");
        typeCombo.addItem("监控设备");
        typeCombo.addItem("其他");
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(typeCombo, gbc);
        
        // 型号
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("型号："), gbc);
        JTextField modelField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(modelField, gbc);
        
        // 所属水厂
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("所属水厂："), gbc);
        JComboBox<String> plantCombo = new JComboBox<>();
        try {
            com.ruralwater.service.WaterPlantService plantService = new com.ruralwater.service.WaterPlantService();
            java.util.List<com.ruralwater.entity.WaterPlant> plants = plantService.getPlantsByPage(1, 100);
            for (com.ruralwater.entity.WaterPlant plant : plants) {
                plantCombo.addItem(plant.getPlantName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(plantCombo, gbc);
        
        // 购买日期
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("购买日期："), gbc);
        JTextField purchaseDateField = new JTextField(20);
        purchaseDateField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(purchaseDateField, gbc);
        
        // 保修期（月）
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("保修期（月）："), gbc);
        JTextField warrantyField = new JTextField(20);
        warrantyField.setText("12");
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(warrantyField, gbc);
        
        // 状态
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("状态："), gbc);
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("normal");
        statusCombo.addItem("maintenance");
        statusCombo.addItem("faulty");
        statusCombo.addItem("scrapped");
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(statusCombo, gbc);
        
        // 备注
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("备注："), gbc);
        JTextArea remarkArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(remarkArea);
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(scrollPane, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                String selectedPlant = (String) plantCombo.getSelectedItem();
                com.ruralwater.service.WaterPlantService plantService = new com.ruralwater.service.WaterPlantService();
                java.util.List<com.ruralwater.entity.WaterPlant> plants = plantService.searchPlants(selectedPlant);
                Integer plantId = null;
                for (com.ruralwater.entity.WaterPlant plant : plants) {
                    if (plant.getPlantName().equals(selectedPlant)) {
                        plantId = plant.getPlantId();
                        break;
                    }
                }
                
                Equipment equipment = new Equipment();
                equipment.setEquipmentName(nameField.getText().trim());
                equipment.setEquipmentType((String) typeCombo.getSelectedItem());
                equipment.setModel(modelField.getText().trim());
                equipment.setPlantId(plantId);
                equipment.setPurchaseDate(purchaseDateField.getText().trim());
                equipment.setWarrantyPeriod(Integer.parseInt(warrantyField.getText().trim()));
                equipment.setStatus((String) statusCombo.getSelectedItem());
                equipment.setRemark(remarkArea.getText().trim());
                
                EquipmentService equipmentService = new EquipmentService();
                equipmentService.addEquipment(equipment);
                
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
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public boolean isAdded() {
        return added;
    }
}

/**
 * 编辑设备对话框
 */
class EditEquipmentDialog extends JDialog {
    private boolean edited = false;
    private Equipment equipment;
    
    public EditEquipmentDialog(Frame owner, Integer equipmentId, User currentUser) {
        super(owner, "编辑设备 #" + equipmentId, true);
        initUI(equipmentId);
    }
    
    private void initUI(Integer equipmentId) {
        setSize(500, 450);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        try {
            EquipmentService equipmentService = new EquipmentService();
            equipment = equipmentService.getEquipmentById(equipmentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (equipment == null) {
            JOptionPane.showMessageDialog(this, "未找到设备信息", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // 设备名称
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("设备名称："), gbc);
        JTextField nameField = new JTextField(20);
        nameField.setText(equipment.getEquipmentName());
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(nameField, gbc);
        
        // 设备类型
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("设备类型："), gbc);
        JComboBox<String> typeCombo = new JComboBox<>();
        typeCombo.addItem("泵");
        typeCombo.addItem("阀门");
        typeCombo.addItem("传感器");
        typeCombo.addItem("监控设备");
        typeCombo.addItem("其他");
        typeCombo.setSelectedItem(equipment.getEquipmentType());
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(typeCombo, gbc);
        
        // 型号
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("型号："), gbc);
        JTextField modelField = new JTextField(20);
        modelField.setText(equipment.getModel());
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(modelField, gbc);
        
        // 购买日期
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("购买日期："), gbc);
        JTextField purchaseDateField = new JTextField(20);
        purchaseDateField.setText(equipment.getPurchaseDate());
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(purchaseDateField, gbc);
        
        // 保修期
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("保修期（月）："), gbc);
        JTextField warrantyField = new JTextField(20);
        warrantyField.setText(String.valueOf(equipment.getWarrantyPeriod()));
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(warrantyField, gbc);
        
        // 状态
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("状态："), gbc);
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("normal");
        statusCombo.addItem("maintenance");
        statusCombo.addItem("faulty");
        statusCombo.addItem("scrapped");
        statusCombo.setSelectedItem(equipment.getStatus());
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(statusCombo, gbc);
        
        // 上次维护日期
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("上次维护："), gbc);
        JTextField lastMaintField = new JTextField(20);
        lastMaintField.setText(equipment.getLastMaintenanceDate());
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(lastMaintField, gbc);
        
        // 下次维护日期
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("下次维护："), gbc);
        JTextField nextMaintField = new JTextField(20);
        nextMaintField.setText(equipment.getNextMaintenanceDate());
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(nextMaintField, gbc);
        
        // 备注
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("备注："), gbc);
        JTextArea remarkArea = new JTextArea(5, 20);
        remarkArea.setText(equipment.getRemark());
        JScrollPane scrollPane = new JScrollPane(remarkArea);
        gbc.gridx = 1; gbc.gridy = 8;
        formPanel.add(scrollPane, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String type = (String) typeCombo.getSelectedItem();
                String model = modelField.getText().trim();
                String purchaseDate = purchaseDateField.getText().trim();
                int warranty = Integer.parseInt(warrantyField.getText().trim());
                String status = (String) statusCombo.getSelectedItem();
                String lastMaintDate = lastMaintField.getText().trim();
                String nextMaintDate = nextMaintField.getText().trim();
                String remark = remarkArea.getText().trim();
                
                equipment.setEquipmentName(name);
                equipment.setEquipmentType(type);
                equipment.setModel(model);
                equipment.setPurchaseDate(purchaseDate);
                equipment.setWarrantyPeriod(warranty);
                equipment.setStatus(status);
                equipment.setLastMaintenanceDate(lastMaintDate);
                equipment.setNextMaintenanceDate(nextMaintDate);
                equipment.setRemark(remark);
                
                EquipmentService equipmentService = new EquipmentService();
                equipmentService.updateEquipment(equipment);
                
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
