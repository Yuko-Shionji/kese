package com.ruralwater;

import com.ruralwater.service.WaterQualityStandardService;
import com.ruralwater.entity.WaterQualityStandard;

import java.util.List;

/**
 * 测试水质检测标准数据
 */
public class TestStandardsData {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("测试水质检测标准数据");
        System.out.println("=====================================");
        
        try {
            WaterQualityStandardService service = new WaterQualityStandardService();
            
            // 查询所有标准
            List<WaterQualityStandard> standards = service.findAll();
            
            if (standards == null || standards.isEmpty()) {
                System.out.println("\n警告：数据库中没有检测到任何水质检测标准！");
                System.out.println("请执行以下操作：");
                System.out.println("1. 检查数据库连接配置");
                System.out.println("2. 执行 SQL 脚本：database\\init_water_quality_standards.sql");
                System.out.println("3. 重新运行此测试");
            } else {
                System.out.println("\n成功加载 " + standards.size() + " 条检测标准");
                System.out.println("\n按类别统计:");
                System.out.println("-------------------------------------");
                
                int physical = 0, chemical = 0, biological = 0, radiological = 0;
                
                for (WaterQualityStandard standard : standards) {
                    String category = standard.getCategory();
                    if ("physical".equals(category)) physical++;
                    else if ("chemical".equals(category)) chemical++;
                    else if ("biological".equals(category)) biological++;
                    else if ("radiological".equals(category)) radiological++;
                    
                    System.out.printf("[%s] %s (%s) - 单位：%s%n", 
                        standard.getCategory(),
                        standard.getItemName(), 
                        standard.getItemCode(),
                        standard.getUnit() != null ? standard.getUnit() : "-");
                }
                
                System.out.println("-------------------------------------");
                System.out.println("\n类别汇总:");
                System.out.println("  物理指标 (physical):      " + physical + " 项");
                System.out.println("  化学指标 (chemical):      " + chemical + " 项");
                System.out.println("  生物指标 (biological):    " + biological + " 项");
                System.out.println("  放射性指标 (radiological): " + radiological + " 项");
                System.out.println("  总计：" + standards.size() + " 项");
                
                System.out.println("\n=====================================");
                System.out.println("✓ 检测标准数据正常！");
                System.out.println("=====================================");
            }
            
        } catch (Exception e) {
            System.err.println("\n发生错误：" + e.getMessage());
            e.printStackTrace();
            System.out.println("\n可能的原因:");
            System.out.println("1. 数据库未启动");
            System.out.println("2. 数据库名称不是 'keshe'");
            System.out.println("3. 用户名或密码错误");
            System.out.println("4. water_quality_standards 表不存在");
        }
    }
}
