# 农村水务管理系统启动脚本
Write-Host "正在启动农村水务管理系统..." -ForegroundColor Green

# 设置类路径
$classpath = "lib/mysql-connector-java-5.1.47.jar;lib/poi-5.2.3.jar;lib/poi-ooxml-5.2.3.jar;out/production/kese1"

# 启动应用程序
java -cp $classpath com.ruralwater.MainApp

# 如果程序退出，显示提示信息
if ($LASTEXITCODE -ne 0) {
    Write-Host "程序已退出，错误代码：$LASTEXITCODE" -ForegroundColor Red
    pause
}
