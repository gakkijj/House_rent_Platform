package com.javaclimb.houserent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
//@MapperScan("com.example.ssm.rental.mapper")
public class Application {
    public static void main(String[] args) {
        // ApplicationContext表示Spring容器
        ApplicationContext context = SpringApplication.run(Application.class, args);
        String serverPort = context.getEnvironment().getProperty("server.port");
        log.info("Verio started at http://localhost:" + serverPort);
    }
}
