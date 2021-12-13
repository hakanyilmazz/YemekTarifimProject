package com.yemektarifim.backend.mapper;

import com.yemektarifim.backend.model.User;

public class UserMapper {
    /**
     * @param database Veritabanından çektiğimiz kullanıcı verileri
     * @param client   Kullanıcının yolladığı güncellenecek yeni veriler
     * @return Veritabanı nesnesine, güncellenmek için yollanan tüm veriler aktarılır ve sonuç döndürülür.
     */
    public static User updateUser(User database, User client) {
        database.setLogin(client.getLogin());
        database.setName(client.getName());
        database.setSurname(client.getSurname());
        database.setAge(client.getAge());
        database.setProfilePhoto(client.getProfilePhoto());
        database.setCountry(client.getCountry());
        database.setIsOnline(client.getIsOnline());

        return database;
    }
}
