package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Score;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ScoreRepository extends MongoRepository<Score, String> {
    @Query("{'userId': ?0}")
    void deleteByUserId(String userId);

    @Query("{'userId': ?0}")
    Score findByUserId(String userId);
}
