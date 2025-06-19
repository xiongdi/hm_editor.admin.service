# 惠每电子病历编辑器模板制作工具-后端

[![Java](https://img.shields.io/badge/Java-1.8-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.14-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-4.0+-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0.html)

## 📖 项目简介

HmEditor Admin Service是为HmEditor电子病历编辑器提供的后台管理端服务，主要实现简单的数据元管理和模板制作功能。基于Spring Boot微服务架构，为前端编辑器提供数据支撑和管理功能。

## 🏗️ 技术架构

### 后端技术栈
- **框架**: Spring Boot 2.6.14
- **数据库**: MongoDB
- **JSON处理**: FastJSON 1.2.83
- **HTML解析**: Jsoup 1.15.3

### 项目结构
```
/
├── admin-service/          # 管理服务模块
│   ├── src/main/java/     # Java源代码
│   ├── src/main/resources/ # 配置文件
│   └── pom.xml            # Maven配置
└── pom.xml                # 父级Maven配置
```

## 🚀 快速开始

### 环境要求
- Java 1.8+
- Maven 3.6+
- MongoDB 4.0+

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/huimeicloud/hm_editor.admin.service.git
cd hm_editor.admin.service
```

2. **配置数据库**
```yaml
# application.yml
spring:
  data:
    mongodb:
      host: localhost
      username: your_username
      password: your_password
      database: HmEditor
      port: 27017
```

3. **编译项目**
```bash
mvn clean compile
```

4. **运行服务**
```bash
cd admin-service
mvn spring-boot:run
```

5. **访问应用**
```
http://localhost:13071/hmEditor/admin-service
```

## 🐳 Docker部署

### 使用Docker Compose
```yaml
version: '3.8'
services:
  hm-editor:
    build: .
    ports:
      - "13071:13071"
    environment:
      - MONGODB_HOST=mongodb
      - MONGODB_USERNAME=HmEditor
      - MONGODB_PASSWORD=HmEditor2025
    depends_on:
      - mongodb

  mongodb:
    image: mongo:4.4
    environment:
      MONGO_INITDB_ROOT_USERNAME: HmEditor
      MONGO_INITDB_ROOT_PASSWORD: HmEditor2025
```

运行命令：
```bash
docker-compose up -d
```

## 🔧 配置说明

### 应用配置
```yaml
server:
  servlet:
    context-path: /hmEditor/admin-service
  port: 13071

spring:
  servlet:
    multipart:
      max-request-size: 100MB
      max-file-size: 100MB

logger:
  showParam: false
```



## 📄 许可证

本项目采用 GNU Lesser General Public License v2.1 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 🙏 致谢

感谢所有为本项目做出贡献的开发者和用户！

特别感谢以下开源项目：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MongoDB](https://www.mongodb.com/)
- [Apache POI](https://poi.apache.org/)
- [FastJSON](https://github.com/alibaba/fastjson)

---

⭐ 如果这个项目对您有帮助，请给我们一个星标！
