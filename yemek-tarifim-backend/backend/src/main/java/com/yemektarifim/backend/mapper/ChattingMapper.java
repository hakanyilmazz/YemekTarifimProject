package com.yemektarifim.backend.mapper;

import com.yemektarifim.backend.model.Chatting;

public class ChattingMapper {
    /**
     * @param database Veritabanından çektiğimiz sohbet verileri
     * @param client   Kullanıcının yolladığı güncellenecek yeni veriler
     * @return Veritabanı nesnesine, güncellenmek için yollanan tüm veriler aktarılır ve sonuç döndürülür.
     */
    public static Chatting update(Chatting database, Chatting client) {
        database.setUsers(client.getUsers());
        return database;
    }
}
