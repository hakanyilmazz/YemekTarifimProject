package com.yemektarifim.backend.mapper;

import com.yemektarifim.backend.model.Score;

public class ScoreMapper {
    /**
     * @param database Veritabanından çektiğimiz score bilgisi
     * @param client   Kullanıcının yolladığı güncellenecek yeni veriler
     * @return Veritabanı nesnesine, güncellenmek için yollanan tüm veriler aktarılır ve sonuç döndürülür.
     */
    public static Score update(Score database, Score client) {
        database.setUser(client.getUser());
        database.setScore(client.getScore());

        return database;
    }
}
