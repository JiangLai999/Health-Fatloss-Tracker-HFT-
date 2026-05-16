# 健康减脂管理系统

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.14-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue.js-3.3-4FC08D.svg" alt="Vue.js">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1.svg" alt="MySQL">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
  <img src="https://img.shields.io/badge/JDK-1.8+-ED8B00.svg" alt="JDK">
  <img src="https://img.shields.io/badge/Node.js-16+-339933.svg" alt="Node.js">
</p>

<p align="center">
  基于 Spring Boot + Vue.js 的全栈健康减脂管理平台
</p>

---

## 项目简介

健康减脂管理系统是一个综合性的个人健康管理平台，旨在帮助用户科学地记录、分析和追踪自己的身体数据、饮食摄入与运动消耗。系统通过可视化的数据图表和智能的热量计算，为用户提供个性化的减脂建议和目标管理功能，助力用户实现健康的生活方式。

## 系统架构

```
┌─────────────────────────────────────────────────────────┐
│                      前端 (Vue.js)                       │
│  Vue 3 + Vue Router + Vuex + Element Plus + ECharts     │
├─────────────────────────────────────────────────────────┤
│                    RESTful API (HTTP)                    │
├─────────────────────────────────────────────────────────┤
│                     后端 (Spring Boot)                   │
│  Spring Security + JWT + MyBatis Plus                   │
├─────────────────────────────────────────────────────────┤
│                      数据库 (MySQL)                      │
└─────────────────────────────────────────────────────────┘
```

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.14 | 核心框架 |
| Spring Security | - | 安全框架 |
| JWT (jjwt) | - | 无状态身份认证 |
| MyBatis Plus | 3.5.3 | ORM 框架 |
| MySQL | 8.0 | 关系型数据库 |
| Maven | - | 项目构建工具 |
| Lombok | - | 简化 Java 代码 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue.js | 3.3 | 渐进式 JavaScript 框架 |
| Vue Router | 4 | 官方路由管理器 |
| Vuex | 4 | 状态管理库 |
| Element Plus | 2.3 | Vue 3 UI 组件库 |
| ECharts | 5.4 | 数据可视化图表库 |
| Axios | - | HTTP 客户端 |
| Vite | 4 | 前端构建工具 |
| dayjs | - | 轻量级日期处理库 |

## 功能模块

### 1. 用户管理

- **用户注册**：支持用户名、密码、昵称、邮箱等信息注册
- **用户登录**：基于 JWT 的无状态认证机制
- **个人信息管理**：维护昵称、身高、体重、年龄、性别、活动水平等基础信息
- **密码重置**：通过短信验证码重置密码

### 2. 身体数据管理

- **体重记录**：按日期记录体重变化
- **BMI 自动计算**：根据身高体重自动计算身体质量指数
- **多维度数据**：支持体脂率、肌肉量、水分含量等指标记录
- **趋势可视化**：通过折线图展示身体数据变化趋势
- **数据统计**：提供最大值、最小值、平均值等统计信息

### 3. 饮食管理

- **食物库**：内置常见食物的营养成分数据（热量、蛋白质、碳水、脂肪）
- **饮食记录**：支持单次记录多种食物，每种食物可独立设置摄入量
- **营养统计**：自动计算每日摄入的总热量及各类营养素
- **营养分布图**：环形图直观展示蛋白质/碳水/脂肪占比
- **日期查询**：按日期快速查询历史饮食记录

### 4. 运动管理

- **运动库**：内置多种运动类型及其单位时间消耗数据
- **运动记录**：记录运动类型、时长、消耗热量等信息
- **消耗统计**：按日/周/月统计运动消耗总量
- **详细信息**：显示运动分类（有氧/力量训练等）、强度等级
- **日期查询**：按日期查询历史运动记录

### 5. 数据总览（Dashboard）

- **今日热量概览**：展示摄入目标、实际摄入、运动消耗、热量缺口/盈余
- **摄入进度条**：可视化当前热量摄入进度
- **30天体重趋势图**：展示近一个月的体重变化曲线
- **营养分布图**：今日三大营养素摄入比例
- **7天热量平衡图**：柱状图展示近一周热量摄入与消耗对比
- **快捷查看**：快速浏览今日饮食和运动记录

### 6. 目标管理

- **目标创建**：设置减重/减脂目标，包含目标值、截止日期等
- **进度追踪**：实时计算目标完成进度百分比
- **状态管理**：支持进行中、已完成、已放弃三种状态
- **统计面板**：顶部实时展示各类目标数量统计

## 项目结构

```
health/
├── backend/                              # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/health/fatloss/
│   │   │   │   ├── FatLossApplication.java  # 启动类
│   │   │   │   ├── common/                  # 公共类（Result、异常处理等）
│   │   │   │   ├── config/                  # 配置类（Security、CORS、MyBatis等）
│   │   │   │   ├── controller/              # REST 控制器
│   │   │   │   ├── dto/                     # 数据传输对象
│   │   │   │   ├── entity/                  # 数据库实体类
│   │   │   │   ├── mapper/                  # MyBatis Mapper 接口
│   │   │   │   ├── service/                 # 业务逻辑接口
│   │   │   │   │   └── impl/                # 业务逻辑实现
│   │   │   │   └── utils/                   # 工具类
│   │   │   └── resources/
│   │   │       └── application.yml          # 应用配置文件
│   │   └── test/                            # 单元测试
│   └── pom.xml                              # Maven 依赖配置
├── frontend/                             # 前端项目
│   ├── src/
│   │   ├── views/                        # 页面组件
│   │   │   ├── Dashboard.vue             # 数据总览
│   │   │   ├── BodyData.vue              # 身体数据管理
│   │   │   ├── Diet.vue                  # 饮食管理
│   │   │   ├── Exercise.vue              # 运动管理
│   │   │   ├── Goal.vue                  # 目标管理
│   │   │   ├── Profile.vue               # 个人信息
│   │   │   ├── Login.vue                 # 登录页
│   │   │   ├── Register.vue              # 注册页
│   │   │   ├── ForgotPassword.vue        # 忘记密码
│   │   │   └── Layout.vue                # 主布局组件
│   │   ├── router/                       # 路由配置
│   │   ├── store/                        # Vuex 状态管理
│   │   ├── utils/                        # 工具函数
│   │   ├── assets/                       # 静态资源
│   │   ├── App.vue                       # 根组件
│   │   └── main.js                       # 入口文件
│   ├── index.html                        # HTML 模板
│   ├── vite.config.js                    # Vite 配置
│   └── package.json                      # 前端依赖
└── database/                             # 数据库脚本
    ├── health_fatloss.sql                # 数据库初始化脚本（表结构 + 基础数据）
    ├── test_data.sql                     # 测试数据脚本
    └── init.sql                          # 数据库创建脚本
```

## 快速开始

### 环境要求

| 环境 | 版本要求 |
|------|----------|
| JDK | 1.8+ |
| MySQL | 8.0+ |
| Node.js | 16+ |
| Maven | 3.6+ |

### 后端部署

#### 1. 创建数据库

```sql
CREATE DATABASE health_fatloss DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 2. 配置数据库连接

编辑 `backend/src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/health_fatloss?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: your_password
```

#### 3. 导入初始数据

```bash
mysql -u root -p health_fatloss < database/health_fatloss.sql
```

#### 4. 启动后端服务

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务默认启动在 `http://localhost:8080`

### 前端部署

#### 1. 安装依赖

```bash
cd frontend
npm install
```

#### 2. 启动开发服务器

```bash
npm run dev
```

前端服务默认启动在 `http://localhost:3000`

#### 3. 构建生产版本

```bash
npm run build
```

构建产物将输出到 `frontend/dist/` 目录。

## 默认账号

系统预置了一个测试账号，可用于快速体验：

| 字段 | 值 |
|------|-----|
| 用户名 | `admin` |
| 密码 | `123456` |

## API 接口文档

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/login` | 用户登录（返回 JWT Token） |
| GET | `/api/auth/info` | 获取当前用户信息 |
| PUT | `/api/auth/info` | 更新用户信息 |
| POST | `/api/auth/send-code` | 发送短信验证码 |
| POST | `/api/auth/reset-password` | 重置密码 |

### 身体数据接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/body` | 添加身体数据记录 |
| GET | `/api/body` | 查询身体数据列表 |
| GET | `/api/body/statistics` | 获取身体数据统计信息 |
| DELETE | `/api/body/{id}` | 删除指定身体数据记录 |

### 饮食管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/diet/food` | 查询食物列表 |
| POST | `/api/diet/food` | 添加新食物 |
| POST | `/api/diet/record` | 添加饮食记录 |
| GET | `/api/diet/record` | 查询饮食记录 |
| GET | `/api/diet/statistics` | 获取饮食营养统计 |
| DELETE | `/api/diet/record/{id}` | 删除饮食记录 |

### 运动管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/exercise/list` | 查询运动列表 |
| POST | `/api/exercise` | 添加新运动类型 |
| POST | `/api/exercise/record` | 添加运动记录 |
| GET | `/api/exercise/record` | 查询运动记录 |
| GET | `/api/exercise/statistics` | 获取运动消耗统计 |
| DELETE | `/api/exercise/record/{id}` | 删除运动记录 |

### 目标管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/goal` | 创建新目标 |
| GET | `/api/goal` | 查询目标列表 |
| GET | `/api/goal/{id}/progress` | 查询目标进度 |
| PUT | `/api/goal/{id}/status` | 更新目标状态 |
| DELETE | `/api/goal/{id}` | 删除目标 |

## 核心功能说明

### TDEE 热量计算

系统根据用户的个人信息自动计算每日总能量消耗（Total Daily Energy Expenditure）：

1. **基础代谢率（BMR）**：使用 Mifflin-St Jeor 公式计算
   - 男性：`BMR = 10 × 体重(kg) + 6.25 × 身高(cm) - 5 × 年龄 + 5`
   - 女性：`BMR = 10 × 体重(kg) + 6.25 × 身高(cm) - 5 × 年龄 - 161`

2. **活动系数**：根据用户选择的活动水平乘以相应系数
   - 久坐不动：× 1.2
   - 轻度活动：× 1.375
   - 中度活动：× 1.55
   - 高度活动：× 1.725
   - 非常高度活动：× 1.9

3. **目标调整**：根据减脂/增肌目标调整每日摄入建议

### 多食物饮食记录

饮食记录支持一次性添加多种食物：

```json
{
  "date": "2024-01-15",
  "items": [
    { "foodId": 1, "amount": 200 },
    { "foodId": 5, "amount": 150 },
    { "foodId": 12, "amount": 100 }
  ]
}
```

系统会自动计算每种食物的营养成分并按比例汇总。

### 数据可视化

前端使用 ECharts 实现多种图表：

- **折线图**：体重趋势、身体数据变化
- **柱状图**：热量平衡对比、运动消耗统计
- **环形图**：营养分布比例
- **进度条**：目标完成进度、热量摄入进度

## 安全设计

- **JWT 认证**：使用 JSON Web Token 进行无状态身份验证，Token 有效期可配置
- **密码加密**：用户密码使用 BCrypt 加密存储
- **接口鉴权**：通过 Spring Security 配置接口访问权限
- **CORS 配置**：支持跨域请求，可配置允许的域名
- **SQL 防护**：使用 MyBatis Plus 参数化查询防止 SQL 注入

## 注意事项

1. 确保 MySQL 数据库服务已启动并正确配置
2. 首次运行请导入数据库初始化脚本 `database/health_fatloss.sql`
3. 生产环境请务必修改 JWT 密钥和数据库密码
4. 建议使用 Nginx 反向代理并启用 HTTPS 协议
5. 短信验证码功能需要配置相应的短信服务 API

## 许可证

本项目采用 [MIT License](LICENSE) 开源协议。
