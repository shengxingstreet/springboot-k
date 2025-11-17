package com.suke.wxk.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.3.155:3306/x_springboot?useSSL=false&serverTimezone=UTC", "root", "root")
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("wxk")
                            .outputDir(System.getProperty("user.dir") + "/src/main/java")
                            .disableOpenDir();
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.suke.wxk")
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    System.getProperty("user.dir") + "/src/main/resources/mapper/wxk"));
                })
                // 策略配置（通过 strategy 控制 Service 命名）
                .strategyConfig(builder -> {
                    builder.addInclude("expert", "expert_group", "vote_activity")
                            .entityBuilder()
                            .enableLombok()
                            .controllerBuilder()
                            .enableRestStyle()
                            // 控制 Service 命名为 %sService
                            .serviceBuilder()
                            .formatServiceFileName("%sService");
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}