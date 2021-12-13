package com.yemektarifim.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("comment")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    private String id;

    @DBRef
    @Field(name = "recipe")
    @NonNull
    private Recipe recipe;

    @DBRef
    @Field(name = "commentedUser")
    @NonNull
    private User commentedUser;

    @Field(name = "commentDetail")
    @NonNull
    private String commentDetail;
}
