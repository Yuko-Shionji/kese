@echo off
chcp 65001 >nul
echo ============================================
echo 修复锁等待超时问题 - 触发器优化
echo ============================================
echo.

echo 正在连接数据库 rural_water_db...
echo.

mysql -u root -p rural_water_db < fix_lock_timeout.sql

echo.
echo ============================================
echo 修复完成！
echo ============================================
echo.
echo 请重启应用并测试添加水质检测记录功能
echo.
pause
