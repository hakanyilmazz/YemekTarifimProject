package com.yemektarifim.backend.repository;

import com.yemektarifim.backend.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CountryRepository extends MongoRepository<Country, String> {
}
