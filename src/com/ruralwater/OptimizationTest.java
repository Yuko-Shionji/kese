package com.ruralwater;

import com.ruralwater.util.SimpleLogger;
import com.ruralwater.util.DBUtil;

/**
 * 优化工具类测试
 */
public class OptimizationTest {
    
    public static void main(String[] args) {
        System.out.println("=== 代码优化测试 ===\n");
        
        // 测试日志工具
        testLogger();
        
        // 测试数据库连接
        testDBConnection();
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    /**
     * 测试日志工具
     */
    private static void testLogger() {
        System.out.println("1. 测试日志工具...");
        
        SimpleLogger.debug("这是一条调试信息");
        SimpleLogger.info("这是一条信息");
        SimpleLogger.warn("这是一条警告");
        SimpleLogger.error("这是一条错误信息");
        
        System.out.println("   ✓ 日志工具测试通过\n");
    }
    
    /**
     * 测试数据库连接
     */
    private static void testDBConnection() {
        System.out.println("2. 测试数据库连接...");
        
        try {
            boolean connected = DBUtil.testConnection();
            if (connected) {
                System.out.println("   ✓ 数据库连接测试通过\n");
            } else {
                System.out.println("   ✗ 数据库连接失败\n");
            }
        } catch (Exception e) {
            System.out.println("   ✗ 数据库连接异常：" + e.getMessage() + "\n");
            SimpleLogger.error("数据库连接测试失败", e);
        }
    }
}
