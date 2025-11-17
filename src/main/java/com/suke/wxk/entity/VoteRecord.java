package com.suke.wxk.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class VoteRecord implements Serializable {
    @Id
    private String id;
    private Long userId; // 专家ID（关联MySQL的expert.id）
    private String productId; // 产品ID（MongoDB的product.id）
    private Long activityId; // 活动ID
    private String vote; // "同意"或"不同意"
    private LocalDateTime createdAt;
}
