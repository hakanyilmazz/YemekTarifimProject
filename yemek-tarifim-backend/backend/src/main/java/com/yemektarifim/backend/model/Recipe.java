package com.yemektarifim.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("recipe")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Recipe {
    @Id
    private String id;

    @Field(name = "recipeName")
    @NonNull
    private String recipeName;

    @Field(name = "recipePhoto")
    @NonNull
    private byte[] recipePhoto;

    @DBRef
    @Field(name = "user")
    @NonNull
    private User user;

    @Field(name = "recipeDetail")
    @NonNull
    private String recipeDetail;
}
