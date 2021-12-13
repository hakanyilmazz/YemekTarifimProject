package com.yemektarifim.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("score")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Score {
    @Id
    private String id;

    @DBRef
    @Field(name = "user")
    @NonNull
    private User user;

    @Field(name = "score")
    @NonNull
    private Integer score;
}
