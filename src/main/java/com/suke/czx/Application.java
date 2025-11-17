package com.suke.czx;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.suke.wxk.repository")
@MapperScan("com.suke.wxk.mapper")
@ComponentScan(basePackages = {"com.suke.czx", "com.suke.wxk"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("==================X-SpringBoot启动成功================");
    }
}