package com.suke.wxk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

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
@TableName("expert_group")
public class ExpertGroup implements Serializable {

        private static final long serialVersionUID = 1L;

      /**
     * 组ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

      /**
     * 组名称（如：医疗耗材组、设备采购组）
     */
      private String name;

      /**
     * 成员数量（固定10人）
     */
      private Integer memberCount;
}
