-- 检查并初始化省份数据
USE keshe;

-- 检查 regions 表是否存在
SHOW TABLES LIKE 'regions';

-- 如果存在，查看现有数据
SELECT COUNT(*) as province_count FROM regions;
SELECT * FROM regions WHERE level = 'province';
