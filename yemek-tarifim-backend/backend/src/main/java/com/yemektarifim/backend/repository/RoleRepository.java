package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    @Query("{'roleName': ?0}")
    Optional<Role> findByRoleName(String roleName);
}
