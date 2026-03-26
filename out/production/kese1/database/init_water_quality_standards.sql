-- 水质检测标准数据初始化脚本
-- 用于向 water_quality_standards 表插入完整的检测标准数据

-- 清空现有数据（可选，如果需要重置）
-- DELETE FROM water_quality_standards;

-- 插入物理指标检测标准
INSERT INTO water_quality_standards (item_name, item_code, unit, standard_value, min_value, max_value, category, description, is_active) VALUES
('pH 值', 'PH001', '无量纲', 7.00, 6.50, 8.50, 'physical', '生活饮用水 pH 值应在 6.5-8.5 之间', 1),
('浊度', 'TURB001', 'NTU', NULL, NULL, 1.00, 'physical', '饮用水浊度不得超过 1 NTU', 1),
('色度', 'COLOR001', '度', NULL, NULL, 15.00, 'physical', '饮用水色度不得超过 15 度', 1),
('臭和味', 'ODOR001', '-', NULL, NULL, NULL, 'physical', '不得有异臭异味', 1),
('肉眼可见物', 'VISIBLE001', '-', NULL, NULL, NULL, 'physical', '不得含有肉眼可见物', 1),
('电导率', 'COND001', 'μS/cm', NULL, NULL, 1000.00, 'physical', '反映水中离子总量', 1);

-- 插入化学指标检测标准
INSERT INTO water_quality_standards (item_name, item_code, unit, standard_value, min_value, max_value, category, description, is_active) VALUES
('溶解氧', 'DO001', 'mg/L', 5.00, 5.00, NULL, 'chemical', '水中溶解氧含量应充足', 1),
('余氯', 'CL001', 'mg/L', 0.30, 0.30, 4.00, 'chemical', '出厂水余氯不得低于 0.3mg/L', 1),
('总硬度', 'HARD001', 'mg/L', NULL, NULL, 450.00, 'chemical', '以 CaCO3 计，不得超过 450mg/L', 1),
('氨氮', 'NH3001', 'mg/L', NULL, NULL, 0.50, 'chemical', '反映水体受污染程度', 1),
('高锰酸盐指数', 'COD001', 'mg/L', NULL, NULL, 3.00, 'chemical', '反映水中有机物含量', 1),
('硝酸盐氮', 'NO3001', 'mg/L', NULL, NULL, 20.00, 'chemical', '地下水源硝酸盐氮不得超过 20mg/L', 1),
('亚硝酸盐氮', 'NO2001', 'mg/L', NULL, NULL, 1.00, 'chemical', '不得超过 1.0mg/L', 1),
('硫酸盐', 'SO4001', 'mg/L', NULL, NULL, 250.00, 'chemical', '不得超过 250mg/L', 1),
('氯化物', 'CL002', 'mg/L', NULL, NULL, 250.00, 'chemical', '不得超过 250mg/L', 1),
('铁', 'FE001', 'mg/L', NULL, NULL, 0.30, 'chemical', '不得超过 0.3mg/L', 1),
('锰', 'MN001', 'mg/L', NULL, NULL, 0.10, 'chemical', '不得超过 0.1mg/L', 1),
('铜', 'CU001', 'mg/L', NULL, NULL, 1.00, 'chemical', '不得超过 1.0mg/L', 1),
('锌', 'ZN001', 'mg/L', NULL, NULL, 1.00, 'chemical', '不得超过 1.0mg/L', 1),
('挥发酚', 'PHENOL001', 'mg/L', NULL, NULL, 0.002, 'chemical', '不得超过 0.002mg/L', 1),
('阴离子合成洗涤剂', 'LAS001', 'mg/L', NULL, NULL, 0.30, 'chemical', '不得超过 0.3mg/L', 1),
('耗氧量', 'OC001', 'mg/L', NULL, NULL, 3.00, 'chemical', '以 O2 计，不得超过 3mg/L', 1);

-- 插入生物指标检测标准
INSERT INTO water_quality_standards (item_name, item_code, unit, standard_value, min_value, max_value, category, description, is_active) VALUES
('大肠菌群', 'COLI001', '个/L', NULL, NULL, 3.00, 'biological', '每升水样中不得检出大肠菌群', 1),
('菌落总数', 'TVC001', 'CFU/mL', NULL, NULL, 100.00, 'biological', '每毫升水样菌落总数不得超过 100CFU', 1),
('耐热大肠菌群', 'FTC001', '个/L', NULL, NULL, 0.00, 'biological', '不得检出', 1),
('贾第鞭毛虫', 'GIARDIA001', '个/10L', NULL, NULL, 0.00, 'biological', '每 10 升水样中不得检出', 1),
('隐孢子虫', 'CRYPTO001', '个/10L', NULL, NULL, 0.00, 'biological', '每 10 升水样中不得检出', 1);

-- 插入放射性指标检测标准
INSERT INTO water_quality_standards (item_name, item_code, unit, standard_value, min_value, max_value, category, description, is_active) VALUES
('总α放射性', 'ALPHA001', 'Bq/L', NULL, NULL, 0.50, 'radiological', '总α放射性不得超过 0.5Bq/L', 1),
('总β放射性', 'BETA001', 'Bq/L', NULL, NULL, 1.00, 'radiological', '总β放射性不得超过 1.0Bq/L', 1);

-- 插入重金属及有毒物质检测标准
INSERT INTO water_quality_standards (item_name, item_code, unit, standard_value, min_value, max_value, category, description, is_active) VALUES
('铅', 'PB001', 'mg/L', NULL, NULL, 0.01, 'chemical', '不得超过 0.01mg/L', 1),
('汞', 'HG001', 'mg/L', NULL, NULL, 0.001, 'chemical', '不得超过 0.001mg/L', 1),
('镉', 'CD001', 'mg/L', NULL, NULL, 0.005, 'chemical', '不得超过 0.005mg/L', 1),
('铬 (六价)', 'CR6001', 'mg/L', NULL, NULL, 0.05, 'chemical', '六价铬不得超过 0.05mg/L', 1),
('砷', 'AS001', 'mg/L', NULL, NULL, 0.01, 'chemical', '不得超过 0.01mg/L', 1),
('硒', 'SE001', 'mg/L', NULL, NULL, 0.01, 'chemical', '不得超过 0.01mg/L', 1),
('氰化物', 'CN001', 'mg/L', NULL, NULL, 0.05, 'chemical', '不得超过 0.05mg/L', 1),
('氟化物', 'F001', 'mg/L', NULL, NULL, 1.00, 'chemical', '不得超过 1.0mg/L', 1);

-- 查询验证插入结果
SELECT 
    category AS '类别',
    COUNT(*) AS '数量',
    GROUP_CONCAT(item_name ORDER BY item_name SEPARATOR ', ') AS '检测项'
FROM water_quality_standards
WHERE is_active = 1
GROUP BY category
ORDER BY category;

-- 显示所有检测标准
SELECT 
    standard_id AS 'ID',
    item_name AS '检测项',
    item_code AS '编码',
    unit AS '单位',
    CASE 
        WHEN standard_value IS NOT NULL THEN CONCAT(standard_value, '(标准值)')
        WHEN min_value IS NOT NULL AND max_value IS NOT NULL THEN CONCAT(min_value, '-', max_value)
        WHEN min_value IS NOT NULL THEN CONCAT('≥', min_value)
        WHEN max_value IS NOT NULL THEN CONCAT('≤', max_value)
        ELSE '-'
    END AS '标准范围',
    category AS '类别',
    is_active AS '状态'
FROM water_quality_standards
WHERE is_active = 1
ORDER BY category, item_name;
