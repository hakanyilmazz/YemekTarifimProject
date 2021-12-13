package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.RecipeContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RecipeContentRepository extends MongoRepository<RecipeContent, String> {
    @Query("{'recipeId': ?0}")
    void deleteByRecipeId(String recipeId);

    @Query("{'recipeId': ?0}")
    List<RecipeContent> findByRecipeId(String recipeId);
}
