package com.ruralwater.ui;

import com.ruralwater.entity.Region;
import com.ruralwater.entity.User;
import com.ruralwater.service.RegionService;

import javax.swing.*;
import java.awt.*;

/**
 * 编辑区域对话框
 */
class EditRegionDialog extends JDialog {
    private boolean edited = false;
    private Region region;
    
    public EditRegionDialog(Frame owner, Integer regionId, User currentUser) {
        super(owner, "编辑区域 #" + regionId, true);
        initUI(regionId);
    }
    
    private void initUI(Integer regionId) {
        setSize(500, 400);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        try {
            RegionService regionService = new RegionService();
            region = regionService.getRegionById(regionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (region == null) {
            JOptionPane.showMessageDialog(this, "未找到区域信息", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // 区域编码
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("区域编码："), gbc);
        JTextField codeField = new JTextField(20);
        codeField.setText(region.getRegionCode());
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(codeField, gbc);
        
        // 区域名称
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("区域名称："), gbc);
        JTextField nameField = new JTextField(20);
        nameField.setText(region.getRegionName());
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
        levelCombo.setSelectedItem(region.getLevel());
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(levelCombo, gbc);
        
        // 父区域 ID
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("父区域 ID："), gbc);
        JTextField parentIdField = new JTextField(20);
        parentIdField.setText(region.getParentId() != null ? region.getParentId().toString() : "0");
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(parentIdField, gbc);
        
        // 完整路径
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("完整路径："), gbc);
        JTextField fullPathField = new JTextField(20);
        fullPathField.setText(region.getFullPath() != null ? region.getFullPath() : "");
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(fullPathField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                String code = codeField.getText().trim();
                String name = nameField.getText().trim();
                String level = (String) levelCombo.getSelectedItem();
                int parentId = parentIdField.getText().trim().isEmpty() ? 0 : Integer.parseInt(parentIdField.getText().trim());
                String fullPath = fullPathField.getText().trim();
                
                region.setRegionCode(code);
                region.setRegionName(name);
                region.setLevel(level);
                region.setParentId(parentId);
                region.setFullPath(fullPath);
                
                RegionService regionService = new RegionService();
                regionService.updateRegion(region);
                
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
