package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterSource;
import com.ruralwater.service.WaterSourceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 水源管理面板
 */
public class WaterSourcePanel extends JPanel {
    
    private User currentUser;
    private JTable sourceTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> typeCombo;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    private WaterSourceService waterSourceService = new WaterSourceService();
    
    public WaterSourcePanel(User user) {
        this.currentUser = user;
        initUI();
        loadData();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel toolbarPanel = createToolbarPanel();
        add(toolbarPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = createTablePanel();
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("操作"));
        
        JLabel typeLabel = new JLabel("类型：");
        panel.add(typeLabel);
        
        typeCombo = new JComboBox<>();
        typeCombo.addItem("全部");
        typeCombo.addItem("reservoir");
        typeCombo.addItem("well");
        typeCombo.addItem("river");
        typeCombo.addItem("lake");
        typeCombo.addItem("spring");
        panel.add(typeCombo);
        
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
    
    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "水源名称", "类型", "所属区域", "位置", "经度", "纬度", 
                               "容量", "状态"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        sourceTable = new JTable(tableModel);
        sourceTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        sourceTable.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(sourceTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("水源列表"));
        
        return scrollPane;
    }
    
    private void loadData() {
        try {
            List<WaterSource> sources = waterSourceService.searchSources(null);
            updateTable(sources);
            
            int count = sources.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("加载完成，共 " + count + " 个水源");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateTable(List<WaterSource> sources) {
        tableModel.setRowCount(0);
        
        for (WaterSource s : sources) {
            Object[] row = {
                s.getSourceId(),
                s.getSourceName(),
                s.getSourceType(),
                s.getRegionName() != null ? s.getRegionName() : "-",
                s.getLocationDetail() != null ? s.getLocationDetail() : "-",
                s.getLongitude() != null ? s.getLongitude() : "-",
                s.getLatitude() != null ? s.getLatitude() : "-",
                s.getCapacity() != null ? s.getCapacity() : "-",
                s.getStatus() == 1 ? "正常" : "停用"
            };
            tableModel.addRow(row);
        }
    }
    
    private void doSearch() {
        try {
            String sourceType = null;
            String selectedType = (String) typeCombo.getSelectedItem();
            if (selectedType != null && !"全部".equals(selectedType)) {
                sourceType = selectedType;
            }
            
            List<WaterSource> sources;
            if (sourceType != null) {
                sources = waterSourceService.getSourcesByType(sourceType);
            } else {
                sources = waterSourceService.searchSources(null);
            }
            
            updateTable(sources);
            
            int count = sources.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("查询完成，共找到 " + count + " 个水源");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void doAdd() {
        if (!"admin".equals(currentUser.getRole()) && !"operator".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员和操作员可以添加", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AddWaterSourceDialog dialog = new AddWaterSourceDialog((Frame) SwingUtilities.getWindowAncestor(this), currentUser);
        dialog.setVisible(true);
        
        if (dialog.isAdded()) {
            JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("添加了新水源");
            }
        }
    }
    
    private void doEdit() {
        int selectedRow = sourceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的水源", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"admin".equals(currentUser.getRole()) && !"operator".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员和操作员可以编辑", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer sourceId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        EditWaterSourceDialog dialog = new EditWaterSourceDialog((Frame) SwingUtilities.getWindowAncestor(this), sourceId, currentUser);
        dialog.setVisible(true);
        
        if (dialog.isEdited()) {
            JOptionPane.showMessageDialog(this, "编辑成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("编辑了水源 #" + sourceId);
            }
        }
    }
    
    private void doDelete() {
        int selectedRow = sourceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的水源", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以删除", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer sourceId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        int result = JOptionPane.showConfirmDialog(this, "确定要删除该水源吗？", "确认删除", 
                                                    JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                waterSourceService.deleteSource(sourceId);
                JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("删除了水源 #" + sourceId);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
