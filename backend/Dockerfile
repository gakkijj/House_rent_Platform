FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:8-jre-jammy
WORKDIR /app
ENV TZ=Asia/Shanghai
EXPOSE 9999
COPY --from=build /workspace/target/houserent.jar ./houserent.jar
ENTRYPOINT ["java", "-jar", "/app/houserent.jar"]
