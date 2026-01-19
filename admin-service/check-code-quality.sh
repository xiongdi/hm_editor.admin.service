#!/bin/bash
# 代码质量检查脚本
# 使用方法: ./check-code-quality.sh [format|checkstyle|pmd|spotbugs|jacoco|all]

ACTION=${1:-all}

echo "========================================"
echo "代码质量检查工具"
echo "========================================"
echo ""

case "$ACTION" in
    all)
        echo "运行所有检查..."
        mvn clean verify
        ;;
    format)
        echo "格式化代码..."
        echo "注意: 如果Spotless不可用，请使用IDE格式化"
        if mvn spotless:apply 2>/dev/null; then
            echo "格式化完成！"
        else
            echo "Spotless格式化失败，请使用IDE格式化方式"
            echo "运行 ./format-code.sh 查看IDE格式化说明"
        fi
        ;;
    checkstyle)
        echo "运行Checkstyle检查..."
        mvn checkstyle:check
        ;;
    pmd)
        echo "运行PMD检查..."
        mvn pmd:check
        ;;
    spotbugs)
        echo "运行SpotBugs检查..."
        mvn spotbugs:check
        ;;
    jacoco)
        echo "生成JaCoCo覆盖率报告..."
        mvn jacoco:report
        echo "报告位置: target/site/jacoco/index.html"
        ;;
    *)
        echo "未知参数: $ACTION"
        echo "使用方法: ./check-code-quality.sh [format|checkstyle|pmd|spotbugs|jacoco|all]"
        exit 1
        ;;
esac

echo ""
echo "检查完成！"
