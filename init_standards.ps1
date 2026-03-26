# 水质检测标准数据初始化脚本
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "水质检测标准数据初始化" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# 数据库配置
$dbServer = "localhost"
$dbName = "keshe"
$dbUser = "root"
$dbPassword = "1234"
$sqlFile = "database\init_water_quality_standards.sql"

Write-Host "数据库服务器：$dbServer" -ForegroundColor Yellow
Write-Host "数据库名称：$dbName" -ForegroundColor Yellow
Write-Host "用户：$dbUser" -ForegroundColor Yellow
Write-Host "SQL 文件：$sqlFile" -ForegroundColor Yellow
Write-Host ""

# 检查 SQL 文件是否存在
if (-Not (Test-Path $sqlFile)) {
    Write-Host "错误：找不到 SQL 文件 $sqlFile" -ForegroundColor Red
    exit 1
}

Write-Host "正在执行 SQL 脚本..." -ForegroundColor Green
Write-Host ""

# 执行 SQL 脚本
try {
    $sqlContent = Get-Content $sqlFile -Raw -Encoding UTF8
    
    # 使用 mysql 命令行执行
    $processInfo = New-Object System.Diagnostics.ProcessStartInfo
    $processInfo.FileName = "mysql"
    $processInfo.Arguments = "-h$dbServer -u$dbUser -p$dbPassword $dbName --default-character-set=utf8"
    $processInfo.RedirectStandardInput = $true
    $processInfo.RedirectStandardOutput = $true
    $processInfo.RedirectStandardError = $true
    $processInfo.UseShellExecute = $false
    $processInfo.CreateNoWindow = $true
    $processInfo.StandardInputEncoding = [System.Text.Encoding]::UTF8
    
    $process = New-Object System.Diagnostics.Process
    $process.StartInfo = $processInfo
    $process.Start() | Out-Null
    
    $process.StandardInput.Write($sqlContent)
    $process.StandardInput.Close()
    
    $output = $process.StandardOutput.ReadToEnd()
    $errorOutput = $process.StandardError.ReadToEnd()
    
    $process.WaitForExit()
    
    if ($process.ExitCode -eq 0) {
        Write-Host ""
        Write-Host "=====================================" -ForegroundColor Green
        Write-Host "✓ 数据初始化成功！" -ForegroundColor Green
        Write-Host "=====================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "已添加以下类别的水质检测标准：" -ForegroundColor Cyan
        Write-Host "  • 物理指标 (physical)" -ForegroundColor White
        Write-Host "  • 化学指标 (chemical)" -ForegroundColor White
        Write-Host "  • 生物指标 (biological)" -ForegroundColor White
        Write-Host "  • 放射性指标 (radiological)" -ForegroundColor White
        Write-Host ""
        Write-Host "现在可以正常使用水质检测功能了！" -ForegroundColor Green
        Write-Host ""
        
        # 显示输出
        if ($output -ne "") {
            Write-Host "执行结果:" -ForegroundColor Yellow
            Write-Host $output
        }
    } else {
        Write-Host ""
        Write-Host "=====================================" -ForegroundColor Red
        Write-Host "✗ 数据初始化失败！" -ForegroundColor Red
        Write-Host "=====================================" -ForegroundColor Red
        Write-Host ""
        Write-Host "退出代码：$($process.ExitCode)" -ForegroundColor Red
        
        if ($errorOutput -ne "") {
            Write-Host ""
            Write-Host "错误信息:" -ForegroundColor Red
            Write-Host $errorOutput -ForegroundColor Red
        }
        
        Write-Host ""
        Write-Host "请检查:" -ForegroundColor Yellow
        Write-Host "  1. MySQL 服务是否已启动" -ForegroundColor White
        Write-Host "  2. 数据库 'keshe' 是否存在" -ForegroundColor White
        Write-Host "  3. 用户名和密码是否正确" -ForegroundColor White
        Write-Host "  4. 是否已安装 MySQL 客户端工具" -ForegroundColor White
        Write-Host ""
    }
} catch {
    Write-Host ""
    Write-Host "发生异常：$_" -ForegroundColor Red
    Write-Host ""
    Write-Host "请确保:" -ForegroundColor Yellow
    Write-Host "  1. MySQL 在系统 PATH 中可用" -ForegroundColor White
    Write-Host "  2. 或者手动执行：mysql -hlocalhost -uroot -p1234 keshe < database\init_water_quality_standards.sql" -ForegroundColor White
    Write-Host ""
}

Write-Host "按任意键退出..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
