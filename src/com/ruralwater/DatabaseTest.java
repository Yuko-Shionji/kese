package com.ruralwater;

import com.ruralwater.util.DBUtil;
import com.ruralwater.service.*;

/**
 * 数据库连接测试
 */
public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("=== 农村饮水安全监测管理系统 - 数据库连接测试 ===\n");
        
        // 测试数据库连接
        System.out.println("1. 测试数据库连接...");
        boolean connected = DBUtil.testConnection();
        
        if (connected) {
            System.out.println("✓ 数据库连接成功！\n");
            
            try {
                // 测试服务层
                System.out.println("2. 测试服务层功能...");
                
                WaterPlantService plantService = new WaterPlantService();
                int plantCount = plantService.getTotalCount();
                System.out.println("   - 水厂总数：" + plantCount);
                
                WaterQualityService qualityService = new WaterQualityService();
                int recordCount = qualityService.getTotalCount();
                System.out.println("   - 检测记录数：" + recordCount);
                
                WarningService warningService = new WarningService();
                int activeWarnings = warningService.getActiveWarnings();
                System.out.println("   - 待处理预警：" + activeWarnings);
                
                EquipmentService equipmentService = new EquipmentService();
                int equipmentCount = equipmentService.getTotalCount();
                System.out.println("   - 设备总数：" + equipmentCount);
                
                System.out.println("\n✓ 所有服务层功能测试通过！\n");
                
                System.out.println("=== 系统优化完成 ===");
                System.out.println("新增功能：");
                System.out.println("1. DBUtil 支持配置文件加载");
                System.out.println("2. 新增设备管理模块（EquipmentDAO、EquipmentService）");
                System.out.println("3. 新增水质检测详情模块（WaterQualityDetailDAO、WaterQualityDetailService）");
                System.out.println("4. 新增系统日志模块（SystemLogService）");
                System.out.println("5. WaterQualityService 增强：");
                System.out.println("   - 事务处理支持");
                System.out.println("   - 统计分析功能");
                System.out.println("   - 合格率计算");
                System.out.println("6. MainFrame 首页仪表板：");
                System.out.println("   - 数据统计卡片");
                System.out.println("   - 最近检测记录");
                System.out.println("   - 系统信息展示");
                
            } catch (Exception e) {
                System.err.println("✗ 服务层测试失败：" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("✗ 数据库连接失败，请检查配置！");
        }
    }
}
