package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Setting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SettingRepository extends MongoRepository<Setting, String> {

}

