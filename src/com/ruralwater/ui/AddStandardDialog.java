package com.ruralwater.ui;

import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterQualityStandard;
import com.ruralwater.service.WaterQualityStandardService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * 添加检测标准对话框
 */
class AddStandardDialog extends JDialog {
    private boolean added = false;
    private User currentUser;
    
    public AddStandardDialog(Frame owner, User user) {
        super(owner, "新增检测标准", true);
        this.currentUser = user;
        initUI();
    }
    
    private void initUI() {
        setSize(550, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 检测项名称
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("检测项名称："), gbc);
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(nameField, gbc);
        
        // 编码
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("编码："), gbc);
        JTextField codeField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(codeField, gbc);
        
        // 单位
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("单位："), gbc);
        JTextField unitField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(unitField, gbc);
        
        // 标准值
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("标准值："), gbc);
        JTextField standardValueField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(standardValueField, gbc);
        
        // 最小值
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("最小值："), gbc);
        JTextField minValueField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(minValueField, gbc);
        
        // 最大值
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("最大值："), gbc);
        JTextField maxValueField = new JTextField(20);
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
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(categoryCombo, gbc);
        
        // 说明
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("说明："), gbc);
        JTextArea descriptionArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(scrollPane, gbc);
        
        // 状态
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("状态："), gbc);
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("启用");
        statusCombo.addItem("停用");
        gbc.gridx = 1; gbc.gridy = 8;
        formPanel.add(statusCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                WaterQualityStandard standard = new WaterQualityStandard();
                standard.setItemName(nameField.getText().trim());
                standard.setItemCode(codeField.getText().trim());
                standard.setUnit(unitField.getText().trim());
                
                if (!standardValueField.getText().trim().isEmpty()) {
                    standard.setStandardValue(new BigDecimal(standardValueField.getText().trim()));
                }
                if (!minValueField.getText().trim().isEmpty()) {
                    standard.setMinValue(new BigDecimal(minValueField.getText().trim()));
                }
                if (!maxValueField.getText().trim().isEmpty()) {
                    standard.setMaxValue(new BigDecimal(maxValueField.getText().trim()));
                }
                
                standard.setCategory((String) categoryCombo.getSelectedItem());
                standard.setDescription(descriptionArea.getText().trim());
                standard.setIsActive("启用".equals(statusCombo.getSelectedItem()) ? 1 : 0);
                
                WaterQualityStandardService standardService = new WaterQualityStandardService();
                standardService.addStandard(standard);
                
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
