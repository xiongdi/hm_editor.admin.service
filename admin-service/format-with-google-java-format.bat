@echo off
REM 使用Google Java Format格式化代码
REM 需要先下载google-java-format工具

echo ========================================
echo Google Java Format 代码格式化工具
echo ========================================
echo.

REM 检查是否安装了google-java-format
where google-java-format >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo 错误: 未找到 google-java-format 工具
    echo.
    echo 请先安装 Google Java Format:
    echo 1. 下载: https://github.com/google/google-java-format/releases
    echo 2. 将 google-java-format-1.22.0-all-deps.jar 放到项目根目录
    echo 3. 或者使用IDE格式化功能（推荐）
    echo.
    pause
    exit /b 1
)

echo 开始格式化Java文件...
echo.

REM 格式化所有Java文件
for /r "src\main\java" %%f in (*.java) do (
    echo 格式化: %%f
    google-java-format --replace --aosp "%%f"
)

echo.
echo 格式化完成！
pause
