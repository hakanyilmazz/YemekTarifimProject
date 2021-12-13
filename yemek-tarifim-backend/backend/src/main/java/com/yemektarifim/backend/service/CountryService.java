package com.yemektarifim.backend.service;

import com.yemektarifim.backend.mapper.CountryMapper;
import com.yemektarifim.backend.model.Country;
import com.yemektarifim.backend.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final LoginService loginService;

    /**
     * @return Veritabanındaki ülkelerin listesi
     */
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    /**
     * @param id Ülke id değeri
     * @return O id değerine sahip ülke.
     */
    public Country getById(String id) {
        return countryRepository.findById(id).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Country by Id %s", id)
        ));
    }

    /**
     * Veritabanına ülke nesnesini kaydeder.
     */
    public void add(Country country) {
        countryRepository.insert(country);
    }

    /**
     * Veritabanındaki bir ülke nesnesinin bilgilerini günceller.
     */
    public void update(Country country) {
        Country database = countryRepository.findById(country.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find Country by Id %s", country.getId())
                ));

        Country result = CountryMapper.update(database, country);

        countryRepository.save(result);
    }

    /**
     * Veritabanındaki ülkenin id değerini bulur ve siler. Ayrıca o id değerini kullanan diğer tablolardaki nesnelerde silinir.
     *
     * @param countryId Veritabanından silinecek ülkenin id değeri.
     */
    public void deleteById(String countryId) {
        loginService.deleteByCountryId(countryId);
        countryRepository.deleteById(countryId);
    }
}
