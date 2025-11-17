package com.suke.wxk.service.impl;

import com.suke.wxk.entity.Product;
import com.suke.wxk.entity.VoteOption;
import com.suke.wxk.repository.ProductRepository;
import com.suke.wxk.service.ProductService;
import com.suke.wxk.constant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * 新增产品（自动设置创建时间）
     */
    @Override
    public Product save(Product product) {
        if (product.getId() == null) { // 新增文档
            product.setCreateTime(LocalDateTime.now());
            // 初始化投票选项（如默认添加"同意"和"不同意"）
            if (product.getOptions() == null || product.getOptions().isEmpty()) {
                VoteOption agree = new VoteOption(CommonConstant.VOTE_ARGEE, 0);
                VoteOption disagree = new VoteOption(CommonConstant.VOTE_DISAGREE, 0);

                product.setOptions(List.of(agree, disagree));
            }
        }
        return productRepository.save(product);
    }

    /**
     * 根据ID查询产品
     */
    @Override
    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在：" + id));
    }

    /**
     * 根据活动ID查询关联产品
     */
    @Override
    public List<Product> findByActivityId(Long activityId) {
        return productRepository.findByActivityId(activityId);
    }

    /**
     * 查询所有产品（按创建时间倒序）
     */
    @Override
    public List<Product> findAll() {
        return productRepository.findByOrderByCreateTimeDesc();
    }

}
