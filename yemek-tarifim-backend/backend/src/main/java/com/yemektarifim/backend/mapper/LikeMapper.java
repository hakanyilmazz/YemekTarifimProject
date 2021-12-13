package com.yemektarifim.backend.mapper;

import com.yemektarifim.backend.model.Like;

public class LikeMapper {
    /**
     * @param database Veritabanından çektiğimiz like nesnesi
     * @param client   Kullanıcının yolladığı güncellenecek like nesnesi
     * @return Veritabanı nesnesine, güncellenmek için yollanan tüm veriler aktarılır ve sonuç döndürülür.
     */
    public static Like update(Like database, Like client) {
        database.setRecipe(client.getRecipe());
        database.setLikedUser(client.getLikedUser());
        return database;
    }
}
