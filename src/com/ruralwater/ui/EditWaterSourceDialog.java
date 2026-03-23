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
 * 编辑水源对话框
 */
class EditWaterSourceDialog extends JDialog {
    private boolean edited = false;
    
    public EditWaterSourceDialog(Frame owner, Integer sourceId, User currentUser) {
        super(owner, "编辑水源 #" + sourceId, true);
        initUI(sourceId);
    }
    
    private void initUI(Integer sourceId) {
        setSize(550, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        final WaterSource[] source = new WaterSource[1];
        try {
            WaterSourceService waterSourceService = new WaterSourceService();
            source[0] = waterSourceService.getSourceById(sourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (source[0] == null) {
            JOptionPane.showMessageDialog(this, "未找到水源信息", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // 水源名称
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("水源名称："), gbc);
        JTextField nameField = new JTextField(20);
        nameField.setText(source[0].getSourceName());
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
        typeCombo.setSelectedItem(source[0].getSourceType());
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(typeCombo, gbc);
        
        // 所属区域
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("所属区域："), gbc);
        JComboBox<String> regionCombo = new JComboBox<>();
        String currentRegionName = null;
        try {
            RegionService regionService = new RegionService();
            List<Region> regions = regionService.searchRegions(null);
            for (Region region : regions) {
                regionCombo.addItem(region.getRegionName());
                if (source[0].getRegionId() != null && source[0].getRegionId().equals(region.getRegionId())) {
                    currentRegionName = region.getRegionName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentRegionName != null) {
            regionCombo.setSelectedItem(currentRegionName);
        }
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(regionCombo, gbc);
        
        // 位置详情
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("位置详情："), gbc);
        JTextField locationField = new JTextField(20);
        locationField.setText(source[0].getLocationDetail() != null ? source[0].getLocationDetail() : "");
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(locationField, gbc);
        
        // 经度
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("经度："), gbc);
        JTextField longitudeField = new JTextField(20);
        longitudeField.setText(source[0].getLongitude() != null ? source[0].getLongitude().toString() : "");
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(longitudeField, gbc);
        
        // 纬度
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("纬度："), gbc);
        JTextField latitudeField = new JTextField(20);
        latitudeField.setText(source[0].getLatitude() != null ? source[0].getLatitude().toString() : "");
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(latitudeField, gbc);
        
        // 容量
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("容量："), gbc);
        JTextField capacityField = new JTextField(20);
        capacityField.setText(source[0].getCapacity() != null ? source[0].getCapacity().toString() : "");
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(capacityField, gbc);
        
        // 状态
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("状态："), gbc);
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("normal");
        statusCombo.addItem("maintenance");
        statusCombo.addItem("scrapped");
        statusCombo.setSelectedItem(source[0].getStatus() == 1 ? "normal" : "scrapped");
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(statusCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(e -> {
            try {
                source[0].setSourceName(nameField.getText().trim());
                source[0].setSourceType((String) typeCombo.getSelectedItem());
                
                String selectedRegion = (String) regionCombo.getSelectedItem();
                if (selectedRegion != null) {
                    RegionService regionService = new RegionService();
                    List<Region> regions = regionService.searchRegions(selectedRegion);
                    if (!regions.isEmpty()) {
                        source[0].setRegionId(regions.get(0).getRegionId());
                    }
                }
                
                source[0].setLocationDetail(locationField.getText().trim());
                
                if (!longitudeField.getText().trim().isEmpty()) {
                    source[0].setLongitude(new BigDecimal(longitudeField.getText().trim()));
                } else {
                    source[0].setLongitude(null);
                }
                if (!latitudeField.getText().trim().isEmpty()) {
                    source[0].setLatitude(new BigDecimal(latitudeField.getText().trim()));
                } else {
                    source[0].setLatitude(null);
                }
                if (!capacityField.getText().trim().isEmpty()) {
                    source[0].setCapacity(new BigDecimal(capacityField.getText().trim()));
                } else {
                    source[0].setCapacity(null);
                }
                
                source[0].setStatus("normal".equals(statusCombo.getSelectedItem()) ? 1 : 0);
                
                WaterSourceService waterSourceService = new WaterSourceService();
                waterSourceService.updateSource(source[0]);
                
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
