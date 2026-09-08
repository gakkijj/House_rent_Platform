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

默认使用同源的 Spring Boot API，收藏、租赁订单、发布房源和反馈都会写入旧 MySQL。仅在 `.env.local` 中显式设置 `VITE_API_MODE=mock` 时，才会启用浏览器演示数据。

## 下一阶段的 API 对接契约

前端的 `src/api/housing.js` 是唯一的数据访问边界。当前 REST API 已由 Spring Boot 在 `/api/v1` 提供；页面组件不直接访问 Controller 或 JSP。

| 业务 | 建议接口 |
| --- | --- |
| 登录/注册 | `POST /api/v1/auth/login`、`/register`、`/logout`、`GET /me` |
| 房源检索/详情 | `GET /api/v1/houses`、`GET /api/v1/houses/{id}` |
| 收藏 | `PUT/DELETE /api/v1/houses/{id}/favorite` |
| 租赁订单 | `POST /api/v1/orders`、合同确认与模拟支付接口 |
| 后台房源 | `POST/PATCH/DELETE /api/v1/admin/houses/{id}` |
| 反馈/资讯 | `/api/v1/feedbacks`、`/api/v1/news` |

`npm run build` 会直接生成到 `../src/main/resources/static`；Spring Boot 从这里提供 Vue 的 `index.html` 和静态资源，Vue Router 刷新时由 `VueSpaController` 回退到入口页。Vite 开发服务器也配置了 `/api` 反向代理到 `http://localhost:9999`。

旧 JSP 源文件仍保留在 `src/main/webapp/jsp`，仅作为迁移备份，已不再被 Maven 打包或运行时解析。
# -
