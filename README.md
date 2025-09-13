# EchoNote - 思维回响平台

<p align="center">
  <img src="EhcoNote-Electron/src/assets/logo.png" alt="EchoNote Logo" width="120"/>
</p>

<p align="center">
  <strong>思维回响，灵感永存</strong>
</p>

<p align="center">
  <a href="#技术栈">技术栈</a> •
  <a href="#功能特性">功能特性</a> •
  <a href="#快速部署">快速部署</a> •
  <a href="#项目结构">项目结构</a> •
  <a href="#API文档">API文档</a>
</p>

## 🎯 项目简介

EchoNote 是一个专注于思维记录与表达的创新平台，致力于让每一个灵感都能找到属于它的回响。通过四大核心功能模块，为用户提供完整的思维记录、整理、发酵和回顾的闭环体验。

### 核心理念
- **思维回响**：让每一次思考都能产生持久的价值
- **灵感永存**：捕捉稍纵即逝的灵感瞬间
- **认知迭代**：通过深度思考实现思维的持续进化

## 🛠️ 技术栈

### 前端技术栈
- **框架**: Vue.js 3.2+ (Composition API)
- **桌面应用**: Electron 13.0+
- **UI组件库**: Element Plus 2.10+
- **路由管理**: Vue Router 4.0+
- **状态管理**: Vue 3 Composition API
- **构建工具**: Vue CLI 5.0+
- **代码规范**: ESLint + Prettier

### 后端技术栈
- **框架**: Spring Boot 3.5.4
- **安全框架**: Spring Security + JWT
- **数据库**: MySQL 8.0+
- **ORM框架**: MyBatis 3.0+
- **数据格式**: JSON RESTful API
- **构建工具**: Maven
- **Java版本**: Java 17

### 部署架构
- **容器化**: Docker + Docker Compose
- **反向代理**: Nginx
- **数据库容器**: MySQL 8.0.33
- **网络**: 自定义Docker网络

## ✨ 功能特性

### 四大核心模块

#### 1. 🌟 灵光胶囊 (Lightbulb)
**即时灵感捕捉系统**
- 快速记录突发的想法和灵感
- 支持位置标记和情境描述
- 情绪标签关联系统（42种情绪标签）
- 状态追踪（未处理/已处理）

#### 2. ✍️ 心流写作 (Flow Writing)
**沉浸式写作体验**
- 版本化文档管理
- 分类整理系统
- 自动保存和版本备份
- 专注模式写作界面

#### 3. 🧠 思维慢发酵 (Slow Fermentation)
**深度思考培养系统**
- 24小时延迟回看机制
- AI挑衅式提问生成
- 思维发展轨迹追踪
- 多角度思考框架

#### 4. 🌀 思维迷宫 (Maze of Thought)
**复杂思维整理工具**
- 思维导图式整理
- 关联性分析
- 思维路径可视化
- 复杂问题分解

### 用户系统特性
- **安全认证**: JWT Token + Spring Security
- **权限管理**: 基于角色的访问控制
- **用户配置**: 个性化主题设置（深色/浅色模式）
- **数据安全**: 密码BCrypt加密存储

## 🚀 快速部署

### 环境要求
- Docker 20.10+
- Docker Compose 2.0+
- Node.js 16+ (开发环境)
- Java 17+ (开发环境)

### 一键部署

#### 1. 克隆项目
```bash
git clone <repository-url>
cd EchoNote
```

#### 2. Docker部署（推荐）
```bash
# 进入docker目录
cd docker

# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps
```

服务启动后：
- **前端应用**: http://localhost:80
- **后端API**: http://localhost:8080
- **数据库**: localhost:3306

#### 3. 开发环境部署

##### 后端启动
```bash
# 进入Java项目目录
cd EchoNote-JAVA

# 安装依赖
mvn clean install

# 启动应用
mvn spring-boot:run
```

##### 前端启动
```bash
# 进入前端项目目录
cd EhcoNote-Electron

# 安装依赖
npm install

# 开发模式启动
npm run electron:serve

# 或者Web模式
npm run serve
```

### 配置说明

#### 数据库配置
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/echonote
    username: root
    password: 1353914278
```

#### Docker环境变量
```yaml
# docker-compose.yml
environment:
  - SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/echonote
  - SPRING_DATASOURCE_USERNAME=root
  - SPRING_DATASOURCE_PASSWORD=1353914278
```

## 📁 项目结构

```
EchoNote/
├── EchoNote-JAVA/                 # Java后端项目
│   ├── src/main/java/
│   │   └── uno/zhuchen/echonotejava/
│   │       ├── config/           # 配置类
│   │       ├── controller/       # 控制器
│   │       ├── service/          # 业务逻辑层
│   │       ├── mapper/           # MyBatis映射
│   │       └── model/          # 数据模型
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/             # MyBatis XML映射文件
│   └── pom.xml                 # Maven配置
│
├── EhcoNote-Electron/            # 前端项目
│   ├── src/
│   │   ├── components/          # 公共组件
│   │   ├── views/             # 页面组件
│   │   ├── router/            # 路由配置
│   │   ├── assets/            # 静态资源
│   │   └── utils/             # 工具函数
│   ├── package.json           # 前端依赖
│   └── vue.config.js          # Vue配置
│
├── docker/                     # Docker部署配置
│   ├── docker-compose.yml     # 服务编排
│   ├── Dockerfile.backend     # 后端镜像
│   ├── nginx.conf             # Nginx配置
│   ├── EchoNote.sql          # 数据库初始化
│   └── dist/                 # 前端构建产物
│
└── README.md                  # 项目文档
```

## 🔌 API文档

### 认证相关
- **POST** `/api/login` - 用户登录
- **POST** `/api/register` - 用户注册
- **POST** `/api/logout` - 用户注销

### 灵光胶囊API
- **GET** `/api/capsules` - 获取用户的灵光胶囊列表
- **POST** `/api/capsules` - 创建新的灵光胶囊
- **PUT** `/api/capsules/{id}` - 更新灵光胶囊
- **DELETE** `/api/capsules/{id}` - 删除灵光胶囊

### 心流写作API
- **GET** `/api/categories` - 获取分类列表
- **POST** `/api/texts` - 创建新文档
- **GET** `/api/texts/{id}` - 获取文档详情
- **PUT** `/api/texts/{id}` - 更新文档
- **GET** `/api/texts/{id}/versions` - 获取文档版本历史

### 数据模型

#### 用户模型
```json
{
  "id": "integer",
  "userName": "string",
  "name": "string",
  "email": "string",
  "image": "string",
  "enabled": "boolean"
}
```

#### 灵光胶囊模型
```json
{
  "id": "bigint",
  "userId": "bigint",
  "title": "string",
  "location": "string",
  "description": "string",
  "status": "boolean",
  "createTime": "timestamp",
  "moods": ["string"]
}
```

#### 文档模型
```json
{
  "id": "bigint",
  "categoryId": "int",
  "title": "string",
  "content": "text",
  "currentVersion": "int",
  "createTime": "timestamp",
  "updateTime": "timestamp"
}
```

## 🔧 开发指南

### 代码规范
- **前端**: ESLint + Prettier，遵循Vue 3风格指南
- **后端**: 遵循Spring Boot最佳实践，使用Lombok简化代码
- **数据库**: 使用MyBatis注解和XML混合配置

### 分支管理
- `main` - 生产环境分支
- `develop` - 开发环境分支
- `feature/*` - 功能开发分支
- `hotfix/*` - 紧急修复分支

### 贡献指南
1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📋 环境变量

### 开发环境
```bash
# 后端
MYSQL_URL=jdbc:mysql://localhost:3306/echonote
MYSQL_USERNAME=root
MYSQL_PASSWORD=1353914278
JWT_SECRET=your-jwt-secret-key

# 前端
VUE_APP_API_BASE_URL=http://localhost:8080
```

### 生产环境
```bash
# Docker环境已在docker-compose.yml中配置
# 如需修改，请更新对应的环境变量
```

## 🐛 常见问题

### Q: 数据库连接失败
A: 确保MySQL服务已启动，并检查application.yml中的连接配置

### Q: 前端无法访问后端API
A: 检查跨域配置和网络连接，确保后端服务正常运行

### Q: Docker容器启动失败
A: 检查端口占用情况，确保80、8080、3306端口未被占用

## 📞 支持与联系

- **项目维护**: EchoNote Team
- **问题反馈**: [提交Issue](https://github.com/your-repo/issues)
- **邮箱联系**: 3029489598@qq.com

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

---

<p align="center">
  <strong>让每一次思考都有回响，让每一个灵感都被珍藏</strong><br>
  Made with ❤️ by EchoNote Team
</p>