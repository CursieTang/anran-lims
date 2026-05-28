# AGENTS.md - MLIMS 工作区

## 进入工作区先读什么
1. 先读 `LIMS架构审查与升级方案.md`
2. 如果存在 `SOUL.md`，先读它
3. 如果存在 `USER.md`，先读它
4. 如果存在 `memory/YYYY-MM-DD.md`，读今天和昨天
5. 如果存在 `MEMORY.md`，只在主会话中读取

## 工作原则
- 先理解现状，再动手改代码。
- 所有改动围绕 LIMS 升级路线图推进：`P0` 合规底座、`P1` 核心资源与权限、`P2` 数据采集与追溯、`P3` 增强能力。
- 不要为了完成单个页面或单个接口而牺牲审计、追溯、权限和数据安全。
- 任何需要长期记忆的决策写入 `memory/YYYY-MM-DD.md`；重要共识再摘要到 `MEMORY.md`。

## 团队协作策略
- 这个工作区按 30 人左右团队来设计，优先采用“模块化单体 + 清晰边界 + 少共享”的协作方式。
- 每个模块要有明确 owner，避免多人同时改同一套公共代码。
- `common` 只放真正通用的内容，不要把业务逻辑继续往里堆。
- 新功能默认按业务域切分，不要先做大而全的跨域抽象。

## 目录结构约定
- 后端主模块统一使用 `anran-lims-*` 命名。
- 推荐结构保持为：
  - `anran-lims-boot`
  - `anran-lims-common`
  - `anran-lims-system`
  - `anran-lims-sample`
  - `anran-lims-task`
  - `anran-lims-report`
  - `anran-lims-instrument`
  - `anran-lims-frontend`
  - `database/`
- 每个业务模块内部优先按功能分层：
  - `controller`
  - `service`
  - `service/impl`
  - `mapper`
  - `entity`
  - `dto`
  - `vo`
  - `converter`
  - `enums`
  - `constant`
- 前端按业务域分目录：
  - `src/views/<domain>`
  - `src/api/<domain>`
  - `src/components/common`
  - `src/layout`
  - `src/router`
  - `src/store`
  - `src/utils`
- 新增页面或接口时，前后端目录名尽量保持同域同名，减少跨模块查找成本。

## 后端 / 前端分层规范
- 后端分层建议：
  - `controller` 只处理入参校验、鉴权、返回值包装，不写复杂业务。
  - `service` 负责业务编排、事务边界、状态流转。
  - `mapper` 只负责数据持久化，不拼业务规则。
  - `entity` 只承载持久化字段，不混入页面字段。
  - `dto` 用于入参和内部传输，`vo` 用于返回展示。
  - 跨模块调用尽量通过服务接口或明确的应用层入口，不直接跨模块操作数据库。
- 前端分层建议：
  - 页面放 `views`，可复用组件放 `components`，接口放 `api`。
  - 页面组件只做组合和展示，状态管理放 `store`，公共工具放 `utils`。
  - 页面路径、API 名称、后端模块名保持同一业务域命名。
  - 复杂表单拆成基础表单 + 子组件，不把所有逻辑塞进单页。

## 数据库迁移和命名规范
- 迁移脚本优先采用版本化方式管理，建议目录：
  - `database/migration/VYYYYMMDDHHMM__description.sql`
  - `database/repeatable/R__description.sql`
- 已执行过的迁移脚本禁止直接修改，新增修复脚本，不回改历史文件。
- 初始化脚本只负责全量初始化，不承担长期演进。
- 表名建议保持业务前缀一致，优先使用 `sys_`、`lims_` 这类语义化前缀；品牌前缀统一体现在项目名、模块名、仓库名和文档中，避免把品牌名扩散到每一张表。
- 字段命名统一使用 `snake_case`，时间字段统一用 `create_time`、`update_time`、`delete_time` 这类风格。
- 主键统一用 `id`，外键字段统一用 `<table>_id` 或 `<entity>_id`。
- 枚举字段统一用整型或短码，避免把业务状态直接写成自由文本。

## LIMS 方向约束
- `P0` 优先：数据修改日志、审计日志、备份、密码策略、登录锁定。
- `P1` 优先：RBAC、人员证书/培训、仪器使用和维护记录。
- `P2` 优先：标准物质、方法库、样品流转日志、条码/二维码。
- `P3` 视资源推进：仪器自动采集、电子签名、环境监控、信创适配。

## 提交前检查
- 数据结构是否有迁移脚本或初始化脚本更新。
- 后端接口是否补了必要的权限校验与审计。
- 前端页面是否和后端接口字段一致。
- 新增功能是否有最基本的验证或测试说明。
