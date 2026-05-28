# 🧪 ANRAN LIMS 架构审查与升级方案

> 审查日期：2026-05-25  
> 审查对象：anran-lims-beta（中天环科 LIMS 系统）  
> 参考标准：《生态环境监测信息管理系统技术要求》征求意见稿  
> 版本：v1.0

---

## 一、项目现状总览

### 1.1 技术栈

| 层级 | 技术选型 | 版本 |
|------|---------|------|
| **后端** | Spring Boot | 3.1.5 |
| **Java** | OpenJDK | 17 |
| **ORM** | MyBatis-Plus | 3.5.7 |
| **数据库** | MySQL | 8.0 |
| **缓存** | Redis | 7.x |
| **认证** | JWT (auth0) | 4.4.0 |
| **前端** | Vue 3 + Element Plus | - |
| **图表** | ECharts | - |
| **构建** | Maven | - |
| **部署** | Docker Compose | - |

### 1.2 模块结构

```
anran-lims-beta/
├── anran-lims-boot/          # 启动模块 (Spring Boot 入口)
├── anran-lims-common/        # 公共模块 (工具类、异常、结果封装)
├── anran-lims-system/        # 系统管理 (用户、认证、权限)
├── anran-lims-sample/        # 样品管理 (样品登记、流转)
├── anran-lims-task/          # 任务管理 (任务分配、检测)
├── anran-lims-report/        # 报告管理 (报告生成、审核、签发)
├── anran-lims-instrument/    # 仪器管理 (设备台账、校准)
├── anran-lims-frontend/      # 前端 (Vue3 + Element Plus)
└── database/init.sql         # 数据库初始化脚本
```

### 1.3 已实现功能

| 模块 | 功能点 | 状态 |
|------|--------|------|
| **用户认证** | JWT 登录/登出、Token 验证 | ✅ 完成 |
| **样品管理** | CRUD、编码生成、状态流转 | ✅ 完成 |
| **任务管理** | 分配/认领/提交/审核/驳回 | ✅ 完成 |
| **报告管理** | 编制/提交/审核/签发 | ✅ 完成 |
| **仪器管理** | 台账、状态统计 | ✅ 完成 |
| **前端页面** | Dashboard、列表页、表单 | ✅ 完成 |
| **数据看板** | ECharts 图表展示 | ✅ 完成 |

---

## 二、五大维度对标审查

### 维度1：服务业务管理（全链条覆盖）

#### 标准要求
- 合同→采样→检测→报告 全生命周期管理
- 多级审批流（编制→审核→批准→签发）
- 样品条码/二维码追溯
- 电子签名

#### 现状分析

| 检查项 | 现状 | 差距 | 优先级 |
|--------|------|------|--------|
| 合同管理 | 有表结构，但功能简单 | ⚠️ 缺合同审批、到期提醒 | P1 |
| 样品编码 | 自动生成（20240330-W-001） | ✅ 已满足 | - |
| 样品条码 | 数据库有 qrcode 字段 | ⚠️ 未实现打印/扫描功能 | P2 |
| 多级审批 | 任务/报告有审核流 | ✅ 基础审批已实现 | - |
| 电子签名 | 无 | ❌ 完全缺失 | P1 |
| 样品留样 | 路由有，功能未实现 | ⚠️ 缺留样管理 | P2 |
| 样品流转 | 状态字段有（0-3） | ⚠️ 缺流转记录日志 | P2 |

#### 改进建议
1. **合同管理增强**：增加合同审批流、到期自动提醒
2. **电子签名**：引入数字签名/手写签名组件
3. **条码系统**：集成条码打印机和扫码枪
4. **流转日志**：增加样品流转历史记录表

---

### 维度2：资源管理（人、机、料、法、环）

#### 标准要求
- **人**：人员持证、培训记录、能力考核
- **机**：仪器检定/校准、维护记录、使用记录
- **料**：标准物质、试剂、耗材管理
- **法**：检测方法、标准文件管理
- **环**：实验室环境条件监控

#### 现状分析

| 资源 | 现状 | 差距 | 优先级 |
|------|------|------|--------|
| **人员** | 基础用户信息 | ❌ 缺持证/培训/考核 | P1 |
| **仪器** | 台账+校准日期 | ⚠️ 缺检定证书、维护记录、使用记录 | P1 |
| **标准物质** | 无 | ❌ 完全缺失 | P2 |
| **试剂耗材** | 无 | ❌ 完全缺失 | P3 |
| **检测方法** | 样品表有 test_standard | ⚠️ 缺方法库管理 | P2 |
| **环境监控** | 无 | ❌ 完全缺失 | P3 |

#### 改进建议
1. **人员管理增强**：
   - 新增 `sys_user_certificate` 证书表
   - 新增 `sys_user_training` 培训记录表
   - 新增 `sys_user_capability` 能力考核表

2. **仪器管理增强**：
   - 检定证书附件上传
   - 维护保养记录
   - 使用记录（谁、何时、测什么）

3. **新增标准物质模块**：
   - 标准物质台账
   - 期间核查记录
   - 有效期预警

---

### 维度3：数据采集与真值（杜绝人工干预）

#### 标准要求
- 仪器数据自动采集
- 原始数据不可篡改
- 数据修改留痕（谁、何时、改什么、为什么）

#### 现状分析

| 检查项 | 现状 | 差距 | 优先级 |
|--------|------|------|--------|
| 仪器接口 | 表中有 IP/Port/Protocol | ⚠️ 仅配置，未实现采集 | P1 |
| 数据自动采集 | 无 | ❌ 完全缺失 | P1 |
| 数据修改留痕 | 无 | ❌ 完全缺失 | P1 |
| 原始数据保护 | 无 | ❌ 完全缺失 | P1 |

#### 改进建议
1. **仪器数据采集**：
   - 开发 Modbus TCP/RS232 采集服务
   - 支持主流仪器（岛津、安捷伦、赛默飞等）
   - 数据自动入库，禁止人工修改原始值

2. **数据修改留痕**：
   - 新增 `data_change_log` 表
   - 任何修改必须记录：原值→新值、修改人、修改时间、修改原因
   - 原始数据表设为只读

3. **审计日志**：
   - 已在前端预留 AuditLogs 页面
   - 需完善后端审计日志记录

---

### 维度4：数据安全（分级、加密、备份、审计）

#### 标准要求
- 分级权限控制（字段级）
- 数据传输加密
- 数据备份机制
- 操作日志审计

#### 现状分析

| 检查项 | 现状 | 差距 | 优先级 |
|--------|------|------|--------|
| 权限控制 | 简单的角色区分（1-3） | ⚠️ 缺细粒度权限（菜单/按钮/字段） | P1 |
| 数据加密 | 密码 BCrypt 加密 | ⚠️ 传输未强制 HTTPS | P2 |
| 数据备份 | 无 | ❌ 完全缺失 | P1 |
| 操作审计 | 前端有页面，后端未实现 | ⚠️ 缺审计日志记录 | P1 |
| 数据脱敏 | 无 | ❌ 完全缺失 | P3 |

#### 改进建议
1. **权限系统重构**：
   - 引入 RBAC 模型（Role-Based Access Control）
   - 支持菜单权限、按钮权限、数据权限、字段权限

2. **数据备份**：
   - MySQL 定时备份（每日全量+增量）
   - 备份文件异地存储

3. **审计日志**：
   - 记录所有敏感操作（登录、修改、删除、导出）
   - 日志保留 3 年以上

---

### 维度5：系统安全与部署

#### 标准要求
- 等保合规
- 私有化部署
- 支持信创环境（国产数据库/中间件）

#### 现状分析

| 检查项 | 现状 | 差距 | 优先级 |
|--------|------|------|--------|
| 等保合规 | 未评估 | ❌ 需评估整改 | P2 |
| 私有化部署 | Docker Compose 支持 | ✅ 已满足 | - |
| 国产数据库 | MySQL | ⚠️ 可适配达梦/人大金仓 | P3 |
| 国产中间件 | Tomcat（内置） | ⚠️ 可适配东方通 | P3 |
| 密码复杂度 | 无限制 | ⚠️ 缺密码策略 | P2 |
| 登录安全 | 无限制 | ⚠️ 缺登录失败锁定 | P2 |

#### 改进建议
1. **安全加固**：
   - 密码复杂度策略（8位以上+大小写+数字+特殊字符）
   - 登录失败 5 次锁定 30 分钟
   - 会话超时（30 分钟无操作自动退出）

2. **等保整改**（如需要）：
   - 等保 2.0 三级要求评估
   - 漏洞扫描修复
   - 安全渗透测试

---

## 三、数据库设计优化建议

### 3.1 新增核心表

```sql
-- 1. 人员证书表
CREATE TABLE sys_user_certificate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    cert_type VARCHAR(50) COMMENT '证书类型（上岗证/内审员/授权签字人）',
    cert_no VARCHAR(100) COMMENT '证书编号',
    issue_date DATE COMMENT '发证日期',
    expire_date DATE COMMENT '到期日期',
    issue_org VARCHAR(100) COMMENT '发证机构',
    file_url VARCHAR(500) COMMENT '证书附件',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_expire_date (expire_date)
) ENGINE=InnoDB COMMENT='人员证书表';

-- 2. 仪器使用记录表
CREATE TABLE lims_instrument_usage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    instrument_id BIGINT NOT NULL COMMENT '仪器ID',
    user_id BIGINT COMMENT '使用人ID',
    user_name VARCHAR(50) COMMENT '使用人姓名',
    sample_id BIGINT COMMENT '检测样品ID',
    sample_code VARCHAR(50) COMMENT '样品编号',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    purpose VARCHAR(200) COMMENT '使用目的',
    status TINYINT COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_instrument_id (instrument_id),
    INDEX idx_user_id (user_id),
    INDEX idx_start_time (start_time)
) ENGINE=InnoDB COMMENT='仪器使用记录表';

-- 3. 数据修改日志表（关键！）
CREATE TABLE sys_data_change_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50) COMMENT '表名',
    record_id BIGINT COMMENT '记录ID',
    field_name VARCHAR(50) COMMENT '字段名',
    old_value TEXT COMMENT '原值',
    new_value TEXT COMMENT '新值',
    change_reason VARCHAR(500) COMMENT '修改原因',
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50) COMMENT 'IP地址',
    INDEX idx_table_record (table_name, record_id),
    INDEX idx_operate_time (operate_time)
) ENGINE=InnoDB COMMENT='数据修改日志表';

-- 4. 标准物质表
CREATE TABLE lims_standard_material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_code VARCHAR(50) UNIQUE COMMENT '标物编号',
    material_name VARCHAR(100) COMMENT '标物名称',
    batch_no VARCHAR(50) COMMENT '批号',
    manufacturer VARCHAR(100) COMMENT '生产厂家',
    cert_value DECIMAL(20,10) COMMENT '标准值',
    uncertainty DECIMAL(20,10) COMMENT '不确定度',
    unit VARCHAR(20) COMMENT '单位',
    purchase_date DATE COMMENT '购置日期',
    expire_date DATE COMMENT '有效期至',
    quantity DECIMAL(10,2) COMMENT '数量',
    storage_condition VARCHAR(100) COMMENT '保存条件',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_material_code (material_code),
    INDEX idx_expire_date (expire_date)
) ENGINE=InnoDB COMMENT='标准物质表';

-- 5. 样品流转记录表
CREATE TABLE lims_sample_flow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sample_id BIGINT NOT NULL COMMENT '样品ID',
    sample_code VARCHAR(50) COMMENT '样品编号',
    from_status TINYINT COMMENT '原状态',
    to_status TINYINT COMMENT '新状态',
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    remark TEXT COMMENT '备注',
    INDEX idx_sample_id (sample_id),
    INDEX idx_operate_time (operate_time)
) ENGINE=InnoDB COMMENT='样品流转记录表';
```

---

## 四、升级路线图

### Phase 1：安全与合规（2-3周）

**目标**：满足基础合规要求，数据安全可控

| 任务 | 工作量 | 负责人 |
|------|--------|--------|
| 数据修改日志系统 | 3天 | 后端 |
| 审计日志模块 | 2天 | 后端 |
| 权限系统重构（RBAC） | 5天 | 后端+前端 |
| 密码安全策略 | 1天 | 后端 |
| 数据备份脚本 | 1天 | 运维 |

### Phase 2：资源管理完善（3-4周）

**目标**：人、机、料、法、环全面管控

| 任务 | 工作量 | 负责人 |
|------|--------|--------|
| 人员证书/培训管理 | 3天 | 后端+前端 |
| 仪器使用记录 | 2天 | 后端+前端 |
| 仪器维护保养 | 2天 | 后端+前端 |
| 标准物质管理 | 3天 | 后端+前端 |
| 检测方法库 | 2天 | 后端+前端 |

### Phase 3：数据采集（4-6周）

**目标**：仪器数据自动采集，杜绝人工干预

| 任务 | 工作量 | 负责人 |
|------|--------|--------|
| 仪器通信协议调研 | 3天 | 技术 |
| 数据采集服务开发 | 10天 | 后端 |
| 仪器驱动开发（Modbus/RS232） | 5天 | 后端 |
| 数据自动入库 | 3天 | 后端 |
| 原始数据保护机制 | 2天 | 后端 |

### Phase 4：业务增强（3-4周）

**目标**：合同管理、电子签名、条码系统

| 任务 | 工作量 | 负责人 |
|------|--------|--------|
| 合同管理增强 | 3天 | 后端+前端 |
| 电子签名集成 | 5天 | 后端+前端 |
| 条码打印/扫描 | 3天 | 前端+硬件 |
| 到期提醒系统 | 2天 | 后端+QClaw |

---

## 五、与 Phase 1 轻量版的衔接方案

### 5.1 双轨并行策略

```
┌─────────────────────────────────────────────────────────────────┐
│                     中天环科 LIMS 双轨架构                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────┐      ┌─────────────────────────┐     │
│   │   Phase 1 轻量版     │      │   anran-lims-beta       │     │
│   │   (腾讯文档+QClaw)   │      │   (完整Java系统)        │     │
│   │                     │      │                         │     │
│   │   • 快速数据录入     │◄────►│   • 核心业务系统         │     │
│   │   • 简单查询统计     │      │   • 复杂业务逻辑         │     │
│   │   • 自动化提醒       │      │   • 数据安全保障         │     │
│   │   • 报告生成         │      │   • 仪器数据采集         │     │
│   └─────────────────────┘      └─────────────────────────┘     │
│              │                            │                     │
│              └────────────┬───────────────┘                     │
│                           ▼                                    │
│                  ┌─────────────────┐                           │
│                  │   统一数据层     │                           │
│                  │   (MySQL/SQLite) │                           │
│                  └─────────────────┘                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 5.2 数据同步方案

| 方向 | 方式 | 频率 |
|------|------|------|
| 腾讯文档 → Java系统 | API 同步 | 实时/定时 |
| Java系统 → 腾讯文档 | Webhook | 实时 |
| QClaw → Java系统 | 数据库直连 | 按需 |

### 5.3 渐进式迁移

1. **第1-2周**：轻量版上线，解决 immediate 痛点
2. **第3-4周**：Java系统 Phase 1 升级（安全+权限）
3. **第5-8周**：逐步将轻量版数据迁移到 Java系统
4. **第9周起**：轻量版作为补充入口保留，主系统全面接管

---

## 六、总结

### 6.1 核心差距

| 维度 | 当前完成度 | 目标完成度 | 差距 |
|------|-----------|-----------|------|
| 服务业务管理 | 70% | 100% | 电子签名、条码系统 |
| 资源管理 | 40% | 100% | 人员/标物/方法管理 |
| 数据采集 | 10% | 100% | 仪器自动采集（最大短板）|
| 数据安全 | 50% | 100% | 审计日志、细粒度权限 |
| 系统安全 | 60% | 100% | 等保、安全加固 |

### 6.2 优先级排序

**P0（立即）**：
- 数据修改日志（合规底线）
- 审计日志系统

**P1（本月）**：
- 权限系统重构
- 人员证书管理
- 仪器使用记录

**P2（下月）**：
- 仪器数据采集
- 标准物质管理
- 电子签名

**P3（后续）**：
- 环境监控
- 等保合规
- 信创适配

### 6.3 下一步行动

1. **确认优先级**：哪些功能是当前最紧迫的？
2. **资源评估**：开发人力、硬件设备（扫码枪、打印机）
3. **仪器调研**：现有仪器型号、通信接口协议
4. **启动开发**：按 Phase 1 开始实施

---

*报告生成时间：2026-05-25 14:25*  
*报告路径：`E:\OneDrive\Magic\WorkPlace\WorkClaw\ZTHKLIMS\zthk-lims\LIMS架构审查与升级方案.md`*
