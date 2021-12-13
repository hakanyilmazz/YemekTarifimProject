package com.yemektarifim.backend.mapper;

import com.yemektarifim.backend.model.Message;

public class MessageMapper {
    /**
     * @param database Veritabanından çektiğimiz message nesnesi
     * @param client   Kullanıcının yolladığı güncellenecek message nesnesi
     * @return Veritabanı nesnesine, güncellenmek için yollanan tüm veriler aktarılır ve sonuç döndürülür.
     */
    public static Message update(Message database, Message client) {
        database.setSenderUser(client.getSenderUser());
        database.setReceiverUser(client.getReceiverUser());
        database.setMessageContent(client.getMessageContent());
        database.setRecipe(client.getRecipe());
        database.setChatting(client.getChatting());
        return database;
    }
}