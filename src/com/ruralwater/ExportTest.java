package com.ruralwater;

import com.ruralwater.util.ExportUtil;

import javax.swing.*;

/**
 * 测试导出功能的简单示例
 */
public class ExportTest {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 创建测试窗口
            JFrame frame = new JFrame("导出功能测试");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel();
            JButton exportButton = new JButton("测试导出 Excel");
            
            exportButton.addActionListener(e -> {
                String[] columnNames = {"姓名", "年龄", "城市", "职业"};
                Object[][] data = {
                    {"张三", "25", "北京", "工程师"},
                    {"李四", "30", "上海", "设计师"},
                    {"王五", "28", "广州", "产品经理"},
                    {"赵六", "35", "深圳", "数据分析师"}
                };
                
                boolean success = ExportUtil.exportToExcel(frame, columnNames, data, "测试数据");
                
                if (success) {
                    System.out.println("导出成功！");
                } else {
                    System.out.println("导出取消或失败！");
                }
            });
            
            panel.add(exportButton);
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
