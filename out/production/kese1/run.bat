@echo off
cd /d %~dp0
java -cp "lib/mysql-connector-java-5.1.47.jar;lib/poi-5.2.3.jar;lib/poi-ooxml-5.2.3.jar;out/production/kese1" com.ruralwater.MainApp
pause
