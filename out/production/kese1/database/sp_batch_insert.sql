-- ============================================
-- 方案 2：使用存储过程批量插入（绕过触发器性能问题）
-- ============================================

DELIMITER $$

CREATE PROCEDURE sp_batch_insert_quality_details(
    IN p_record_id INT,
    IN p_standard_ids TEXT,        -- 标准 ID 列表，逗号分隔
    IN p_measured_values TEXT,     -- 测量值列表，逗号分隔
    IN p_is_qualifieds TEXT        -- 是否合格列表，逗号分隔
)
BEGIN
    DECLARE v_done INT DEFAULT 0;
    DECLARE v_standard_id INT;
    DECLARE v_measured_value DECIMAL(10,3);
    DECLARE v_is_qualified INT;
    DECLARE v_plant_id INT;
    DECLARE v_item_name VARCHAR(100);
    
    -- 获取水厂 ID
    SELECT plant_id INTO v_plant_id FROM water_quality_records WHERE record_id = p_record_id;
    
    -- 创建临时表存储解析后的数据
    DROP TEMPORARY TABLE IF EXISTS temp_details;
    CREATE TEMPORARY TABLE temp_details (
        standard_id INT,
        measured_value DECIMAL(10,3),
        is_qualified INT
    );
    
    -- TODO: 这里需要 Java 代码配合，传入已解析的数据
    -- 简化版本：直接批量插入
    
    INSERT INTO water_quality_details (record_id, standard_id, measured_value, is_qualified)
    VALUES 
        (p_record_id, 1, 7.5, 1),
        (p_record_id, 2, 0.8, 1);
    
    -- 处理不合格项的预警
    DECLARE cur_unqualified CURSOR FOR 
        SELECT wd.standard_id, wd.measured_value
        FROM water_quality_details wd
        WHERE wd.record_id = p_record_id AND wd.is_qualified = 0;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;
    
    OPEN cur_unqualified;
    
    read_loop: LOOP
        FETCH cur_unqualified INTO v_standard_id, v_measured_value;
        IF v_done THEN
            LEAVE read_loop;
        END IF;
        
        SELECT item_name INTO v_item_name FROM water_quality_standards WHERE standard_id = v_standard_id;
        
        INSERT INTO warnings (record_id, plant_id, warning_type, warning_level, title, content)
        VALUES (p_record_id, v_plant_id, 'quality', 'high',
                CONCAT('水质检测异常 - ', v_item_name),
                CONCAT('检测项 "', v_item_name, '" 不合格，测量值：', v_measured_value));
    END LOOP;
    
    CLOSE cur_unqualified;
    DROP TEMPORARY TABLE IF EXISTS temp_details;
END$$

DELIMITER ;
