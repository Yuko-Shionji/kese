package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterQualityStandard;
import com.ruralwater.service.WaterQualityStandardService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 水质检测标准管理面板
 */
public class StandardManagePanel extends JPanel {
    
    private User currentUser;
    private JTable standardTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryCombo;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    private WaterQualityStandardService standardService = new WaterQualityStandardService();
    
    public StandardManagePanel(User user) {
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
        
        JLabel categoryLabel = new JLabel("类别：");
        panel.add(categoryLabel);
        
        categoryCombo = new JComboBox<>();
        categoryCombo.addItem("全部");
        categoryCombo.addItem("physical");
        categoryCombo.addItem("chemical");
        categoryCombo.addItem("biological");
        categoryCombo.addItem("radiological");
        panel.add(categoryCombo);
        
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
        String[] columnNames = {"ID", "检测项名称", "编码", "单位", "标准值", 
                               "最小值", "最大值", "类别", "说明", "状态"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        standardTable = new JTable(tableModel);
        standardTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        standardTable.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(standardTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("检测标准列表"));
        
        return scrollPane;
    }
    
    private void loadData() {
        try {
            List<WaterQualityStandard> standards = standardService.searchStandards(null);
            updateTable(standards);
            
            int count = standards.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("加载完成，共 " + count + " 个检测标准");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateTable(List<WaterQualityStandard> standards) {
        tableModel.setRowCount(0);
        
        for (WaterQualityStandard s : standards) {
            Object[] row = {
                s.getStandardId(),
                s.getItemName(),
                s.getItemCode(),
                s.getUnit() != null ? s.getUnit() : "-",
                s.getStandardValue() != null ? s.getStandardValue() : "-",
                s.getMinValue() != null ? s.getMinValue() : "-",
                s.getMaxValue() != null ? s.getMaxValue() : "-",
                s.getCategory() != null ? s.getCategory() : "-",
                s.getDescription() != null ? s.getDescription() : "-",
                s.getIsActive() == 1 ? "启用" : "停用"
            };
            tableModel.addRow(row);
        }
    }
    
    private void doSearch() {
        try {
            String category = null;
            String selectedCategory = (String) categoryCombo.getSelectedItem();
            if (selectedCategory != null && !"全部".equals(selectedCategory)) {
                category = selectedCategory;
            }
            
            List<WaterQualityStandard> standards;
            if (category != null) {
                standards = standardService.getStandardsByCategory(category);
            } else {
                standards = standardService.searchStandards(null);
            }
            
            updateTable(standards);
            
            int count = standards.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("查询完成，共找到 " + count + " 个检测标准");
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
        
        AddStandardDialog dialog = new AddStandardDialog((Frame) SwingUtilities.getWindowAncestor(this), currentUser);
        dialog.setVisible(true);
        
        if (dialog.isAdded()) {
            JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("添加了新检测标准");
            }
        }
    }
    
    private void doEdit() {
        int selectedRow = standardTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的检测标准", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以编辑", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer standardId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        EditStandardDialog dialog = new EditStandardDialog((Frame) SwingUtilities.getWindowAncestor(this), standardId, currentUser);
        dialog.setVisible(true);
        
        if (dialog.isEdited()) {
            JOptionPane.showMessageDialog(this, "编辑成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("编辑了检测标准 #" + standardId);
            }
        }
    }
    
    private void doDelete() {
        int selectedRow = standardTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的检测标准", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "权限不足，只有管理员可以删除", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer standardId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        int result = JOptionPane.showConfirmDialog(this, "确定要删除该检测标准吗？", "确认删除", 
                                                    JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                standardService.deleteStandard(standardId);
                JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("删除了检测标准 #" + standardId);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
