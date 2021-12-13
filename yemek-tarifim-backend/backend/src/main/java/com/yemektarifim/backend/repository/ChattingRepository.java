package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Chatting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChattingRepository extends MongoRepository<Chatting, String> {

}
