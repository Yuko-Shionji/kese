package com.ruralwater.ui;

import com.ruralwater.service.WaterQualityService;
import com.ruralwater.util.ExportUtil;
import com.ruralwater.util.UIStyles;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计分析面板 - 显示水质检测的统计图表
 */
public class StatisticsPanel extends JPanel {
    
    private BarChartPanel monthBarChart;
    private PieChartPanel conclusionPieChart;
    private JButton refreshButton;
    private JButton exportButton;
    
    public StatisticsPanel() {
        initUI();
        loadStatistics();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 顶部工具栏
        JPanel toolbarPanel = createToolbarPanel();
        add(toolbarPanel, BorderLayout.NORTH);
        
        // 中间图表区域
        JPanel chartsPanel = createChartsPanel();
        add(chartsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("操作"));
        
        refreshButton = UIStyles.createPrimaryButton("刷新统计");
        refreshButton.addActionListener(e -> loadStatistics());
        panel.add(refreshButton);
        
        exportButton = UIStyles.createSuccessButton("导出数据");
        exportButton.addActionListener(e -> exportData());
        panel.add(exportButton);
        
        return panel;
    }
    
    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        // 月份统计柱状图
        monthBarChart = new BarChartPanel("月度检测趋势", UIStyles.PRIMARY_COLOR);
        panel.add(monthBarChart);
        
        // 结论分布饼图
        conclusionPieChart = new PieChartPanel("检测结果分布");
        panel.add(conclusionPieChart);
        
        // 添加更多信息面板
        panel.add(createInfoPanel());
        panel.add(createTrendPanel());
        
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("统计信息"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("总检测次数:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        JLabel totalCountLabel = new JLabel("-");
        totalCountLabel.setFont(UIStyles.FONT_HEADING);
        panel.add(totalCountLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("合格次数:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        JLabel qualifiedCountLabel = new JLabel("-");
        qualifiedCountLabel.setForeground(UIStyles.SUCCESS_COLOR);
        qualifiedCountLabel.setFont(UIStyles.FONT_HEADING);
        panel.add(qualifiedCountLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("不合格次数:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        JLabel unqualifiedCountLabel = new JLabel("-");
        unqualifiedCountLabel.setForeground(UIStyles.DANGER_COLOR);
        unqualifiedCountLabel.setFont(UIStyles.FONT_HEADING);
        panel.add(unqualifiedCountLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("合格率:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        JLabel passRateLabel = new JLabel("-");
        passRateLabel.setFont(UIStyles.FONT_HEADING);
        panel.add(passRateLabel, gbc);
        
        return panel;
    }
    
    private JPanel createTrendPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("最近趋势"));
        
        JTextArea trendText = new JTextArea();
        trendText.setEditable(false);
        trendText.setLineWrap(true);
        trendText.setFont(UIStyles.FONT_SMALL);
        trendText.setText("• 本月检测次数较上月有所增加\n" +
                         "• 水质整体保持稳定\n" +
                         "• 需要关注个别水厂的指标波动\n" +
                         "• 建议加强定期检测频率");
        trendText.setBackground(UIStyles.BACKGROUND_LIGHT);
        trendText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(trendText, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadStatistics() {
        try {
            // WaterQualityService qualityService = new WaterQualityService();
            
            // 模拟数据（实际应该从数据库获取）
            List<String> months = new ArrayList<>();
            List<Double> monthValues = new ArrayList<>();
            
            for (int i = 1; i <= 6; i++) {
                months.add(i + "月");
                monthValues.add((double) (Math.random() * 50 + 20));
            }
            
            monthBarChart.setData(months, monthValues);
            
            // 合格/不合格统计
            List<String> conclusions = new ArrayList<>();
            List<Double> conclusionValues = new ArrayList<>();
            
            conclusions.add("合格");
            conclusions.add("不合格");
            conclusionValues.add((double) (Math.random() * 80 + 150));
            conclusionValues.add((double) (Math.random() * 20 + 10));
            
            conclusionPieChart.setData(conclusions, conclusionValues);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "加载统计数据失败：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportData() {
        try {
            // 导出统计数据到 Excel
            String[] columnNames = {"统计项目", "数值", "说明"};
            Object[][] data = new Object[][]{
                {"总检测次数", "150", "本年度累计检测次数"},
                {"合格次数", "142", "检测结果合格的次数"},
                {"不合格次数", "8", "检测结果不合格的次数"},
                {"合格率", "94.67%", "合格次数占总次数的比例"},
                {"", "", ""},
                {"月度趋势 -1 月", "45", "1 月份检测次数"},
                {"月度趋势 -2 月", "38", "2 月份检测次数"},
                {"月度趋势 -3 月", "52", "3 月份检测次数"},
                {"月度趋势 -4 月", "41", "4 月份检测次数"},
                {"月度趋势 -5 月", "49", "5 月份检测次数"},
                {"月度趋势 -6 月", "43", "6 月份检测次数"}
            };
            
            boolean success = ExportUtil.exportToExcel(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                columnNames,
                data,
                "水质检测统计分析"
            );
            
            if (!success) {
                JOptionPane.showMessageDialog(this, 
                    "用户取消导出操作", 
                    "提示", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "导出失败：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
