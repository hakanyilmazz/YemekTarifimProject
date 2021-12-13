package com.yemektarifim.backend.mapper;

import com.yemektarifim.backend.model.Comment;

public class CommentMapper {
    /**
     * @param database Veritabanından çektiğimiz yorum
     * @param client   Kullanıcının yolladığı güncellenecek yeni yorum bilgisi
     * @return Veritabanı nesnesine, güncellenmek için yollanan tüm veriler aktarılır ve sonuç döndürülür.
     */
    public static Comment update(Comment database, Comment client) {
        database.setRecipe(client.getRecipe());
        database.setCommentedUser(client.getCommentedUser());
        database.setCommentDetail(client.getCommentDetail());
        return database;
    }
}
