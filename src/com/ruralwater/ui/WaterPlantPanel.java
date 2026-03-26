package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterPlant;
import com.ruralwater.service.WaterPlantService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 水厂管理面板
 */
public class WaterPlantPanel extends JPanel {
    
    private User currentUser;
    private JTable plantTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    private WaterPlantService plantService = new WaterPlantService();
    
    public WaterPlantPanel(User user) {
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
        
        JLabel searchLabel = new JLabel("关键字：");
        panel.add(searchLabel);
        
        searchField = new JTextField(20);
        panel.add(searchField);
        
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
     * 创建表格面板
     */
    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "水厂名称", "水厂编码", "所属区域", "水源", "地址", 
                               "设计规模", "服务人口", "联系人", "联系电话", "状态"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };
        
        plantTable = new JTable(tableModel);
        plantTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        plantTable.setRowHeight(30);
        plantTable.setAutoCreateRowSorter(true); // 支持排序
        
        JScrollPane scrollPane = new JScrollPane(plantTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("水厂列表"));
        
        return scrollPane;
    }
    
    /**
     * 加载数据
     */
    private void loadData() {
        try {
            List<WaterPlant> plants = plantService.getPlantsByPage(1, 100);
            updateTable(plants);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 更新表格数据
     */
    private void updateTable(List<WaterPlant> plants) {
        tableModel.setRowCount(0);
        
        for (WaterPlant plant : plants) {
            Object[] row = {
                plant.getPlantId(),
                plant.getPlantName(),
                plant.getPlantCode(),
                plant.getRegionName() != null ? plant.getRegionName() : "-",
                plant.getSourceName() != null ? plant.getSourceName() : "-",
                plant.getAddress() != null ? plant.getAddress() : "-",
                plant.getDesignCapacity() != null ? plant.getDesignCapacity() + "万吨/日" : "-",
                plant.getServicePopulation() != null ? plant.getServicePopulation() + "人" : "-",
                plant.getContactPerson() != null ? plant.getContactPerson() : "-",
                plant.getContactPhone() != null ? plant.getContactPhone() : "-",
                plant.getStatus() == 1 ? "正常" : "停用"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * 搜索
     */
    private void doSearch() {
        String keyword = searchField.getText().trim();
        
        try {
            List<WaterPlant> plants = plantService.searchPlants(keyword);
            updateTable(plants);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "搜索失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 新增
     */
    private void doAdd() {
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以添加", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            AddWaterPlantDialog dialog = new AddWaterPlantDialog(owner, currentUser);
            dialog.setVisible(true);
            
            if (dialog.isAdded()) {
                JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("添加了新水厂");
                }
            }
        }
    }
    
    /**
     * 编辑
     */
    private void doEdit() {
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以编辑", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = plantTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer plantId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            EditWaterPlantDialog dialog = new EditWaterPlantDialog(owner, plantId, currentUser);
            dialog.setVisible(true);
            
            if (dialog.isEdited()) {
                JOptionPane.showMessageDialog(this, "编辑成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("编辑了水厂 #" + plantId);
                }
            }
        }
    }
    
    /**
     * 删除
     */
    private void doDelete() {
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以删除", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = plantTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, "确定要删除选中的记录吗？", "确认删除", 
                                                    JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                Integer plantId = (Integer) tableModel.getValueAt(selectedRow, 0);
                plantService.deletePlant(plantId);
                JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

/**
 * 新增水厂对话框
 */
class AddWaterPlantDialog extends JDialog {
    private boolean added = false;
    private User currentUser;
    
    public AddWaterPlantDialog(Frame owner, User user) {
        super(owner, "新增水厂", true);
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
        
        // 水厂名称
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("水厂名称："), gbc);
        JTextField nameField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(nameField, gbc);
        
        // 水厂编码
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("水厂编码："), gbc);
        JTextField codeField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(codeField, gbc);
        
        // 所属区域
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("所属区域："), gbc);
        JComboBox<String> regionCombo = new JComboBox<>();
        try {
            com.ruralwater.service.RegionService regionService = new com.ruralwater.service.RegionService();
            // 只查询省份级别的区域
            java.util.List<com.ruralwater.entity.Region> provinces = regionService.getAllProvinces();
            System.out.println("查询到 " + provinces.size() + " 个省份");
            for (com.ruralwater.entity.Region province : provinces) {
                System.out.println("  - " + province.getRegionName());
                regionCombo.addItem(province.getRegionName());
            }
            if (provinces.isEmpty()) {
                JOptionPane.showMessageDialog(this, "数据库中没有省份数据，请先执行 init_provinces.sql 初始化脚本", "提示", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载省份失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(regionCombo, gbc);
        
        // 水源
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("水源："), gbc);
        JTextField sourceField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(sourceField, gbc);
        
        // 地址
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("地址："), gbc);
        JTextField addressField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(addressField, gbc);
        
        // 设计规模
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("设计规模（万吨/日）："), gbc);
        JTextField capacityField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(capacityField, gbc);
        
        // 服务人口
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("服务人口（人）："), gbc);
        JTextField populationField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(populationField, gbc);
        
        // 联系人
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("联系人："), gbc);
        JTextField contactField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(contactField, gbc);
        
        // 联系电话
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("联系电话："), gbc);
        JTextField phoneField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 8;
        formPanel.add(phoneField, gbc);
        
        // 状态
        gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(new JLabel("状态："), gbc);
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("正常");
        statusCombo.addItem("停用");
        gbc.gridx = 1; gbc.gridy = 9;
        formPanel.add(statusCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                String selectedStatus = (String) statusCombo.getSelectedItem();
                int status = "正常".equals(selectedStatus) ? 1 : 0;
                
                WaterPlant plant = new WaterPlant();
                plant.setPlantName(nameField.getText().trim());
                plant.setPlantCode(codeField.getText().trim());
                
                // 设置 regionId
                String selectedRegion = (String) regionCombo.getSelectedItem();
                if (selectedRegion != null) {
                    com.ruralwater.service.RegionService regionService = new com.ruralwater.service.RegionService();
                    java.util.List<com.ruralwater.entity.Region> provinces = null;
                    try {
                        provinces = regionService.getAllProvinces();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (provinces != null) {
                        for (com.ruralwater.entity.Region province : provinces) {
                            if (province.getRegionName().equals(selectedRegion)) {
                                plant.setRegionId(province.getRegionId());
                                break;
                            }
                        }
                    }
                }
                
                plant.setSourceName(sourceField.getText().trim());
                plant.setAddress(addressField.getText().trim());
                
                String capacity = capacityField.getText().trim();
                if (!capacity.isEmpty()) {
                    plant.setDesignCapacity(new java.math.BigDecimal(capacity));
                }
                
                String population = populationField.getText().trim();
                if (!population.isEmpty()) {
                    plant.setServicePopulation(Integer.parseInt(population));
                }
                
                plant.setContactPerson(contactField.getText().trim());
                plant.setContactPhone(phoneField.getText().trim());
                plant.setStatus(status);
                
                WaterPlantService plantService = new WaterPlantService();
                plantService.addWaterPlant(plant);
                
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
 * 编辑水厂对话框
 */
class EditWaterPlantDialog extends JDialog {
    private boolean edited = false;
    private WaterPlant plant;
    
    public EditWaterPlantDialog(Frame owner, Integer plantId, User currentUser) {
        super(owner, "编辑水厂 #" + plantId, true);
        initUI(plantId);
    }
    
    private void initUI(Integer plantId) {
        setSize(600, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        try {
            WaterPlantService plantService = new WaterPlantService();
            plant = plantService.getPlantById(plantId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (plant == null) {
            JOptionPane.showMessageDialog(this, "未找到水厂信息", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // 水厂名称
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("水厂名称："), gbc);
        JTextField nameField = new JTextField(25);
        nameField.setText(plant.getPlantName());
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(nameField, gbc);
        
        // 水厂编码
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("水厂编码："), gbc);
        JTextField codeField = new JTextField(25);
        codeField.setText(plant.getPlantCode());
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(codeField, gbc);
        
        // 所属区域
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("所属区域："), gbc);
        JComboBox<String> regionCombo = new JComboBox<>();
        try {
            com.ruralwater.service.RegionService regionService = new com.ruralwater.service.RegionService();
            // 只查询省份级别的区域
            java.util.List<com.ruralwater.entity.Region> provinces = regionService.getAllProvinces();
            System.out.println("编辑时查询到 " + provinces.size() + " 个省份");
            int selectedIndex = -1;
            for (int i = 0; i < provinces.size(); i++) {
                com.ruralwater.entity.Region province = provinces.get(i);
                regionCombo.addItem(province.getRegionName());
                if (plant.getRegionId() != null && plant.getRegionId().equals(province.getRegionId())) {
                    selectedIndex = i;
                }
            }
            if (selectedIndex >= 0) {
                regionCombo.setSelectedIndex(selectedIndex);
            }
            if (provinces.isEmpty()) {
                JOptionPane.showMessageDialog(this, "数据库中没有省份数据，请先执行 init_provinces.sql 初始化脚本", "提示", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载省份失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(regionCombo, gbc);
        
        // 水源
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("水源："), gbc);
        JTextField sourceField = new JTextField(25);
        sourceField.setText(plant.getSourceName() != null ? plant.getSourceName() : "");
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(sourceField, gbc);
        
        // 地址
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("地址："), gbc);
        JTextField addressField = new JTextField(25);
        addressField.setText(plant.getAddress() != null ? plant.getAddress() : "");
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(addressField, gbc);
        
        // 设计规模
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("设计规模（万吨/日）："), gbc);
        JTextField capacityField = new JTextField(25);
        capacityField.setText(plant.getDesignCapacity() != null ? String.valueOf(plant.getDesignCapacity()) : "");
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(capacityField, gbc);
        
        // 服务人口
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("服务人口（人）："), gbc);
        JTextField populationField = new JTextField(25);
        populationField.setText(plant.getServicePopulation() != null ? String.valueOf(plant.getServicePopulation()) : "");
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(populationField, gbc);
        
        // 联系人
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("联系人："), gbc);
        JTextField contactField = new JTextField(25);
        contactField.setText(plant.getContactPerson() != null ? plant.getContactPerson() : "");
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(contactField, gbc);
        
        // 联系电话
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("联系电话："), gbc);
        JTextField phoneField = new JTextField(25);
        phoneField.setText(plant.getContactPhone() != null ? plant.getContactPhone() : "");
        gbc.gridx = 1; gbc.gridy = 8;
        formPanel.add(phoneField, gbc);
        
        // 状态
        gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(new JLabel("状态："), gbc);
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("正常");
        statusCombo.addItem("停用");
        statusCombo.setSelectedIndex(plant.getStatus() == 1 ? 0 : 1);
        gbc.gridx = 1; gbc.gridy = 9;
        formPanel.add(statusCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                String selectedStatus = (String) statusCombo.getSelectedItem();
                int status = "正常".equals(selectedStatus) ? 1 : 0;
                
                plant.setPlantName(nameField.getText().trim());
                plant.setPlantCode(codeField.getText().trim());
                
                // 设置 regionId
                String selectedRegion = (String) regionCombo.getSelectedItem();
                if (selectedRegion != null) {
                    com.ruralwater.service.RegionService regionService = new com.ruralwater.service.RegionService();
                    java.util.List<com.ruralwater.entity.Region> provinces = null;
                    try {
                        provinces = regionService.getAllProvinces();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (provinces != null) {
                        for (com.ruralwater.entity.Region province : provinces) {
                            if (province.getRegionName().equals(selectedRegion)) {
                                plant.setRegionId(province.getRegionId());
                                break;
                            }
                        }
                    }
                }
                
                plant.setSourceName(sourceField.getText().trim());
                plant.setAddress(addressField.getText().trim());
                
                String capacity = capacityField.getText().trim();
                if (!capacity.isEmpty()) {
                    plant.setDesignCapacity(new java.math.BigDecimal(capacity));
                }
                
                String population = populationField.getText().trim();
                if (!population.isEmpty()) {
                    plant.setServicePopulation(Integer.parseInt(population));
                }
                
                plant.setContactPerson(contactField.getText().trim());
                plant.setContactPhone(phoneField.getText().trim());
                plant.setStatus(status);
                
                WaterPlantService plantService = new WaterPlantService();
                plantService.updateWaterPlant(plant);
                
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
