package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.Warning;

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
    private JButton handleButton;
    private JButton refreshButton;
    
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
        // TODO: 从数据库加载真实数据
        List<Warning> warnings = new ArrayList<>();
        
        // 模拟测试数据
        for (int i = 1; i <= 10; i++) {
            Warning w = new Warning();
            w.setWarningId(i);
            w.setPlantName("合肥" + i + "水厂");
            w.setWarningType("quality");
            w.setWarningLevel(i % 2 == 0 ? "high" : "medium");
            w.setTitle("水质检测异常 - 浊度超标");
            w.setContent("检测项浊度不合格，测量值：" + (1.0 + i * 0.1));
            w.setStatus(i % 3 == 0 ? "processed" : "active");
            w.setCreateTime("2026-03-" + (i < 10 ? "0" + i : i));
            warnings.add(w);
        }
        
        updateTable(warnings);
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
        JOptionPane.showMessageDialog(this, "查询功能开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void doHandle() {
        int selectedRow = warningTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要处理的预警", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "处理功能开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}
