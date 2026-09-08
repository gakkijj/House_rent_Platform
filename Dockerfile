# 构建阶段：frontend-maven-plugin 会构建 Vue 并复制到 Spring Boot 静态资源目录。
FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /workspace
COPY . .
RUN mvn -DskipTests package

# 运行基础镜像：Vue 静态资源已经包含在可执行 jar 内，不需要单独启动 Vite。
FROM eclipse-temurin:8-jre-jammy AS runtime-base
WORKDIR /app
ENV TZ=Asia/Shanghai
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "/app/houserent.jar"]

# 本地验证可复用已构建 jar；默认构建仍使用下方完整的 build 阶段。
FROM runtime-base AS prebuilt
COPY target/houserent.jar /app/houserent.jar

FROM runtime-base AS runtime
COPY --from=build /workspace/target/houserent.jar /app/houserent.jar
