package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CommentRepository extends MongoRepository<Comment, String> {
    @Query("{'userId': ?0}")
    void deleteByCommentedUserId(String userId);
}
