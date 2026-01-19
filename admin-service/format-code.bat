@echo off
REM 代码格式化脚本
REM 使用IDE或Maven工具格式化代码

echo ========================================
echo 代码格式化工具
echo ========================================
echo.
echo 请选择格式化方式:
echo 1. 使用IDE格式化 (推荐)
echo 2. 使用Maven格式化 (如果Spotless可用)
echo.

set /p choice="请选择 (1/2): "

if "%choice%"=="1" (
    echo.
    echo 请在IDE中执行以下操作:
    echo.
    echo IntelliJ IDEA:
    echo   1. 选择所有Java文件 (Ctrl+A)
    echo   2. 代码 -> 重新格式化代码 (Ctrl+Alt+L)
    echo   3. 代码 -> 优化导入 (Ctrl+Alt+O)
    echo.
    echo Eclipse:
    echo   1. 选择所有Java文件
    echo   2. 源代码 -> 格式化 (Shift+Ctrl+F)
    echo   3. 源代码 -> 组织导入 (Shift+Ctrl+O)
    echo.
    pause
    goto :end
)

if "%choice%"=="2" (
    echo.
    echo 尝试使用Spotless格式化...
    call mvn spotless:apply
    if errorlevel 1 (
        echo.
        echo Spotless格式化失败，请使用IDE格式化方式
    ) else (
        echo.
        echo 格式化完成！
    )
    goto :end
)

echo 无效选择
goto :end

:end
pause
