package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterQualityRecord;
import com.ruralwater.service.WaterPlantService;
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
        loadPlantList(); // 加载水厂列表
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
     * 加载水厂列表
     */
    private void loadPlantList() {
        try {
            WaterPlantService plantService = new WaterPlantService();
            List<com.ruralwater.entity.WaterPlant> plants = plantService.getPlantsByPage(1, 100);
            for (com.ruralwater.entity.WaterPlant plant : plants) {
                plantCombo.addItem(plant.getPlantName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            String selectedPlant = (String) plantCombo.getSelectedItem();
            if (selectedPlant != null && !"全部".equals(selectedPlant)) {
                // 根据水厂名称获取 ID
                WaterPlantService plantService = new WaterPlantService();
                List<com.ruralwater.entity.WaterPlant> plants = plantService.searchPlants(selectedPlant);
                for (com.ruralwater.entity.WaterPlant plant : plants) {
                    if (plant.getPlantName().equals(selectedPlant)) {
                        plantId = plant.getPlantId();
                        break;
                    }
                }
            }
            
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
            
            int count = records.size();
            MainFrame mainFrame = MainFrame.getMainFrame(this);
            if (mainFrame != null) {
                mainFrame.updateStatus("查询完成，共找到 " + count + " 条记录");
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
        
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            AddRecordDialog dialog = new AddRecordDialog(owner, currentUser);
            dialog.setVisible(true);
            
            if (dialog.isAdded()) {
                JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("添加了新的水质检测记录");
                }
            }
        }
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
        
        Integer recordId = (Integer) tableModel.getValueAt(selectedRow, 0);
        try {
            WaterQualityRecord record = qualityService.getRecordById(recordId);
            Frame owner = MainFrame.getMainFrame(this);
            if (owner != null) {
                RecordDetailDialog dialog = new RecordDetailDialog(owner, record);
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查看详情失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
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
        
        Integer recordId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        
        if ("已审核".equals(status)) {
            JOptionPane.showMessageDialog(this, "该记录已审核，无需重复审核", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Frame owner = MainFrame.getMainFrame(this);
        if (owner != null) {
            ApproveDialog dialog = new ApproveDialog(owner, recordId, currentUser);
            dialog.setVisible(true);
            
            if (dialog.isApproved()) {
                JOptionPane.showMessageDialog(this, "审核成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                MainFrame mainFrame = MainFrame.getMainFrame(this);
                if (mainFrame != null) {
                    mainFrame.updateStatus("审核了水质检测记录 #" + recordId);
                }
            }
        }
    }
}

/**
 * 新增检测记录对话框
 */
class AddRecordDialog extends JDialog {
    private User currentUser;
    private boolean added = false;
    private JComboBox<String> plantCombo;
    private JTextField samplePointField;
    private JTextField sampleTimeField;
    private JComboBox<String> conclusionCombo;
    private JTextArea remarkArea;
    
    public AddRecordDialog(Frame owner, User user) {
        super(owner, "新增水质检测记录", true);
        this.currentUser = user;
        initUI();
    }
    
    private void initUI() {
        setSize(500, 400);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("水厂："), gbc);
        
        plantCombo = new JComboBox<>();
        try {
            WaterPlantService plantService = new WaterPlantService();
            List<com.ruralwater.entity.WaterPlant> plants = plantService.getPlantsByPage(1, 100);
            for (com.ruralwater.entity.WaterPlant plant : plants) {
                plantCombo.addItem(plant.getPlantName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(plantCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("采样点："), gbc);
        
        samplePointField = new JTextField(20);
        samplePointField.setText("出厂水");
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(samplePointField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("采样时间："), gbc);
        
        sampleTimeField = new JTextField(20);
        sampleTimeField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(sampleTimeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("检测结论："), gbc);
        
        conclusionCombo = new JComboBox<>();
        conclusionCombo.addItem("qualified");
        conclusionCombo.addItem("unqualified");
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(conclusionCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("备注："), gbc);
        
        remarkArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(remarkArea);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(scrollPane, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> doConfirm());
        buttonPanel.add(confirmBtn);
        
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void doConfirm() {
        try {
            String selectedPlant = (String) plantCombo.getSelectedItem();
            WaterPlantService plantService = new WaterPlantService();
            List<com.ruralwater.entity.WaterPlant> plants = plantService.searchPlants(selectedPlant);
            Integer plantId = null;
            for (com.ruralwater.entity.WaterPlant plant : plants) {
                if (plant.getPlantName().equals(selectedPlant)) {
                    plantId = plant.getPlantId();
                    break;
                }
            }
            
            WaterQualityRecord record = new WaterQualityRecord();
            record.setPlantId(plantId);
            record.setSamplePoint(samplePointField.getText().trim());
            record.setSampleTime(sampleTimeField.getText().trim());
            record.setTesterId(currentUser.getUserId());
            record.setConclusion((String) conclusionCombo.getSelectedItem());
            record.setRemark(remarkArea.getText().trim());
            
            WaterQualityService qualityService = new WaterQualityService();
            qualityService.addRecord(record, null); // TODO: 添加检测详情
            
            added = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isAdded() {
        return added;
    }
}

/**
 * 检测记录详情对话框
 */
class RecordDetailDialog extends JDialog {
    public RecordDetailDialog(Frame owner, WaterQualityRecord record) {
        super(owner, "检测记录详情 - #" + record.getRecordId(), true);
        initUI(record);
    }
    
    private void initUI(WaterQualityRecord record) {
        setSize(600, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        // ID
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(createLabel("记录 ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        infoPanel.add(createValue(String.valueOf(record.getRecordId())), gbc);
        
        // 水厂
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(createLabel("水厂："), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        infoPanel.add(createValue(record.getPlantName() != null ? record.getPlantName() : "-"), gbc);
        
        // 采样点
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(createLabel("采样点："), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        infoPanel.add(createValue(record.getSamplePoint() != null ? record.getSamplePoint() : "-"), gbc);
        
        // 采样时间
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(createLabel("采样时间："), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        infoPanel.add(createValue(record.getSampleTime() != null ? record.getSampleTime() : "-"), gbc);
        
        // 检测员
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(createLabel("检测员："), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        infoPanel.add(createValue(record.getTesterName() != null ? record.getTesterName() : "-"), gbc);
        
        // 状态
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(createLabel("审核状态："), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        infoPanel.add(createValue(getReviewStatusText(record.getReviewStatus())), gbc);
        
        // 结论
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(createLabel("检测结论："), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        infoPanel.add(createValue(getConclusionText(record.getConclusion())), gbc);
        
        // 备注
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(createLabel("备注："), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        JTextArea remarkArea = new JTextArea(record.getRemark() != null ? record.getRemark() : "-", 4, 30);
        remarkArea.setLineWrap(true);
        remarkArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(remarkArea);
        infoPanel.add(scrollPane, gbc);
        
        // 创建时间
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(createLabel("创建时间："), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        infoPanel.add(createValue(record.getCreateTime() != null ? record.getCreateTime() : "-"), gbc);
        
        add(infoPanel, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("关闭");
        closeBtn.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.BOLD, 13));
        return label;
    }
    
    private JLabel createValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        return label;
    }
    
    private String getReviewStatusText(String status) {
        switch (status) {
            case "pending": return "待审核";
            case "approved": return "已审核";
            case "rejected": return "已驳回";
            default: return status;
        }
    }
    
    private String getConclusionText(String conclusion) {
        switch (conclusion) {
            case "qualified": return "合格";
            case "unqualified": return "不合格";
            default: return conclusion;
        }
    }
}

/**
 * 审核对话框
 */
class ApproveDialog extends JDialog {
    private boolean approved = false;
    
    public ApproveDialog(Frame owner, Integer recordId, User currentUser) {
        super(owner, "审核检测记录 #" + recordId, true);
        initUI(recordId, currentUser);
    }
    
    private void initUI(Integer recordId, User currentUser) {
        setSize(450, 250);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("审核结论："), gbc);
        
        JComboBox<String> conclusionCombo = new JComboBox<>();
        conclusionCombo.addItem("qualified");
        conclusionCombo.addItem("unqualified");
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(conclusionCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("审核意见："), gbc);
        
        JTextArea opinionArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(opinionArea);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(scrollPane, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                String conclusion = (String) conclusionCombo.getSelectedItem();
                String opinion = opinionArea.getText().trim();
                
                WaterQualityService qualityService = new WaterQualityService();
                qualityService.approveRecord(recordId, currentUser.getUserId(), conclusion);
                
                approved = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "审核失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(confirmBtn);
        
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public boolean isApproved() {
        return approved;
    }
}
