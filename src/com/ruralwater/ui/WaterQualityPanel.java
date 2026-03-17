package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterQualityRecord;
import com.ruralwater.service.WaterQualityService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 水质检测管理面板
 */
public class WaterQualityPanel extends JPanel {
    
    private User currentUser;
    private JTable qualityTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> plantCombo;
    private JTextField startDateField;
    private JTextField endDateField;
    private JComboBox<String> conclusionCombo;
    private JButton searchButton;
    private JButton addButton;
    private JButton viewButton;
    private JButton approveButton;
    private JButton refreshButton;
    
    private WaterQualityService qualityService = new WaterQualityService();
    
    public WaterQualityPanel(User user) {
        this.currentUser = user;
        initUI();
        loadData();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 顶部查询面板
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
        
        panel.add(new JLabel("水厂："));
        plantCombo = new JComboBox<>();
        plantCombo.setPreferredSize(new Dimension(150, 30));
        plantCombo.addItem("全部");
        // TODO: 加载水厂列表
        panel.add(plantCombo);
        
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("开始日期："));
        startDateField = new JTextField(12);
        startDateField.setText("2026-01-01");
        panel.add(startDateField);
        
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("结束日期："));
        endDateField = new JTextField(12);
        endDateField.setText("2026-12-31");
        panel.add(endDateField);
        
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("结论："));
        conclusionCombo = new JComboBox<>();
        conclusionCombo.setPreferredSize(new Dimension(100, 30));
        conclusionCombo.addItem("全部");
        conclusionCombo.addItem("qualified");
        conclusionCombo.addItem("unqualified");
        panel.add(conclusionCombo);
        
        panel.add(Box.createHorizontalStrut(10));
        searchButton = new JButton("查询");
        searchButton.addActionListener(e -> doSearch());
        panel.add(searchButton);
        
        panel.add(Box.createHorizontalStrut(20));
        
        addButton = new JButton("新增记录");
        addButton.addActionListener(e -> doAdd());
        panel.add(addButton);
        
        viewButton = new JButton("查看详情");
        viewButton.addActionListener(e -> doView());
        panel.add(viewButton);
        
        if ("admin".equals(currentUser.getRole()) || "operator".equals(currentUser.getRole())) {
            approveButton = new JButton("审核");
            approveButton.addActionListener(e -> doApprove());
            panel.add(approveButton);
        }
        
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
        String[] columnNames = {"ID", "水厂", "采样点", "采样时间", "检测员", "状态", "结论", "备注", "创建时间"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        qualityTable = new JTable(tableModel);
        qualityTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        qualityTable.setRowHeight(30);
        qualityTable.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(qualityTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("检测记录列表"));
        
        return scrollPane;
    }
    
    /**
     * 加载数据
     */
    private void loadData() {
        try {
            List<WaterQualityRecord> records = qualityService.getRecordsByCondition(
                null, null, null, null, 1, 100);
            updateTable(records);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 更新表格数据
     */
    private void updateTable(List<WaterQualityRecord> records) {
        tableModel.setRowCount(0);
        
        for (WaterQualityRecord record : records) {
            Object[] row = {
                record.getRecordId(),
                record.getPlantName() != null ? record.getPlantName() : "-",
                record.getSamplePoint() != null ? record.getSamplePoint() : "-",
                record.getSampleTime() != null ? record.getSampleTime() : "-",
                record.getTesterName() != null ? record.getTesterName() : "-",
                getReviewStatusText(record.getReviewStatus()),
                getConclusionText(record.getConclusion()),
                record.getRemark() != null ? record.getRemark() : "-",
                record.getCreateTime() != null ? record.getCreateTime() : "-"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * 获取审核状态文本
     */
    private String getReviewStatusText(String status) {
        switch (status) {
            case "pending": return "待审核";
            case "approved": return "已审核";
            case "rejected": return "已驳回";
            default: return status;
        }
    }
    
    /**
     * 获取结论文本
     */
    private String getConclusionText(String conclusion) {
        switch (conclusion) {
            case "qualified": return "合格";
            case "unqualified": return "不合格";
            default: return conclusion;
        }
    }
    
    /**
     * 查询
     */
    private void doSearch() {
        try {
            Integer plantId = null;
            String startDate = startDateField.getText().trim();
            String endDate = endDateField.getText().trim();
            String conclusion = null;
            
            String selectedConclusion = (String) conclusionCombo.getSelectedItem();
            if (!"全部".equals(selectedConclusion)) {
                conclusion = selectedConclusion;
            }
            
            List<WaterQualityRecord> records = qualityService.getRecordsByCondition(
                plantId, startDate, endDate, conclusion, 1, 100);
            updateTable(records);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 新增
     */
    private void doAdd() {
        JOptionPane.showMessageDialog(this, "新增检测记录功能开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 查看详情
     */
    private void doView() {
        int selectedRow = qualityTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要查看的记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "查看详情功能开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 审核
     */
    private void doApprove() {
        int selectedRow = qualityTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要审核的记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "审核功能开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}
