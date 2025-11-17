package com.suke.wxk.service;

import com.suke.wxk.entity.VoteActivity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wxk
 * @since 2025-10-14
 */
public interface VoteActivityService extends IService<VoteActivity> {
    public boolean checkActivityAvailable(Long activityId);
    public void endActivity(Long activityId);
    public void startActivity(Long activityId);
    void createActivity(VoteActivity activity); // 创建活动
}
