package com.ruralwater.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单的饼图面板
 */
public class PieChartPanel extends JPanel {
    
    private List<String> labels;
    private List<Double> values;
    private String title;
    private List<Color> colors;
    
    public PieChartPanel(String title) {
        this.title = title;
        this.labels = new ArrayList<>();
        this.values = new ArrayList<>();
        this.colors = new ArrayList<>();
        setPreferredSize(new Dimension(300, 300));
        
        // 默认颜色
        colors.add(new Color(52, 152, 219));
        colors.add(new Color(46, 204, 113));
        colors.add(new Color(241, 196, 15));
        colors.add(new Color(231, 76, 60));
        colors.add(new Color(155, 89, 182));
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
        
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2 + 10;
        int radius = Math.min(getWidth(), getHeight()) / 2 - 40;
        
        double total = values.stream().mapToDouble(Double::doubleValue).sum();
        double startAngle = 0;
        
        // 绘制饼图
        for (int i = 0; i < values.size(); i++) {
            double value = values.get(i);
            double angle = (value / total) * 360;
            
            Color color = colors.get(i % colors.size());
            g2d.setColor(color);
            
            g2d.fillArc(centerX - radius, centerY - radius, 
                       2 * radius, 2 * radius, 
                       (int) startAngle, (int) -angle);
            
            startAngle += angle;
        }
        
        // 绘制图例
        int legendX = 10;
        int legendY = getHeight() - 20 - labels.size() * 20;
        
        g2d.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        for (int i = 0; i < labels.size(); i++) {
            g2d.setColor(colors.get(i % colors.size()));
            g2d.fillRect(legendX, legendY + i * 20, 15, 15);
            
            g2d.setColor(Color.BLACK);
            g2d.drawString(labels.get(i) + ": " + String.format("%.1f%%", values.get(i) / total * 100), 
                          legendX + 20, legendY + i * 20 + 13);
        }
        
        g2d.dispose();
    }
}
