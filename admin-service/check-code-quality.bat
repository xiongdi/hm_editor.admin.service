@echo off
REM 代码质量检查脚本
REM 使用方法: check-code-quality.bat [format|checkstyle|pmd|spotbugs|jacoco|all]

setlocal enabledelayedexpansion

if "%1"=="" (
    set ACTION=all
) else (
    set ACTION=%1
)

echo ========================================
echo 代码质量检查工具
echo ========================================
echo.

if "%ACTION%"=="all" (
    echo 运行所有检查...
    call mvn clean verify
    goto :end
)

if "%ACTION%"=="format" (
    echo 格式化代码...
    echo 注意: 如果Spotless不可用，请使用IDE格式化
    call mvn spotless:apply 2>nul
    if errorlevel 1 (
        echo Spotless格式化失败，请使用IDE格式化方式
        echo 运行 format-code.bat 查看IDE格式化说明
    ) else (
        echo 格式化完成！
    )
    goto :end
)

if "%ACTION%"=="checkstyle" (
    echo 运行Checkstyle检查...
    call mvn checkstyle:check
    goto :end
)

if "%ACTION%"=="pmd" (
    echo 运行PMD检查...
    call mvn pmd:check
    goto :end
)

if "%ACTION%"=="spotbugs" (
    echo 运行SpotBugs检查...
    call mvn spotbugs:check
    goto :end
)

if "%ACTION%"=="jacoco" (
    echo 生成JaCoCo覆盖率报告...
    call mvn jacoco:report
    echo 报告位置: target\site\jacoco\index.html
    goto :end
)

echo 未知参数: %ACTION%
echo 使用方法: check-code-quality.bat [format|checkstyle|pmd|spotbugs|jacoco|all]
goto :end

:end
echo.
echo 检查完成！
pause
