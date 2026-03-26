-- 彻底删除触发器解决锁超时问题
USE rural_water_db;

-- 1. 确认触发器存在
SELECT 'Before drop:' as status;
SHOW TRIGGERS;

-- 2. 删除触发器
DROP TRIGGER IF EXISTS trg_auto_warning;

-- 3. 验证删除
SELECT 'After drop:' as status;
SHOW TRIGGERS;

-- 4. 确认表结构正常
SELECT COUNT(*) as total_records FROM water_quality_records;
SELECT COUNT(*) as total_details FROM water_quality_details;
SELECT COUNT(*) as total_warnings FROM warnings;
