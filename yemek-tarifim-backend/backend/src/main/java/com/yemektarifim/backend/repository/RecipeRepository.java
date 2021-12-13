package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    @Query("{'userId': ?0}")
    void deleteByUserId(String userId);

    @Query("{'userId': ?0}")
    List<Recipe> findByUserId(String userId);
}
