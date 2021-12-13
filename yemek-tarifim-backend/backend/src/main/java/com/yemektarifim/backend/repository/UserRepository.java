package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{'loginId': ?0}")
    Optional<User> findByLoginId(String loginId);

    @Query("{'countryId': ?0}")
    Optional<Set<User>> findByCountryId(String countryId);

}
