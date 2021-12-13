package com.yemektarifim.backend.mapper;

import com.yemektarifim.backend.model.RecipeContent;

public class RecipeContentMapper {
    /**
     * @param database Veritabanından çektiğimiz yemek tarifi içeriği
     * @param client   Kullanıcının yolladığı güncellenecek yeni veriler
     * @return Veritabanı nesnesine, güncellenmek için yollanan tüm veriler aktarılır ve sonuç döndürülür.
     */
    public static RecipeContent update(RecipeContent database, RecipeContent client) {
        database.setRecipe(client.getRecipe());
        database.setRecipeContentName(client.getRecipeContentName());

        return database;
    }
}
