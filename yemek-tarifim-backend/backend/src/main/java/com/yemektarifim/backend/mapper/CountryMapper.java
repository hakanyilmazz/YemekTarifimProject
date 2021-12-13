package com.yemektarifim.backend.mapper;

import com.yemektarifim.backend.model.Country;

public class CountryMapper {
    /**
     * @param database Veritabanından çektiğimiz ülke nesnesi
     * @param client   Kullanıcının yolladığı güncellenecek ülke nesnesi
     * @return Veritabanı nesnesine, güncellenmek için yollanan tüm veriler aktarılır ve sonuç döndürülür.
     */
    public static Country update(Country database, Country client) {
        database.setCountryName(client.getCountryName());
        return database;
    }
}

