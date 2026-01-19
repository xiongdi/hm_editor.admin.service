# 代码质量检查工具使用指南

本项目已集成以下代码质量检查工具：

## 工具列表

1. **Spotless** - 代码自动格式化
2. **Checkstyle** - 代码风格检查
3. **PMD** - 代码质量分析
4. **SpotBugs** - Bug检测
5. **JaCoCo** - 代码覆盖率

## 使用方法

### 1. 代码格式化

#### 方式一：使用IDE格式化（推荐）

**IntelliJ IDEA:**
1. 选择所有Java文件 (`Ctrl+A`)
2. 代码 -> 重新格式化代码 (`Ctrl+Alt+L`)
3. 代码 -> 优化导入 (`Ctrl+Alt+O`)

**Eclipse:**
1. 选择所有Java文件
2. 源代码 -> 格式化 (`Shift+Ctrl+F`)
3. 源代码 -> 组织导入 (`Shift+Ctrl+O`)

**VS Code:**
1. 安装 "EditorConfig for VS Code" 插件
2. 安装 "Java Extension Pack" 插件
3. 右键 -> 格式化文档 (`Shift+Alt+F`)

#### 方式二：使用Maven格式化（可选）

```bash
# 检查代码格式（不修改文件）
mvn spotless:check

# 自动格式化代码
mvn spotless:apply
```

**注意**：
- Spotless已升级到最新版本（2.46.1），支持Java 25
- 如果遇到错误，请使用IDE格式化方式（推荐）
- 或者使用Google Java Format命令行工具

#### 方式三：使用格式化脚本

```bash
# Windows
format-code.bat

# Linux/Mac
chmod +x format-code.sh
./format-code.sh
```

### 2. 运行所有检查

```bash
mvn clean verify
```

这个命令会：
- 检查代码格式（Spotless）
- 编译代码
- 运行Checkstyle检查
- 运行PMD检查
- 运行SpotBugs检查
- 生成JaCoCo覆盖率报告

### 3. 单独运行各个工具

#### Spotless
```bash
# 检查格式
mvn spotless:check

# 格式化代码
mvn spotless:apply
```

#### Checkstyle

#### Checkstyle
```bash
mvn checkstyle:check
```

生成报告：
```bash
mvn checkstyle:checkstyle
```
报告位置：`target/site/checkstyle.html`

#### PMD
```bash
mvn pmd:check
```

生成报告：
```bash
mvn pmd:pmd
```
报告位置：`target/site/pmd.html`

#### SpotBugs
```bash
mvn spotbugs:check
```

生成报告：
```bash
mvn spotbugs:spotbugs
```
报告位置：`target/site/spotbugs.html`

#### JaCoCo
```bash
mvn jacoco:report
```
报告位置：`target/site/jacoco/index.html`

### 4. 在IDE中集成

#### IntelliJ IDEA

1. **Spotless插件**（可选）：
   - 安装插件：`File` -> `Settings` -> `Plugins` -> 搜索 "Spotless"
   - 或者使用Maven命令：`mvn spotless:apply`

2. **Google Java Format插件**（推荐）：
   - 安装插件：`File` -> `Settings` -> `Plugins` -> 搜索 "Google Java Format"
   - 配置：`File` -> `Settings` -> `Tools` -> `Actions on Save`
   - 启用 "Run Spotless" 或 "Reformat code"

3. **Checkstyle插件**：
   - 安装插件：`File` -> `Settings` -> `Plugins` -> 搜索 "Checkstyle-IDEA"
   - 配置：`File` -> `Settings` -> `Tools` -> `Checkstyle`
   - 添加配置文件：选择项目根目录下的 `checkstyle.xml`
   - 启用实时检查

2. **PMD插件**：
   - 安装插件：`File` -> `Settings` -> `Plugins` -> 搜索 "PMD"
   - 配置：`File` -> `Settings` -> `Tools` -> `PMD`
   - 启用实时检查

3. **SpotBugs插件**：
   - 安装插件：`File` -> `Settings` -> `Plugins` -> 搜索 "SpotBugs"
   - 配置：`File` -> `Settings` -> `Tools` -> `SpotBugs`
   - 启用实时检查

#### Eclipse

1. **Google Java Format插件**（推荐）：
   - 安装：`Help` -> `Eclipse Marketplace` -> 搜索 "Google Java Format"
   - 配置：`Window` -> `Preferences` -> `Java` -> `Code Style` -> `Formatter`
   - 使用Google Java Format作为格式化器

2. **Checkstyle插件**：
   - 安装：`Help` -> `Eclipse Marketplace` -> 搜索 "Checkstyle"
   - 配置：`Window` -> `Preferences` -> `Checkstyle`

2. **PMD插件**：
   - 安装：`Help` -> `Eclipse Marketplace` -> 搜索 "PMD"
   - 配置：`Window` -> `Preferences` -> `PMD`

3. **SpotBugs插件**：
   - 安装：`Help` -> `Eclipse Marketplace` -> 搜索 "SpotBugs"
   - 配置：`Window` -> `Preferences` -> `SpotBugs`

## 配置说明

### 代码格式化配置

#### EditorConfig (`.editorconfig`)

项目根目录包含 `.editorconfig` 文件，定义了统一的代码风格：
- **缩进**：4个空格
- **行长度**：120字符
- **字符编码**：UTF-8
- **行尾**：LF
- **文件末尾**：换行符

大多数现代IDE都支持EditorConfig，会自动应用这些规则。

#### Spotless配置（Maven）

如果使用Maven格式化：
- **风格**：Google Java Code Style
- **功能**：
  - 自动格式化Java代码
  - 移除未使用的导入
  - 统一代码风格

**格式化规则**：
- 4空格缩进
- 行长度限制：120字符
- 括号风格：K&R风格
- 导入排序：按字母顺序

### Checkstyle配置 (`checkstyle.xml`)

主要检查规则：
- 代码风格（命名、缩进、空格等）
- 代码长度（方法不超过150行，文件不超过2000行）
- 导入检查（禁止使用 `*` 导入）
- 代码复杂度

### PMD配置

使用PMD内置规则集：
- Best Practices（最佳实践）
- Code Style（代码风格）
- Design（设计）
- Error Prone（易错代码）
- Performance（性能）
- Security（安全）

### SpotBugs配置 (`spotbugs-exclude.xml`)

已排除：
- Lombok生成的代码
- Spring Boot配置类
- DTO和实体类的字段未使用警告

### JaCoCo配置

代码覆盖率要求：
- 最低覆盖率：50%
- 报告位置：`target/site/jacoco/index.html`

## 常见问题修复

### Checkstyle错误

1. **方法过长**：
   - 将长方法拆分为多个小方法

2. **行长度超过120字符**：
   - 适当换行
   - 使用IDE的自动格式化功能

3. **缺少Javadoc**：
   - 为公共类和方法添加Javadoc注释

4. **命名不规范**：
   - 类名：大驼峰（PascalCase）
   - 方法名/变量名：小驼峰（camelCase）
   - 常量：全大写下划线分隔（UPPER_SNAKE_CASE）

### PMD错误

1. **未使用的变量**：
   - 删除未使用的变量
   - 或使用 `@SuppressWarnings("unused")` 注解

2. **空catch块**：
   - 至少添加日志记录
   - 或重新抛出异常

3. **过于复杂的类/方法**：
   - 重构代码，降低复杂度

### SpotBugs错误

1. **空指针风险**：
   - 添加空值检查
   - 使用Optional

2. **资源未关闭**：
   - 使用try-with-resources语句

3. **线程安全问题**：
   - 使用同步机制
   - 使用线程安全的集合类

## CI/CD集成

在CI/CD流水线中添加代码质量检查：

```yaml
# GitLab CI示例
code-quality:
  stage: test
  script:
    - mvn clean verify
  artifacts:
    reports:
      junit: target/surefire-reports/TEST-*.xml
    paths:
      - target/site/
    expire_in: 1 week
```

## 最佳实践

1. **提交前格式化**：
   - 在提交代码前运行 `mvn spotless:apply` 自动格式化代码
   - 确保代码风格统一

2. **提交前检查**：
   - 在提交代码前运行 `mvn clean verify`
   - 修复所有错误和警告

2. **代码审查**：
   - 查看代码质量报告
   - 关注高优先级的警告

3. **持续改进**：
   - 定期审查代码质量报告
   - 逐步提高代码覆盖率
   - 修复技术债务

4. **团队规范**：
   - 统一代码风格
   - 定期进行代码审查
   - 分享最佳实践

## 报告查看

所有报告生成在 `target/site/` 目录下：

- Checkstyle: `target/site/checkstyle.html`
- PMD: `target/site/pmd.html`
- SpotBugs: `target/site/spotbugs.html`
- JaCoCo: `target/site/jacoco/index.html`

使用浏览器打开这些HTML文件即可查看详细的检查报告。

## 注意事项

1. 首次运行可能需要下载依赖，请确保网络连接正常
2. 如果某些警告是预期的，可以在配置文件中排除
3. 代码覆盖率要求可以根据项目实际情况调整
4. 建议在IDE中安装相应插件，实现实时检查

## 参考资源

- [Spotless官方文档](https://github.com/diffplug/spotless)
- [Google Java Format](https://github.com/google/google-java-format)
- [Checkstyle官方文档](https://checkstyle.sourceforge.io/)
- [PMD官方文档](https://pmd.github.io/)
- [SpotBugs官方文档](https://spotbugs.github.io/)
- [JaCoCo官方文档](https://www.jacoco.org/jacoco/)
