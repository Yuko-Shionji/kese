package com.ruralwater;

import com.ruralwater.service.RegionService;
import com.ruralwater.entity.Region;

import java.util.List;

/**
 * 测试省份数据初始化
 */
public class TestProvinceData {
    public static void main(String[] args) {
        System.out.println("=== 测试省份数据初始化 ===\n");
        
        RegionService regionService = new RegionService();
        
        try {
            // 测试 1：获取所有省份
            System.out.println("1. 尝试获取所有省份数据...");
            List<Region> provinces = regionService.getAllProvinces();
            
            if (provinces == null || provinces.isEmpty()) {
                System.out.println("✗ 省份数据为空！数据库中没有省份数据。");
                System.out.println("\n请执行以下步骤：");
                System.out.println("1. 打开 MySQL 客户端");
                System.out.println("2. 运行命令：USE keshe;");
                System.out.println("3. 运行 SQL 文件：source E:\\kese1\\database\\init_provinces.sql;");
                System.out.println("4. 或者手动执行该文件中的 INSERT 语句");
            } else {
                System.out.println("✓ 成功获取到 " + provinces.size() + " 个省份：\n");
                
                for (int i = 0; i < provinces.size(); i++) {
                    Region p = provinces.get(i);
                    System.out.printf("%2d. %s (编码：%s, ID: %d)\n", 
                        i + 1, p.getRegionName(), p.getRegionCode(), p.getRegionId());
                }
                
                // 测试 2：获取特定省份
                System.out.println("\n2. 测试查询特定省份（例如：河北省）...");
                List<Region> hebeiList = regionService.searchRegions("河北");
                if (!hebeiList.isEmpty()) {
                    Region hebei = hebeiList.get(0);
                    System.out.println("✓ 找到： " + hebei.getRegionName() + 
                                     " (路径：" + hebei.getFullPath() + ")");
                } else {
                    System.out.println("✗ 未找到河北省");
                }
                
                // 测试 3：统计总数
                System.out.println("\n3. 区域总数统计...");
                int totalCount = regionService.getTotalCount();
                System.out.println("   区域总数：" + totalCount);
                
                if (totalCount > provinces.size()) {
                    System.out.println("   ✓ 除了省份，还有其他级别的区域数据");
                } else if (totalCount == provinces.size()) {
                    System.out.println("   - 只有省份数据，没有市、县等下级区域");
                }
            }
            
        } catch (Exception e) {
            System.err.println("✗ 测试失败：" + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}
