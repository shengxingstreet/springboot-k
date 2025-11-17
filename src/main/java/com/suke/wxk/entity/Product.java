package com.suke.wxk.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
//@NoArgsConstructor
@Document(collection = "product")
public class Product {
    @Id
    private String id; // MongoDB自动生成的ObjectId
    private String name; // 产品名称
    private org.bson.Document description; // 描述
    private String category; // 分类（如：耗材、设备）
    private Long activityId; // 关联的投票活动ID
    private List<VoteOption> options; // 投票选项（同意/不同意）
    private LocalDateTime createTime;
}