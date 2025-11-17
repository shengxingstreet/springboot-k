package com.suke.wxk.repository;

import com.suke.wxk.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    // 1. 按方法名自动生成查询（根据活动ID查询产品）
    List<Product> findByActivityId(Long activityId);

    // 2. 按分类查询（支持模糊匹配）
    List<Product> findByCategoryContaining(String category);

    // 3. 自定义 MongoDB 查询（使用 JSON 格式条件）
    // 查询指定活动中，分类为"设备"的产品
    @Query("{ 'activityId' : ?0, 'category' : '设备' }")
    List<Product> findDevicesByActivityId(Long activityId);

    // 4. 排序查询（按创建时间降序）
    List<Product> findByOrderByCreateTimeDesc();

}
