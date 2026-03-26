package com.ruralwater;

import com.ruralwater.ui.LoginFrame;
import com.ruralwater.util.UIStyles;

import javax.swing.*;

/**
 * 系统主启动类
 */
public class MainApp {
    
    public static void main(String[] args) {
        // 应用现代化主题
        UIStyles.applyModernTheme();
        
        // 使用 EventQueue 确保线程安全
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "系统启动失败：" + e.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
