package com.ruralwater.ui;

import com.ruralwater.entity.Region;
import com.ruralwater.entity.User;
import com.ruralwater.service.RegionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 区域管理面板
 */
public class RegionManagePanel extends JPanel {
    
    private User currentUser;
    private JTable regionTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> levelCombo;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    private RegionService regionService = new RegionService();
    
    public RegionManagePanel(User user) {
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
        
        JLabel levelLabel = new JLabel("级别：");
        panel.add(levelLabel);
        
        levelCombo = new JComboBox<>();
        levelCombo.addItem("全部");
        levelCombo.addItem("province");
        levelCombo.addItem("city");
        levelCombo.addItem("district");
        levelCombo.addItem("town");
        levelCombo.addItem("village");
        panel.add(levelCombo);
        
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
        String[] columnNames = {"ID", "区域编码", "区域名称", "级别", "父 ID", "完整路径"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        regionTable = new JTable(tableModel);
        regionTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        regionTable.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(regionTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("区域列表"));
        
        return scrollPane;
    }
    
    private void loadData() {
        try {
            List<Region> regions = regionService.searchRegions(null);
            updateTable(regions);
            
            int count = regions.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("加载完成，共 " + count + " 个区域");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateTable(List<Region> regions) {
        tableModel.setRowCount(0);
        
        for (Region r : regions) {
            Object[] row = {
                r.getRegionId(),
                r.getRegionCode(),
                r.getRegionName(),
                r.getLevel(),
                r.getParentId() != null ? r.getParentId() : 0,
                r.getFullPath() != null ? r.getFullPath() : "-"
            };
            tableModel.addRow(row);
        }
    }
    
    private void doSearch() {
        try {
            String level = null;
            String selectedLevel = (String) levelCombo.getSelectedItem();
            if (selectedLevel != null && !"全部".equals(selectedLevel)) {
                level = selectedLevel;
            }
            
            List<Region> regions;
            if (level != null) {
                regions = regionService.getRegionsByLevel(level);
            } else {
                regions = regionService.searchRegions(null);
            }
            
            updateTable(regions);
            
            int count = regions.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("查询完成，共找到 " + count + " 个区域");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void doAdd() {
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以添加", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AddRegionDialog dialog = new AddRegionDialog((Frame) SwingUtilities.getWindowAncestor(this), currentUser);
        dialog.setVisible(true);
        
        if (dialog.isAdded()) {
            JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("添加了新区域");
            }
        }
    }
    
    private void doEdit() {
        int selectedRow = regionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的区域", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以编辑", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer regionId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        EditRegionDialog dialog = new EditRegionDialog((Frame) SwingUtilities.getWindowAncestor(this), regionId, currentUser);
        dialog.setVisible(true);
        
        if (dialog.isEdited()) {
            JOptionPane.showMessageDialog(this, "编辑成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("编辑了区域 #" + regionId);
            }
        }
    }
    
    private void doDelete() {
        int selectedRow = regionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的区域", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以删除", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer regionId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        int result = JOptionPane.showConfirmDialog(this, "确定要删除该区域吗？", "确认删除", 
                                                    JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                regionService.deleteRegion(regionId);
                JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("删除了区域 #" + regionId);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
