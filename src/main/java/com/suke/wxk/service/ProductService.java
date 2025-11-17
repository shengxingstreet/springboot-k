package com.suke.wxk.service;

import com.suke.wxk.entity.Product;

import java.util.List;

public interface ProductService {
    Product save(Product product);
    Product getById(String id);
    List<Product> findByActivityId(Long activityId);
    List<Product> findAll();
}
