package com.ruralwater.util;

import javax.swing.*;
import java.awt.*;

/**
 * 通知提醒工具类
 */
public class NotificationUtil {
    
    /**
     * 显示成功通知
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, 
            message, 
            "成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 显示警告通知
     */
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, 
            message, 
            "警告", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * 显示错误通知
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, 
            message, 
            "错误", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * 显示确认对话框
     */
    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, 
            message, 
            "确认", 
            JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * 显示带有详细信息的错误对话框
     */
    public static void showErrorWithDetails(Component parent, String message, String details) {
        JTextArea textArea = new JTextArea(details);
        textArea.setRows(10);
        textArea.setColumns(40);
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(parent, 
            scrollPane, 
            "错误详情", 
            JOptionPane.ERROR_MESSAGE);
    }
}
