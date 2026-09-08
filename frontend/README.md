# 租住 Vue 前端

这是对原 `src/main/webapp/jsp` 的独立前端迁移。它保留了原有租客侧和后台管理侧的主要业务入口，但**没有修改旧 JSP 或 Java 后端**；两套页面可并存，避免在 API 尚未改造时破坏旧系统。

## 已覆盖的页面能力

- 租客侧：首页检索、房源列表/筛选、房源详情、收藏、预约看房、租房指南、意见反馈、登录/注册入口。
- 管理侧：数据概览、房源发布/编辑/审核/删除、预约、用户、资讯和反馈管理。
- 响应式布局：窄屏下筛选、房源卡片、详情和后台导航会折叠为单列。

## 启动

```bash
cd /home/gakki/projects/houserent/frontend
npm install
npm run dev
```

打开 `http://localhost:5173`。登录页可选择“以管理员身份体验管理台”。

默认 `VITE_API_MODE=mock`，使用浏览器本地演示数据，所以收藏、预约、发布和反馈在刷新前后可演示，但不会写入旧 MySQL。

## 下一阶段的 API 对接契约

前端的 `src/api/housing.js` 是唯一的数据访问边界。后端改为 JSON API 后，只替换它的实现即可；页面组件不应直接访问旧 Controller。建议逐个提供：

| 业务 | 建议接口 |
| --- | --- |
| 房源检索/详情 | `GET /api/v1/houses`、`GET /api/v1/houses/{id}` |
| 收藏 | `PUT/DELETE /api/v1/houses/{id}/favorite` |
| 预约 | `POST /api/v1/appointments`、`GET /api/v1/appointments` |
| 后台房源 | `POST/PATCH/DELETE /api/v1/admin/houses/{id}` |
| 反馈/资讯 | `/api/v1/feedbacks`、`/api/v1/news` |

Vite 已配置 `/api` 反向代理到 `http://localhost:8080`。真正接入时，需要再补 Spring Security/JWT、统一响应和 CORS/网关策略；不要让 Vue 去调用 JSP 页面。
# -
