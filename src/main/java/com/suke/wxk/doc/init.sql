-- 专家用户表
CREATE TABLE `expert` (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '专家ID',
                          `username` varchar(50) NOT NULL COMMENT '用户名',
                          `name` varchar(50) NOT NULL COMMENT '姓名',
                          `group_id` bigint NOT NULL COMMENT '所属专家组ID',
                          `status` tinyint DEFAULT 1 COMMENT '状态（1-正常，0-禁用）',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 专家组表
CREATE TABLE `expert_group` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '组ID',
                                `name` varchar(100) NOT NULL COMMENT '组名称（如：医疗耗材组、设备采购组）',
                                `member_count` int NOT NULL COMMENT '成员数量（固定10人）',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 投票活动表（支持模板配置）
CREATE TABLE `vote_activity` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '活动ID',
                                 `title` varchar(200) NOT NULL COMMENT '活动标题（如：2025Q3耗材采购投票）',
                                 `group_id` bigint NOT NULL COMMENT '参与专家组ID',
                                 `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0-未开始，1-进行中，2-已结束）',
                                 `start_time` datetime NOT NULL,
                                 `end_time` datetime NOT NULL,
                                 `template_config` json DEFAULT NULL COMMENT '投票模板配置（JSON格式）',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;