package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.Country;
import com.yemektarifim.backend.service.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;

    /**
     * @return Tüm ülkelerin listesini döndürür.
     */
    @GetMapping
    public ResponseEntity<List<Country>> getAll() {
        List<Country> countryList = countryService.getAll();
        return ResponseEntity.ok(countryList);
    }

    /**
     * @return Id değerine göre bir ülke nesnesi.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Country> getById(@PathVariable String id) {
        Country country = countryService.getById(id);
        return ResponseEntity.ok(country);
    }

    /**
     * Veritabanına yeni bir ülke ekler.
     */
    @PostMapping
    public ResponseEntity<Country> add(@RequestBody Country country) {
        countryService.add(country);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Veritabanındaki bir ülke bilgisini günceller.
     */
    @PutMapping
    public ResponseEntity<Country> update(@RequestBody Country country) {
        countryService.update(country);
        return ResponseEntity.ok().build();
    }

    /**
     * Veritabanından ülke bilgilerini ve bağlı bilgileri siler. Not: User ve Login tablosundaki verileri de etkileyecektir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Country> deleteById(@PathVariable String id) {
        countryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
