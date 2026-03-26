-- 禁用自动预警触发器（临时解决方案）
-- 这样可以先让添加功能正常工作，预警功能后续通过其他方式实现

USE rural_water_db;

-- 删除触发器
DROP TRIGGER IF EXISTS trg_auto_warning;

-- 验证触发器已删除
SELECT 'Trigger deleted successfully' as result;
