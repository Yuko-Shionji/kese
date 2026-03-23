package com.ruralwater.ui;

import com.ruralwater.entity.Region;
import com.ruralwater.entity.User;
import com.ruralwater.entity.WaterSource;
import com.ruralwater.service.RegionService;
import com.ruralwater.service.WaterSourceService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 添加水源对话框
 */
class AddWaterSourceDialog extends JDialog {
    private boolean added = false;
    private User currentUser;
    
    public AddWaterSourceDialog(Frame owner, User user) {
        super(owner, "新增水源", true);
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
        
        // 水源名称
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("水源名称："), gbc);
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(nameField, gbc);
        
        // 类型
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("类型："), gbc);
        JComboBox<String> typeCombo = new JComboBox<>();
        typeCombo.addItem("reservoir");
        typeCombo.addItem("well");
        typeCombo.addItem("river");
        typeCombo.addItem("lake");
        typeCombo.addItem("spring");
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(typeCombo, gbc);
        
        // 所属区域
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("所属区域："), gbc);
        JComboBox<String> regionCombo = new JComboBox<>();
        try {
            RegionService regionService = new RegionService();
            List<Region> regions = regionService.searchRegions(null);
            for (Region region : regions) {
                regionCombo.addItem(region.getRegionName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(regionCombo, gbc);
        
        // 位置详情
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("位置详情："), gbc);
        JTextField locationField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(locationField, gbc);
        
        // 经度
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("经度："), gbc);
        JTextField longitudeField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(longitudeField, gbc);
        
        // 纬度
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("纬度："), gbc);
        JTextField latitudeField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(latitudeField, gbc);
        
        // 容量
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("容量："), gbc);
        JTextField capacityField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(capacityField, gbc);
        
        // 状态
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("状态："), gbc);
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("normal");
        statusCombo.addItem("maintenance");
        statusCombo.addItem("scrapped");
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(statusCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                WaterSource source = new WaterSource();
                source.setSourceName(nameField.getText().trim());
                source.setSourceType((String) typeCombo.getSelectedItem());
                
                String selectedRegion = (String) regionCombo.getSelectedItem();
                if (selectedRegion != null) {
                    RegionService regionService = new RegionService();
                    List<Region> regions = regionService.searchRegions(selectedRegion);
                    if (!regions.isEmpty()) {
                        source.setRegionId(regions.get(0).getRegionId());
                    }
                }
                
                source.setLocationDetail(locationField.getText().trim());
                
                if (!longitudeField.getText().trim().isEmpty()) {
                    source.setLongitude(new BigDecimal(longitudeField.getText().trim()));
                }
                if (!latitudeField.getText().trim().isEmpty()) {
                    source.setLatitude(new BigDecimal(latitudeField.getText().trim()));
                }
                if (!capacityField.getText().trim().isEmpty()) {
                    source.setCapacity(new BigDecimal(capacityField.getText().trim()));
                }
                
                source.setStatus("normal".equals(statusCombo.getSelectedItem()) ? 1 : 0);
                
                WaterSourceService waterSourceService = new WaterSourceService();
                waterSourceService.addSource(source);
                
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
