package com.ruralwater.util;

import javax.swing.*;
import java.awt.*;

/**
 * 统一的 UI 样式工具类
 */
public class UIStyles {
    
    // 主色调
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    public static final Color PRIMARY_DARK = new Color(52, 152, 219);
    public static final Color PRIMARY_LIGHT = new Color(135, 206, 250);
    
    // 辅助色
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    public static final Color WARNING_COLOR = new Color(241, 196, 15);
    public static final Color DANGER_COLOR = new Color(231, 76, 60);
    public static final Color INFO_COLOR = new Color(52, 152, 219);
    
    // 中性色
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    public static final Color BORDER_COLOR = new Color(220, 220, 220);
    public static final Color BACKGROUND_LIGHT = new Color(248, 249, 250);
    
    // 字体
    public static final Font FONT_TITLE = new Font("微软雅黑", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("微软雅黑", Font.BOLD, 18);
    public static final Font FONT_HEADING = new Font("微软雅黑", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("微软雅黑", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("微软雅黑", Font.PLAIN, 12);
    
    /**
     * 应用现代化主题到组件
     */
    public static void applyModernTheme() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            
            // 自定义 Nimbus 样式
            UIDefaults defaults = UIManager.getDefaults();
            defaults.put("nimbusBase", PRIMARY_COLOR);
            defaults.put("nimbusBlueGrey", BACKGROUND_LIGHT);
            defaults.put("control", Color.WHITE);
            
            // 设置按钮样式
            defaults.put("Button.background", PRIMARY_COLOR);
            defaults.put("Button.foreground", Color.WHITE);
            
            // 设置表格样式
            defaults.put("Table.alternateRowColor", BACKGROUND_LIGHT);
            defaults.put("Table.selectionBackground", PRIMARY_LIGHT);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建带图标的按钮
     */
    public static JButton createIconButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setFont(FONT_BODY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        return button;
    }
    
    /**
     * 创建主要按钮
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * 创建成功按钮
     */
    public static JButton createSuccessButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY);
        button.setBackground(SUCCESS_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * 创建危险按钮
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY);
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * 创建文本字段
     */
    public static JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 35));
        return textField;
    }
    
    /**
     * 创建密码字段
     */
    public static JPasswordField createPasswordField(int columns) {
        JPasswordField passwordField = new JPasswordField(columns);
        passwordField.setFont(FONT_BODY);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, 35));
        return passwordField;
    }
    
    /**
     * 创建下拉框
     */
    public static JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(FONT_BODY);
        comboBox.setPreferredSize(new Dimension(150, 35));
        return comboBox;
    }
    
    /**
     * 创建带标题的面板
     */
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                FONT_HEADING,
                TEXT_PRIMARY
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }
    
    /**
     * 创建卡片面板
     */
    public static JPanel createCardPanel(Color bgColor) {
        JPanel card = new JPanel();
        card.setBackground(bgColor != null ? bgColor : BACKGROUND_LIGHT);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        return card;
    }
}
