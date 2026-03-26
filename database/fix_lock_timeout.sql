-- ============================================
-- 修复锁等待超时问题
-- ============================================

-- 1. 查看当前正在运行的事务和锁等待情况
SHOW PROCESSLIST;
SHOW ENGINE INNODB STATUS;

-- 2. 删除旧的触发器（包含冗余查询的版本）
DROP TRIGGER IF EXISTS trg_auto_warning;

DELIMITER $$

-- 3. 创建优化后的触发器（减少不必要的查询）
CREATE TRIGGER trg_auto_warning
AFTER INSERT ON water_quality_details
FOR EACH ROW
BEGIN
    IF NEW.is_qualified = 0 THEN
        DECLARE v_plant_id INT;
        DECLARE v_item_name VARCHAR(100);
        
        -- 只查询必要的字段
        SELECT wr.plant_id INTO v_plant_id 
        FROM water_quality_records wr 
        WHERE wr.record_id = NEW.record_id;
        
        SELECT ws.item_name INTO v_item_name 
        FROM water_quality_standards ws 
        WHERE ws.standard_id = NEW.standard_id;
        
        -- 只有当查询到数据时才插入预警
        IF v_plant_id IS NOT NULL AND v_item_name IS NOT NULL THEN
            INSERT INTO warnings (record_id, plant_id, warning_type, warning_level, title, content)
            VALUES (NEW.record_id, v_plant_id, 'quality', 'high', 
                    CONCAT('水质检测异常 - ', v_item_name),
                    CONCAT('检测项 "', v_item_name, '" 不合格，测量值：', NEW.measured_value));
        END IF;
    END IF;
END$$

DELIMITER ;

-- 4. 验证触发器是否创建成功
SHOW TRIGGERS LIKE 'water_quality_details';
