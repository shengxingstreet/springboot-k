package com.suke.wxk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Expert implements Serializable {

        private static final long serialVersionUID = 1L;

      /**
     * 专家ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

      /**
     * 用户名
     */
      private String username;

      /**
     * 姓名
     */
      private String name;

      /**
     * 所属专家组ID
     */
      private Long groupId;

      /**
     * 状态（1-正常，0-禁用）
     */
      private Byte status;
}
