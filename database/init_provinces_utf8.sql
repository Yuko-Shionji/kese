-- 初始化中国省份数据
-- 用于农村饮水安全监测管理系统

-- 使用数据库
USE keshe;

-- 如果表不存在则创建 regions 表
CREATE TABLE IF NOT EXISTS regions (
    region_id INT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL UNIQUE,
    region_name VARCHAR(100) NOT NULL,
    parent_id INT DEFAULT 0,
    level ENUM('province', 'city', 'district', 'town', 'village') NOT NULL,
    full_path VARCHAR(500),
    INDEX idx_parent_id (parent_id),
    INDEX idx_region_code (region_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 清空现有数据（可选，如果需要保留数据请注释掉这行）
-- 注意：由于外键约束，使用 DELETE 而不是 TRUNCATE


-- 插入中国 34 个省级行政区（23 个省、5 个自治区、4 个直辖市、2 个特别行政区）
INSERT INTO regions (region_code, region_name, parent_id, level, full_path) VALUES

-- 4 个直辖市
('110000', '北京市', 0, 'province', '北京市'),
('120000', '天津市', 0, 'province', '天津市'),
('310000', '上海市', 0, 'province', '上海市'),
('500000', '重庆市', 0, 'province', '重庆市'),

-- 23 个省
('130000', '河北省', 0, 'province', '河北省'),
('140000', '山西省', 0, 'province', '山西省'),
('210000', '辽宁省', 0, 'province', '辽宁省'),
('220000', '吉林省', 0, 'province', '吉林省'),
('230000', '黑龙江省', 0, 'province', '黑龙江省'),
('320000', '江苏省', 0, 'province', '江苏省'),
('330000', '浙江省', 0, 'province', '浙江省'),
('340000', '安徽省', 0, 'province', '安徽省'),
('350000', '福建省', 0, 'province', '福建省'),
('360000', '江西省', 0, 'province', '江西省'),
('370000', '山东省', 0, 'province', '山东省'),
('410000', '河南省', 0, 'province', '河南省'),
('420000', '湖北省', 0, 'province', '湖北省'),
('430000', '湖南省', 0, 'province', '湖南省'),
('440000', '广东省', 0, 'province', '广东省'),
('450000', '海南省', 0, 'province', '海南省'),
('460000', '广西自治区', 0, 'province', '广西自治区'),
('510000', '四川省', 0, 'province', '四川省'),
('520000', '贵州省', 0, 'province', '贵州省'),
('530000', '云南省', 0, 'province', '云南省'),
('610000', '陕西省', 0, 'province', '陕西省'),
('620000', '甘肃省', 0, 'province', '甘肃省'),
('630000', '青海省', 0, 'province', '青海省'),
('640000', '宁夏省', 0, 'province', '宁夏省'),

-- 5 个自治区
('150000', '内蒙古自治区', 0, 'province', '内蒙古自治区'),
('540000', '西藏自治区', 0, 'province', '西藏自治区'),
('650000', '新疆维吾尔自治区', 0, 'province', '新疆维吾尔自治区'),

-- 2 个特别行政区
('810000', '香港特别行政区', 0, 'province', '香港特别行政区'),
('820000', '澳门特别行政区', 0, 'province', '澳门特别行政区');

