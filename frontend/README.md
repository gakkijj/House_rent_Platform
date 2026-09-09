# 租住平台前端

Vue 3 + Vite 单页应用。页面组件仅通过 `/api/v1/**` 与 Spring Boot 通信，不依赖 JSP 或后端模板。

## 本地开发

先在仓库根目录启动后端与数据库：

```bash
docker compose up -d mysql app
```

再启动 Vite：

```bash
npm ci
npm run dev
```

访问 `http://localhost:5173`。Vite 会将 `/api` 和 `/uploads` 转发到本地调试 API `http://localhost:9998`。

## 生产运行

根目录执行 `docker compose up -d --build`。Vue 构建产物由 Nginx 提供，Nginx 负责：

- `/**`：Vue Router history 模式回退到 `index.html`；
- `/api/**`：代理到 Spring Boot；
- `/uploads/**`：代理到 Spring Boot 的持久化上传目录。
