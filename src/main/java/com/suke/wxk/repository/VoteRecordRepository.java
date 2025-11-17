package com.suke.wxk.repository;


import com.suke.wxk.entity.VoteRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VoteRecordRepository extends MongoRepository<VoteRecord, String> {
    // 统计用户对某个产品的投票记录（防止重复投票）
    Optional<VoteRecord> findByUserIdAndProductId(Long userId, String productId);
}
