@echo off
chcp 65001 >nul
echo =====================================
echo 水质检测标准数据初始化
echo =====================================
echo.

set DB_SERVER=localhost
set DB_NAME=keshe
set DB_USER=root
set DB_PASSWORD=1234
set SQL_FILE=database\init_water_quality_standards.sql

echo 数据库服务器：%DB_SERVER%
echo 数据库名称：%DB_NAME%
echo 用户：%DB_USER%
echo SQL 文件：%SQL_FILE%
echo.

if not exist %SQL_FILE% (
    echo 错误：找不到 SQL 文件 %SQL_FILE%
    exit /b 1
)

echo 正在执行 SQL 脚本...
echo.

mysql -h%DB_SERVER% -u%DB_USER% -p%DB_PASSWORD% %DB_NAME% --default-character-set=utf8 < %SQL_FILE%

if %ERRORLEVEL% EQU 0 (
    echo.
    echo =====================================
    echo √ 数据初始化成功！
    echo =====================================
    echo.
    echo 已添加以下类别的水质检测标准：
    echo   • 物理指标 (physical)
    echo   • 化学指标 (chemical)
    echo   • 生物指标 (biological)
    echo   • 放射性指标 (radiological)
    echo.
    echo 现在可以正常使用水质检测功能了！
    echo.
    
    echo 查询验证结果:
    mysql -h%DB_SERVER% -u%DB_USER% -p%DB_PASSWORD% %DB_NAME% --default-character-set=utf8 -e "SELECT category AS '类别', COUNT(*) AS '数量' FROM water_quality_standards WHERE is_active = 1 GROUP BY category ORDER BY category;"
    
) else (
    echo.
    echo =====================================
    echo × 数据初始化失败！
    echo =====================================
    echo.
    echo 请检查:
    echo   1. MySQL 服务是否已启动
    echo   2. 数据库 '%DB_NAME%' 是否存在
    echo   3. 用户名和密码是否正确
    echo   4. 是否已安装 MySQL 客户端工具
    echo.
)

pause
