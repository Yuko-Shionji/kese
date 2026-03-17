-- 农村饮水安全监测管理系统数据库设计
-- MySQL 5.5+
-- 包含：8 张表、索引、触发器、存储过程、事务处理

-- 创建数据库
CREATE DATABASE IF NOT EXISTS rural_water_db CHARACTER SET utf8 COLLATE utf8_general_ci;
USE rural_water_db;

-- 1. 用户表（管理员、操作员等）
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    role ENUM('admin', 'operator', 'viewer') DEFAULT 'viewer',
    phone VARCHAR(20),
    email VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status TINYINT DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 2. 区域表（省市区县乡镇村）
CREATE TABLE regions (
    region_id INT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL UNIQUE,
    region_name VARCHAR(100) NOT NULL,
    parent_id INT DEFAULT 0,
    level ENUM('province', 'city', 'district', 'town', 'village') NOT NULL,
    full_path VARCHAR(500),
    INDEX idx_parent_id (parent_id),
    INDEX idx_region_code (region_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 3. 水源表（水库、井、河流等）
CREATE TABLE water_sources (
    source_id INT PRIMARY KEY AUTO_INCREMENT,
    source_name VARCHAR(100) NOT NULL,
    source_type ENUM('reservoir', 'well', 'river', 'lake', 'spring') NOT NULL,
    region_id INT NOT NULL,
    location_detail VARCHAR(200),
    longitude DECIMAL(10, 6),
    latitude DECIMAL(10, 6),
    capacity DECIMAL(10, 2),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (region_id) REFERENCES regions(region_id),
    INDEX idx_region_id (region_id),
    INDEX idx_source_type (source_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 4. 水厂表
CREATE TABLE water_plants (
    plant_id INT PRIMARY KEY AUTO_INCREMENT,
    plant_name VARCHAR(100) NOT NULL,
    plant_code VARCHAR(50) UNIQUE,
    region_id INT NOT NULL,
    source_id INT,
    address VARCHAR(200),
    design_capacity DECIMAL(10, 2),
    actual_capacity DECIMAL(10, 2),
    service_population INT,
    contact_person VARCHAR(50),
    contact_phone VARCHAR(20),
    status TINYINT DEFAULT 1,
    build_date DATE,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (region_id) REFERENCES regions(region_id),
    FOREIGN KEY (source_id) REFERENCES water_sources(source_id),
    INDEX idx_region_id (region_id),
    INDEX idx_source_id (source_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 5. 水质检测标准表
CREATE TABLE water_quality_standards (
    standard_id INT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(100) NOT NULL,
    item_code VARCHAR(20) UNIQUE,
    unit VARCHAR(20),
    standard_value DECIMAL(10, 3),
    min_value DECIMAL(10, 3),
    max_value DECIMAL(10, 3),
    category ENUM('physical', 'chemical', 'biological', 'radiological'),
    description TEXT,
    is_active TINYINT DEFAULT 1,
    INDEX idx_item_code (item_code),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 6. 水质检测记录表
CREATE TABLE water_quality_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    plant_id INT NOT NULL,
    sample_point VARCHAR(100),
    sample_time DATETIME NOT NULL,
    tester_id INT NOT NULL,
    review_status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    reviewer_id INT,
    review_time DATETIME,
    total_score DECIMAL(5, 2),
    conclusion ENUM('qualified', 'unqualified'),
    remark TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plant_id) REFERENCES water_plants(plant_id),
    FOREIGN KEY (tester_id) REFERENCES users(user_id),
    FOREIGN KEY (reviewer_id) REFERENCES users(user_id),
    INDEX idx_plant_id (plant_id),
    INDEX idx_sample_time (sample_time),
    INDEX idx_tester_id (tester_id),
    INDEX idx_review_status (review_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 7. 水质检测详情表
CREATE TABLE water_quality_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT NOT NULL,
    standard_id INT NOT NULL,
    measured_value DECIMAL(10, 3),
    is_qualified TINYINT,
    remark VARCHAR(200),
    FOREIGN KEY (record_id) REFERENCES water_quality_records(record_id) ON DELETE CASCADE,
    FOREIGN KEY (standard_id) REFERENCES water_quality_standards(standard_id),
    INDEX idx_record_id (record_id),
    INDEX idx_standard_id (standard_id),
    INDEX idx_qualified (is_qualified)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 8. 预警信息表
CREATE TABLE warnings (
    warning_id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT,
    plant_id INT NOT NULL,
    warning_type ENUM('quality', 'equipment', 'supply', 'other') NOT NULL,
    warning_level ENUM('low', 'medium', 'high', 'critical') NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    status ENUM('active', 'processed', 'ignored') DEFAULT 'active',
    handler_id INT,
    handle_time DATETIME,
    handle_result TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (record_id) REFERENCES water_quality_records(record_id),
    FOREIGN KEY (plant_id) REFERENCES water_plants(plant_id),
    FOREIGN KEY (handler_id) REFERENCES users(user_id),
    INDEX idx_plant_id (plant_id),
    INDEX idx_warning_level (warning_level),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 9. 设备表
CREATE TABLE equipments (
    equipment_id INT PRIMARY KEY AUTO_INCREMENT,
    equipment_name VARCHAR(100) NOT NULL,
    equipment_type VARCHAR(50),
    model VARCHAR(50),
    plant_id INT,
    purchase_date DATE,
    warranty_period INT,
    status ENUM('normal', 'maintenance', 'faulty', 'scrapped') DEFAULT 'normal',
    last_maintenance_date DATE,
    next_maintenance_date DATE,
    remark TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plant_id) REFERENCES water_plants(plant_id),
    INDEX idx_plant_id (plant_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 10. 系统日志表
CREATE TABLE system_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    operation VARCHAR(100),
    module VARCHAR(50),
    ip_address VARCHAR(50),
    content TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_operation (operation),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 创建触发器：当水质检测不合格时自动生成预警
DELIMITER $$
CREATE TRIGGER trg_auto_warning
AFTER INSERT ON water_quality_details
FOR EACH ROW
BEGIN
    IF NEW.is_qualified = 0 THEN
        DECLARE v_record_id INT;
        DECLARE v_plant_id INT;
        DECLARE v_item_name VARCHAR(100);
        
        SELECT record_id INTO v_record_id FROM water_quality_records WHERE record_id = NEW.record_id;
        SELECT plant_id INTO v_plant_id FROM water_quality_records WHERE record_id = NEW.record_id;
        SELECT item_name INTO v_item_name FROM water_quality_standards WHERE standard_id = NEW.standard_id;
        
        INSERT INTO warnings (record_id, plant_id, warning_type, warning_level, title, content)
        VALUES (v_record_id, v_plant_id, 'quality', 'high', 
                CONCAT('水质检测异常 - ', v_item_name),
                CONCAT('检测项 "', v_item_name, '" 不合格，测量值：', NEW.measured_value));
    END IF;
END$$
DELIMITER ;

-- 创建存储过程：统计某时间段内各水厂的水质合格率
DELIMITER $$
CREATE PROCEDURE sp_quality_statistics(
    IN start_date DATETIME,
    IN end_date DATETIME
)
BEGIN
    SELECT 
        wp.plant_name,
        COUNT(DISTINCT wr.record_id) as total_records,
        SUM(CASE WHEN wr.conclusion = 'qualified' THEN 1 ELSE 0 END) as qualified_count,
        ROUND(SUM(CASE WHEN wr.conclusion = 'qualified' THEN 1 ELSE 0 END) * 100.0 / COUNT(DISTINCT wr.record_id), 2) as pass_rate
    FROM water_plants wp
    LEFT JOIN water_quality_records wr ON wp.plant_id = wr.plant_id 
        AND wr.sample_time BETWEEN start_date AND end_date
    GROUP BY wp.plant_id, wp.plant_name
    ORDER BY pass_rate DESC;
END$$
DELIMITER ;

-- 创建存储过程：插入测试数据
DELIMITER $$
CREATE PROCEDURE sp_insert_test_data()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE j INT DEFAULT 1;
    
    -- 插入测试用户
    INSERT INTO users (username, password, real_name, role, phone, email) VALUES
    ('admin', 'admin123', '系统管理员', 'admin', '13800138000', 'admin@example.com'),
    ('operator1', 'oper123', '操作员 1', 'operator', '13800138001', 'oper1@example.com'),
    ('operator2', 'oper123', '操作员 2', 'operator', '13800138002', 'oper2@example.com'),
    ('viewer1', 'view123', '查看员 1', 'viewer', '13800138003', 'view1@example.com');
    
    -- 插入测试区域
    INSERT INTO regions (region_code, region_name, parent_id, level, full_path) VALUES
    ('110000', '安徽省', 0, 'province', '安徽省'),
    ('110100', '合肥市', 1, 'city', '安徽省/合肥市'),
    ('110101', '瑶海区', 2, 'district', '安徽省/合肥市/瑶海区'),
    ('110102', '庐阳区', 2, 'district', '安徽省/合肥市/庐阳区'),
    ('110103', '蜀山区', 2, 'district', '安徽省/合肥市/蜀山区');
    
    -- 插入测试水源
    INSERT INTO water_sources (source_name, source_type, region_id, location_detail, longitude, latitude, capacity) VALUES
    ('董铺水库', 'reservoir', 2, '合肥市西北部', 117.15, 31.92, 2.5),
    ('大房郢水库', 'reservoir', 2, '合肥市东部', 117.28, 31.88, 1.8),
    ('地下水源 1 号', 'well', 3, '瑶海区地下', 117.30, 31.87, 0.5);
    
    -- 插入测试水厂
    INSERT INTO water_plants (plant_name, plant_code, region_id, source_id, address, design_capacity, service_population, contact_person, contact_phone) VALUES
    ('合肥一水厂', 'HF001', 2, 1, '合肥市瑶海区和平路', 50.0, 500000, '张厂长', '13800138010'),
    ('合肥二水厂', 'HF002', 2, 2, '合肥市庐阳区长江路', 40.0, 400000, '李厂长', '13800138011'),
    ('合肥三水厂', 'HF003', 3, 3, '合肥市蜀山区望江路', 30.0, 300000, '王厂长', '13800138012');
    
    -- 插入水质检测标准
    INSERT INTO water_quality_standards (item_name, item_code, unit, standard_value, min_value, max_value, category) VALUES
    ('pH 值', 'PH001', '无量纲', 7.0, 6.5, 8.5, 'physical'),
    ('浊度', 'TURB001', 'NTU', NULL, NULL, 1.0, 'physical'),
    ('溶解氧', 'DO001', 'mg/L', 5.0, 5.0, NULL, 'chemical'),
    ('余氯', 'CL001', 'mg/L', 0.3, 0.3, 4.0, 'chemical'),
    ('大肠菌群', 'COLI001', '个/L', NULL, NULL, 3.0, 'biological'),
    ('总硬度', 'HARD001', 'mg/L', NULL, NULL, 450.0, 'chemical'),
    ('氨氮', 'NH3001', 'mg/L', NULL, NULL, 0.5, 'chemical'),
    ('高锰酸盐指数', 'COD001', 'mg/L', NULL, NULL, 3.0, 'chemical');
    
    -- 循环插入大量检测记录
    WHILE i <= 100 DO
        BEGIN
            DECLARE v_record_id INT;
            DECLARE v_sample_time DATETIME;
            SET v_sample_time = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 90) DAY);
            
            -- 插入检测记录
            INSERT INTO water_quality_records (plant_id, sample_point, sample_time, tester_id, review_status, conclusion)
            VALUES (FLOOR(1 + RAND() * 3), CONCAT('采样点', i), v_sample_time, 
                    FLOOR(2 + RAND() * 2), 'approved', IF(RAND() > 0.1, 'qualified', 'unqualified'));
            
            SET v_record_id = LAST_INSERT_ID();
            
            -- 为该记录插入检测详情
            SET j = 1;
            WHILE j <= 8 DO
                BEGIN
                    DECLARE v_measured_value DECIMAL(10,3);
                    DECLARE v_is_qualified TINYINT;
                    
                    -- 根据检测项生成合理的测量值
                    CASE j
                        WHEN 1 THEN SET v_measured_value = 6.5 + RAND() * 2.0; -- pH
                        WHEN 2 THEN SET v_measured_value = RAND() * 1.5; -- 浊度
                        WHEN 3 THEN SET v_measured_value = 4.0 + RAND() * 4.0; -- 溶解氧
                        WHEN 4 THEN SET v_measured_value = 0.2 + RAND() * 1.0; -- 余氯
                        WHEN 5 THEN SET v_measured_value = FLOOR(RAND() * 10); -- 大肠菌群
                        WHEN 6 THEN SET v_measured_value = 100 + RAND() * 300; -- 总硬度
                        WHEN 7 THEN SET v_measured_value = RAND() * 1.0; -- 氨氮
                        WHEN 8 THEN SET v_measured_value = 1.0 + RAND() * 3.0; -- 高锰酸盐指数
                    END CASE;
                    
                    -- 判断是否合格
                    SET v_is_qualified = 1;
                    IF j = 2 AND v_measured_value > 1.0 THEN SET v_is_qualified = 0;
                    ELSEIF j = 5 AND v_measured_value > 3.0 THEN SET v_is_qualified = 0;
                    ELSEIF j = 6 AND v_measured_value > 450.0 THEN SET v_is_qualified = 0;
                    END IF;
                    
                    INSERT INTO water_quality_details (record_id, standard_id, measured_value, is_qualified)
                    VALUES (v_record_id, j, v_measured_value, v_is_qualified);
                END;
                SET j = j + 1;
            END WHILE;
        END;
        SET i = i + 1;
    END WHILE;
    
    -- 插入测试设备
    INSERT INTO equipments (equipment_name, equipment_type, model, plant_id, purchase_date, warranty_period, status) VALUES
    ('pH 计', '检测设备', 'PH-2000', 1, '2023-01-15', 24, 'normal'),
    ('浊度仪', '检测设备', 'TURB-3000', 1, '2023-02-20', 24, 'normal'),
    ('余氯检测仪', '检测设备', 'CL-500', 2, '2023-03-10', 12, 'maintenance'),
    ('水泵 A', '供水设备', 'PUMP-1000', 2, '2022-06-01', 36, 'normal'),
    ('消毒设备', '处理设备', 'DIS-800', 3, '2023-05-01', 18, 'normal');
END$$
DELIMITER ;

-- 执行存储过程插入测试数据
CALL sp_insert_test_data();

-- 查询示例：分页、排序、关键字查询
-- 示例 1: 分页查询水质检测记录
-- SELECT * FROM water_quality_records ORDER BY sample_time DESC LIMIT 0, 10;

-- 示例 2: 关键字查询（按水厂名称模糊查询）
-- SELECT wr.*, wp.plant_name 
-- FROM water_quality_records wr
-- INNER JOIN water_plants wp ON wr.plant_id = wp.plant_id
-- WHERE wp.plant_name LIKE '%合肥%'
-- ORDER BY wr.sample_time DESC;

-- 示例 3: 多表关联查询
-- SELECT 
--     wr.record_id,
--     wp.plant_name,
--     ws.source_name,
--     u.real_name as tester_name,
--     wr.sample_time,
--     wr.conclusion
-- FROM water_quality_records wr
-- INNER JOIN water_plants wp ON wr.plant_id = wp.plant_id
-- INNER JOIN water_sources ws ON wp.source_id = ws.source_id
-- INNER JOIN users u ON wr.tester_id = u.user_id
-- WHERE wr.review_status = 'approved'
-- ORDER BY wr.sample_time DESC
-- LIMIT 0, 20;
