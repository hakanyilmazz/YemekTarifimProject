package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    @Query("{'chattingId': ?0}")
    void deleteByChattingId(String chattingId);

    @Query("{'chattingId': ?0}")
    List<Message> findByChattingId(String chattingId);
}
