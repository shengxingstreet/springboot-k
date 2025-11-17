package com.suke.wxk.controller;

import com.suke.wxk.common.Result;
import com.suke.wxk.entity.Product;
import com.suke.wxk.entity.VoteActivity;
import com.suke.wxk.service.ProductVoteService;
import com.suke.wxk.service.VoteActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vote")
public class VoteController {
    @Autowired
    private VoteActivityService activityService;
    @Autowired
    private ProductVoteService productService;

    // 创建投票活动
    @PostMapping("/activity")
    public Result createActivity(@RequestBody VoteActivity activity) {
        activityService.createActivity(activity);
        return Result.success("活动创建成功");
    }

    // 开始投票活动
    @PutMapping("/activity/start/{id}")
    public Result startActivity(@PathVariable Long id) {
        activityService.startActivity(id);
        return Result.success("活动已开始");
    }

    // 结束投票活动
    @PutMapping("/activity/end/{id}")
    public Result endActivity(@PathVariable Long id) {
        activityService.endActivity(id);
        return Result.success("活动已结束");
    }

    @PostMapping("/product")
    public Result<Product> createProduct(
            @RequestBody Product product) {
        Product newproduct = productService.createProduct(product);
        return Result.success(newproduct,"产品创建成功");
    }

    // 提交投票
    @PostMapping("/submit")
    public Result submitVote(
            @RequestParam Long activityId,
            @RequestParam Long userId,
            @RequestParam String productId,
            @RequestParam String vote) {
        productService.submitVote(activityId, userId, productId, vote);
        return Result.success("投票已提交（异步处理中）");
    }

    // 获取投票结果
    @GetMapping("/result/{productId}")
    public Result<Map<String, Integer>> getResult(@PathVariable String productId) {
        Map<String, Integer> result = productService.getVoteResult(productId);
        return Result.success(result, "获取投票结果成功");
    }
}