请设计一个可视化流程编排与执行引擎（Visual Workflow Orchestration Engine），支持用户通过图形界面（拖拽节点、连线）定义业务流程，并在后端可靠执行。整体系统需满足生产级要求，具备高可用、可监控、可扩展特性。

要求如下：

1. 前端可视化部分

使用layout框架
支持节点类型：开始、结束、任务（HTTP/SQL/脚本/自定义）、条件分支（if-else）、并行网关、子流程
每个节点可配置参数（如 URL、超时、重试次数）
支持流程校验（如无孤立节点、无环依赖检查）
提供“预览执行路径”和“执行历史回放”功能
支持流程导出导出

2. 后端执行引擎部分

基于 Spring Boot 构建，遵循 SOLID 原则
支持：
DAG 依赖解析（拓扑排序）
串行/并行任务调度
任务超时（可配置）
失败重试 + 指数退避
状态持久化（到数据库，如 PostgreSQL）
断点续跑（从失败节点恢复）
提供 REST API：
POST /workflows（保存流程定义）
POST /executions（启动执行）
GET /executions/{id}/status（实时状态）
GET /executions/{id}/trace（执行轨迹，用于前端回放）
3. 数据模型

WorkflowDefinition：JSON 格式的流程图定义（兼容前端画布数据结构）
WorkflowExecution：执行实例，含状态（RUNNING/COMPLETED/FAILED）
TaskExecution：每个节点的执行记录（开始/结束时间、输入/输出、状态、错误信息）
4. 可观测性与运维

集成 Micrometer + Prometheus + Grafana（指标：执行成功率、平均耗时、重试分布）
所有操作记录结构化日志（含 traceId）
支持 Webhook 通知（执行完成/失败时回调外部系统）
5. 安全与扩展

节点执行前做输入校验与权限隔离（避免任意代码执行）
支持通过 SPI 或注解注册自定义任务类型（如 @TaskType("email")）
流程定义支持版本管理
6. 输出要求
   请提供：

系统整体架构图（可用 Mermaid 描述）
前端核心组件设计（如 WorkflowCanvas, NodeConfigPanel）
后端关键类图（如 WorkflowEngine, TaskExecutor, ExecutionTracker）
一个完整示例：用户拖拽“HTTP → 条件分支 → 并行任务 → 结束”，保存并执行，前端实时显示执行高亮
部署建议（Docker + Kubernetes，支持水平扩展）