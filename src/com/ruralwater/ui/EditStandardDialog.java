package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterQualityStandard;
import com.ruralwater.service.WaterQualityStandardService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * 编辑检测标准对话框
 */
class EditStandardDialog extends JDialog {
    private boolean edited = false;
    private WaterQualityStandard standard;
    
    public EditStandardDialog(Frame owner, Integer standardId, User currentUser) {
        super(owner, "编辑检测标准 #" + standardId, true);
        initUI(standardId);
    }
    
    private void initUI(Integer standardId) {
        setSize(550, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        try {
            WaterQualityStandardService standardService = new WaterQualityStandardService();
            standard = standardService.getStandardById(standardId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (standard == null) {
            JOptionPane.showMessageDialog(this, "未找到检测标准信息", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // 检测项名称
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("检测项名称："), gbc);
        JTextField nameField = new JTextField(20);
        nameField.setText(standard.getItemName());
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(nameField, gbc);
        
        // 编码
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("编码："), gbc);
        JTextField codeField = new JTextField(20);
        codeField.setText(standard.getItemCode());
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(codeField, gbc);
        
        // 单位
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("单位："), gbc);
        JTextField unitField = new JTextField(20);
        unitField.setText(standard.getUnit() != null ? standard.getUnit() : "");
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(unitField, gbc);
        
        // 标准值
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("标准值："), gbc);
        JTextField standardValueField = new JTextField(20);
        standardValueField.setText(standard.getStandardValue() != null ? standard.getStandardValue().toString() : "");
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(standardValueField, gbc);
        
        // 最小值
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("最小值："), gbc);
        JTextField minValueField = new JTextField(20);
        minValueField.setText(standard.getMinValue() != null ? standard.getMinValue().toString() : "");
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(minValueField, gbc);
        
        // 最大值
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("最大值："), gbc);
        JTextField maxValueField = new JTextField(20);
        maxValueField.setText(standard.getMaxValue() != null ? standard.getMaxValue().toString() : "");
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(maxValueField, gbc);
        
        // 类别
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("类别："), gbc);
        JComboBox<String> categoryCombo = new JComboBox<>();
        categoryCombo.addItem("physical");
        categoryCombo.addItem("chemical");
        categoryCombo.addItem("biological");
        categoryCombo.addItem("radiological");
        categoryCombo.setSelectedItem(standard.getCategory());
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(categoryCombo, gbc);
        
        // 说明
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("说明："), gbc);
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setText(standard.getDescription() != null ? standard.getDescription() : "");
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(scrollPane, gbc);
        
        // 状态
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("状态："), gbc);
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("启用");
        statusCombo.addItem("停用");
        statusCombo.setSelectedItem(standard.getIsActive() == 1 ? "启用" : "停用");
        gbc.gridx = 1; gbc.gridy = 8;
        formPanel.add(statusCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                String itemName = nameField.getText().trim();
                String itemCode = codeField.getText().trim();
                String unit = unitField.getText().trim();
                BigDecimal standardValue = standardValueField.getText().trim().isEmpty() ? null : new BigDecimal(standardValueField.getText().trim());
                BigDecimal minValue = minValueField.getText().trim().isEmpty() ? null : new BigDecimal(minValueField.getText().trim());
                BigDecimal maxValue = maxValueField.getText().trim().isEmpty() ? null : new BigDecimal(maxValueField.getText().trim());
                String category = (String) categoryCombo.getSelectedItem();
                String description = descriptionArea.getText().trim();
                int isActive = "启用".equals(statusCombo.getSelectedItem()) ? 1 : 0;
                
                standard.setItemName(itemName);
                standard.setItemCode(itemCode);
                standard.setUnit(unit);
                standard.setStandardValue(standardValue);
                standard.setMinValue(minValue);
                standard.setMaxValue(maxValue);
                standard.setCategory(category);
                standard.setDescription(description);
                standard.setIsActive(isActive);
                
                WaterQualityStandardService standardService = new WaterQualityStandardService();
                standardService.updateStandard(standard);
                
                edited = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "编辑失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
