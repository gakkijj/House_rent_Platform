# 租住平台

## Docker 启动

这是一个前后端分离的单仓库项目：

- `frontend/`：Vue 3 + Vite；生产环境由 Nginx 托管。
- `backend/`：Spring Boot REST API；只负责 `/api/v1/**` 与 `/uploads/**`。
- `infra/`：Docker/MySQL 初始化资源。

Docker Compose 运行三个服务：`web`（Nginx + Vue）、`app`（Spring Boot API）和 `mysql`。生产运行时不需要 Vite。

当前工作区已经准备好本机的 `.env` 和数据库初始化文件，日常启动只需：

```bash
docker compose up -d
```

访问 `http://localhost:9999`；MySQL 为 `localhost:3307`，可供 DataGrip 连接。后端调试端口为 `http://localhost:9998`。

首次在新电脑上配置时：

```bash
cp .env.example .env
# 编辑 .env，设置 HOUSERENT_MYSQL_ROOT_PASSWORD
# 把自己的逻辑导出文件放入 infra/mysql/init/houserent.sql
docker compose up --build -d
```

修改 Java 或 Vue 代码后，使用 `docker compose up --build -d` 重新构建镜像。

房源图片保存在宿主机的上传目录中，由 `.env` 里的 `UPLOADS_HOST_PATH` 映射进应用容器的 `/root/uploads`。换电脑时，除了导入 SQL，也需要备份并恢复该上传目录。

> `infra/mysql/init/*.sql` 只会在 MySQL volume 第一次创建时自动导入。若要重新导入，先执行 `docker compose down -v`（这会删除容器数据库数据），再重新启动。
