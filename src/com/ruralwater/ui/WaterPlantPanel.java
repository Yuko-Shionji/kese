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
        
        JOptionPane.showMessageDialog(this, "新增功能开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
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
        
        JOptionPane.showMessageDialog(this, "编辑功能开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
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
