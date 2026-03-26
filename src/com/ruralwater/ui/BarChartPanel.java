package com.ruralwater.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单的柱状图面板
 */
public class BarChartPanel extends JPanel {
    
    private List<String> labels;
    private List<Double> values;
    private String title;
    private Color barColor;
    
    public BarChartPanel(String title, Color barColor) {
        this.title = title;
        this.barColor = barColor;
        this.labels = new ArrayList<>();
        this.values = new ArrayList<>();
        setPreferredSize(new Dimension(400, 300));
    }
    
    public void setData(List<String> labels, List<Double> values) {
        this.labels = labels;
        this.values = values;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 绘制标题
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
        g2d.setColor(Color.BLACK);
        g2d.drawString(title, 20, 25);
        
        if (labels.isEmpty() || values.isEmpty()) {
            g2d.dispose();
            return;
        }
        
        int padding = 50;
        int chartWidth = getWidth() - 2 * padding;
        int chartHeight = getHeight() - 2 * padding - 30;
        
        // 找到最大值
        double maxValue = values.stream().max(Double::compare).orElse(1.0);
        
        int barWidth = chartWidth / labels.size() - 10;
        
        // 绘制坐标轴
        g2d.drawLine(padding, padding, padding, getHeight() - padding);
        g2d.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);
        
        // 绘制柱状图
        for (int i = 0; i < labels.size(); i++) {
            int x = padding + 5 + i * (chartWidth / labels.size());
            double value = values.get(i);
            int barHeight = (int) ((value / maxValue) * chartHeight);
            int y = getHeight() - padding - barHeight;
            
            // 绘制柱子
            g2d.setColor(barColor);
            g2d.fillRect(x, y, barWidth, barHeight);
            
            // 绘制边框
            g2d.setColor(barColor.darker());
            g2d.drawRect(x, y, barWidth, barHeight);
            
            // 绘制数值
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 10));
            String valueStr = String.valueOf((int)Math.round(value));
            int valueWidth = g2d.getFontMetrics().stringWidth(valueStr);
            g2d.drawString(valueStr, x + (barWidth - valueWidth) / 2, y - 5);
            
            // 绘制标签
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 11));
            String label = labels.get(i);
            int labelWidth = g2d.getFontMetrics().stringWidth(label);
            g2d.drawString(label, x + (barWidth - labelWidth) / 2, getHeight() - padding + 20);
        }
        
        g2d.dispose();
    }
}
