package com.ruralwater.ui;

import com.ruralwater.entity.Region;
import com.ruralwater.entity.User;
import com.ruralwater.service.RegionService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 添加区域对话框
 */
class AddRegionDialog extends JDialog {
    private boolean added = false;
    private User currentUser;
    
    public AddRegionDialog(Frame owner, User user) {
        super(owner, "新增区域", true);
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
        
        // 区域编码
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("区域编码："), gbc);
        JTextField codeField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(codeField, gbc);
        
        // 区域名称
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("区域名称："), gbc);
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(nameField, gbc);
        
        // 级别
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("级别："), gbc);
        JComboBox<String> levelCombo = new JComboBox<>();
        levelCombo.addItem("province");
        levelCombo.addItem("city");
        levelCombo.addItem("district");
        levelCombo.addItem("town");
        levelCombo.addItem("village");
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(levelCombo, gbc);
        
        // 父区域 ID
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("父区域 ID："), gbc);
        JTextField parentIdField = new JTextField(20);
        parentIdField.setText("0");
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(parentIdField, gbc);
        
        // 完整路径
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("完整路径："), gbc);
        JTextField fullPathField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(fullPathField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                Region region = new Region();
                region.setRegionCode(codeField.getText().trim());
                region.setRegionName(nameField.getText().trim());
                region.setLevel((String) levelCombo.getSelectedItem());
                
                if (!parentIdField.getText().trim().isEmpty()) {
                    region.setParentId(Integer.parseInt(parentIdField.getText().trim()));
                } else {
                    region.setParentId(0);
                }
                
                region.setFullPath(fullPathField.getText().trim());
                
                RegionService regionService = new RegionService();
                regionService.addRegion(region);
                
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
