package com.suke.wxk.service.impl;

import com.suke.wxk.entity.VoteActivity;
import com.suke.wxk.mapper.VoteActivityMapper;
import com.suke.wxk.service.VoteActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lettuce.core.StrAlgoArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import  com.suke.wxk.constant.CommonConstant;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wxk
 * @since 2025-10-14
 */
@Service
public class VoteActivityServiceImpl extends ServiceImpl<VoteActivityMapper, VoteActivity> implements VoteActivityService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${spring.data.redis.prefix.activity}")
    private String activityPrefix;

    @Autowired
    private VoteActivityMapper activityMapper;
    // 实现你的自定义方法
    public void createActivity(VoteActivity activity) {
        // 直接使用父类的 save 方法（等价于 insert）
        this.save(activity);
        redisTemplate.opsForValue().set(activityPrefix + activity.getId(), "0");
    }

    public void startActivity(Long activityId) {
        VoteActivity activity = this.getById(activityId); // 父类提供的 getById 方法
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        //进行中
        Byte status = CommonConstant.VOTE_STATUS_IN_PROGRESS;
        activity.setStatus(status);
        this.updateById(activity); // 父类提供的 update 方法
        redisTemplate.opsForValue().set(activityPrefix + activityId, "1");
    }
    // 结束投票活动（更新状态为“已结束”）
    public void endActivity(Long activityId) {
        VoteActivity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        Byte status = CommonConstant.VOTE_STATUS_ENDED;
        activity.setStatus(status);
        activityMapper.updateById(activity);
        redisTemplate.opsForValue().set(activityPrefix + activityId, "2");
    }

    // 检查活动是否可投票（从缓存获取状态）
    public boolean checkActivityAvailable(Long activityId) {
        String status = redisTemplate.opsForValue().get(activityPrefix + activityId);
        return "1".equals(status); // 状态为1（进行中）则可投票
    }

}
