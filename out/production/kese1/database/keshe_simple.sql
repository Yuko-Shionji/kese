-- 农村饮水安全监测管理系统数据库设计 (简化版 - 仅表结构)
-- 适用于 keshe 数据库
-- MySQL 8

SET FOREIGN_KEY_CHECKS=0;

-- 1. 用户表（管理员、操作员等）
CREATE TABLE IF NOT EXISTS users (
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

-- 3. 水源表（水库、井、河流等）
CREATE TABLE IF NOT EXISTS water_sources (
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
CREATE TABLE IF NOT EXISTS water_plants (
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
CREATE TABLE IF NOT EXISTS water_quality_standards (
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
CREATE TABLE IF NOT EXISTS water_quality_records (
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
CREATE TABLE IF NOT EXISTS water_quality_details (
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
CREATE TABLE IF NOT EXISTS warnings (
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
CREATE TABLE IF NOT EXISTS equipments (
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
CREATE TABLE IF NOT EXISTS system_logs (
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

SET FOREIGN_KEY_CHECKS=1;

-- 插入测试数据 - 用户
INSERT INTO users (username, password, real_name, role, phone, email) VALUES
('admin', 'admin123', '系统管理员', 'admin', '13800138000', 'admin@example.com'),
('operator1', 'oper123', '操作员 1', 'operator', '13800138001', 'oper1@example.com'),
('operator2', 'oper123', '操作员 2', 'operator', '13800138002', 'oper2@example.com'),
('viewer1', 'view123', '查看员 1', 'viewer', '13800138003', 'view1@example.com');

-- 插入测试数据 - 区域
INSERT INTO regions (region_code, region_name, parent_id, level, full_path) VALUES
('110000', '安徽省', 0, 'province', '安徽省'),
('110100', '合肥市', 1, 'city', '安徽省/合肥市'),
('110101', '瑶海区', 2, 'district', '安徽省/合肥市/瑶海区'),
('110102', '庐阳区', 2, 'district', '安徽省/合肥市/庐阳区'),
('110103', '蜀山区', 2, 'district', '安徽省/合肥市/蜀山区');

-- 插入测试数据 - 水源
INSERT INTO water_sources (source_name, source_type, region_id, location_detail, longitude, latitude, capacity) VALUES
('董铺水库', 'reservoir', 2, '合肥市西北部', 117.15, 31.92, 2.50),
('大房郢水库', 'reservoir', 2, '合肥市东部', 117.28, 31.88, 1.80),
('地下水源 1 号', 'well', 3, '瑶海区地下', 117.30, 31.87, 0.50);

-- 插入测试数据 - 水厂
INSERT INTO water_plants (plant_name, plant_code, region_id, source_id, address, design_capacity, service_population, contact_person, contact_phone) VALUES
('合肥一水厂', 'HF001', 2, 1, '合肥市瑶海区和平路', 50.00, 500000, '张厂长', '13800138010'),
('合肥二水厂', 'HF002', 2, 2, '合肥市庐阳区长江路', 40.00, 400000, '李厂长', '13800138011'),
('合肥三水厂', 'HF003', 3, 3, '合肥市蜀山区望江路', 30.00, 300000, '王厂长', '13800138012');

-- 插入测试数据 - 水质检测标准
INSERT INTO water_quality_standards (item_name, item_code, unit, standard_value, min_value, max_value, category) VALUES
('pH 值', 'PH001', '无量纲', 7.00, 6.50, 8.50, 'physical'),
('浊度', 'TURB001', 'NTU', NULL, NULL, 1.00, 'physical'),
('溶解氧', 'DO001', 'mg/L', 5.00, 5.00, NULL, 'chemical'),
('余氯', 'CL001', 'mg/L', 0.30, 0.30, 4.00, 'chemical'),
('大肠菌群', 'COLI001', '个/L', NULL, NULL, 3.00, 'biological'),
('总硬度', 'HARD001', 'mg/L', NULL, NULL, 450.00, 'chemical'),
('氨氮', 'NH3001', 'mg/L', NULL, NULL, 0.50, 'chemical'),
('高锰酸盐指数', 'COD001', 'mg/L', NULL, NULL, 3.00, 'chemical');
