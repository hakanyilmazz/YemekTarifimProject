package com.yemektarifim.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("recipe_content")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class RecipeContent {
    @Id
    private String id;

    @DBRef
    @Field(name = "recipe")
    @NonNull
    private Recipe recipe;

    @Field(name = "recipeContentName")
    @NonNull
    private String recipeContentName;
}
