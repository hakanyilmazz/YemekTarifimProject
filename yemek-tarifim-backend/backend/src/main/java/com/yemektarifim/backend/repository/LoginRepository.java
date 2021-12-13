package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Login;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface LoginRepository extends MongoRepository<Login, String> {
    @Query("{'username': ?0}")
    Optional<Login> findByUsername(String username);

    @Query("{'username': ?0}")
    Boolean existsByUsername(String username);

    @Query("{'email': ?0}")
    Boolean existsByEmail(String email);
}
