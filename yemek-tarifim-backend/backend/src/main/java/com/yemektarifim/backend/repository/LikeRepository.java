package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LikeRepository extends MongoRepository<Like, String> {
    @Query("{'userId': ?0}")
    void deleteByLikedUserId(String userId);
}
