package com.yemektarifim.backend.mapper;

import com.yemektarifim.backend.model.Recipe;

public class RecipeMapper {
    /**
     * @param database Veritabanından çektiğimiz yemek tarifi
     * @param client   Kullanıcının yolladığı güncellenecek yeni veriler
     * @return Veritabanı nesnesine, güncellenmek için yollanan tüm veriler aktarılır ve sonuç döndürülür.
     */
    public static Recipe update(Recipe database, Recipe client) {
        database.setRecipeName(client.getRecipeName());
        database.setRecipePhoto(client.getRecipePhoto());
        database.setUser(client.getUser());
        database.setRecipeDetail(client.getRecipeDetail());

        return database;
    }
}
