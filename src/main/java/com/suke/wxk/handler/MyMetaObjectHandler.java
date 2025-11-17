package com.suke.wxk.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    // 插入操作时的填充逻辑
    @Override
    public void insertFill(MetaObject metaObject) {
        // 1. 给 startTime 字段填充当前系统时间
        // 参数说明：metaObject（元数据对象）、字段名（startTime）、字段类型（LocalDateTime.class）、填充值（当前时间）
        this.strictInsertFill(
                metaObject,
                "startTime",
                LocalDateTime.class,
                LocalDateTime.now() // 获取当前系统时间
        );

        // 若有 updateTime，可同时填充（插入时也赋值当前时间）
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    // 更新操作时的填充逻辑（若不需要更新时间，可空实现）
    @Override
    public void updateFill(MetaObject metaObject) {
        // 示例：更新时自动填充 updateTime
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
