package com.suke.wxk.service;

import com.suke.wxk.config.RabbitConfig;
import com.suke.wxk.entity.Product;
import com.suke.wxk.entity.VoteOption;
import com.suke.wxk.entity.VoteRecord;
import com.suke.wxk.repository.ProductRepository;
import com.suke.wxk.repository.VoteRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import com.suke.wxk.constant.*;

@Service
public class ProductVoteService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VoteRecordRepository voteRecordRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private VoteActivityService activityService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${spring.data.redis.prefix.result}")
    private String resultPrefix;


    public Product createProduct(Product product) {

        // 初始化“同意”“不同意”选项，票数0
        List<VoteOption> options = new ArrayList<>();
        options.add(new VoteOption(CommonConstant.VOTE_ARGEE, 0));
        options.add(new VoteOption(CommonConstant.VOTE_DISAGREE, 0));
        product.setOptions(options);
        return productRepository.save(product);
    }

    // 提交投票（同步校验，异步记录）
    public void submitVote(Long activityId, Long userId, String productId, String vote) {
        // 1. 校验活动状态（从Redis缓存检查）
        if (!activityService.checkActivityAvailable(activityId)) {
            throw new RuntimeException("活动未开始或已结束");
        }

        // 2. 发送投票记录到RabbitMQ（异步处理）
        VoteRecord record = new VoteRecord();
        record.setUserId(userId);
        record.setProductId(productId);
        record.setVote(vote);
        record.setCreatedAt(LocalDateTime.now());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(
                RabbitConfig.VOTE_EXCHANGE,
                RabbitConfig.VOTE_RECORD_ROUTING_KEY,
                record
        );
    }

    // 异步处理投票记录（RabbitMQ消费者）
    @RabbitListener(queues = RabbitConfig.VOTE_RECORD_QUEUE)
    public void handleVoteRecord(VoteRecord record) {
        // 1. 校验是否重复投票（查询MongoDB）
        Optional<VoteRecord> existing = voteRecordRepository
                .findByUserIdAndProductId(record.getUserId(), record.getProductId());
        if (existing.isPresent()) {
            throw new RuntimeException("不可重复投票");
        }

        // 2. 保存投票记录到MongoDB
        voteRecordRepository.save(record);

        // 3. 更新产品票数（MongoDB）并同步到Redis缓存
        Product product = productRepository.findById(record.getProductId())
                .orElseThrow(() -> new RuntimeException("产品不存在"));
        product.getOptions().forEach(option -> {
            if (option.getName().equals(record.getVote())) {
                option.setVotes(option.getVotes() + 1);
            }
        });
        productRepository.save(product);

        // 4. 更新Redis缓存的投票结果（用于快速查询）
        redisTemplate.opsForHash().increment(
                resultPrefix + record.getProductId(),
                record.getVote(),
                1
        );
    }

    // 获取投票结果（优先从Redis缓存查询）
    public Map<String, Integer> getVoteResult(String productId) {
        // 1. 从Redis查询
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String agree = hashOps.get(resultPrefix + productId, "同意");
        String disagree = hashOps.get(resultPrefix + productId, "不同意");

        // 2. 若Redis无数据，从MongoDB查询并同步到Redis
        if (agree == null || disagree == null) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("产品不存在"));
            for (VoteOption option : product.getOptions()) {
                hashOps.put(resultPrefix + productId, option.getName(), option.getVotes().toString());
            }
            agree = hashOps.get(resultPrefix + productId, "同意");
            disagree = hashOps.get(resultPrefix + productId, "不同意");
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("同意", Integer.parseInt(agree));
        result.put("不同意", Integer.parseInt(disagree));
        return result;
    }
}
