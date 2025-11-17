package com.suke.wxk.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wxk
 * @since 2025-10-14
 */
@Getter
@Setter
@ToString
@TableName("vote_activity")
public class VoteActivity implements Serializable {

        private static final long serialVersionUID = 1L;

      /**
     * 活动ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

      /**
     * 活动标题（如：2025Q3耗材采购投票）
     */
      private String title;

      /**
     * 参与专家组ID
     */
      private Long groupId;

      /**
     * 状态（0-未开始，1-进行中，2-已结束）
     */
      private Byte status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

      /**
     * 投票模板配置（JSON格式）
     */
      private String templateConfig;
}
