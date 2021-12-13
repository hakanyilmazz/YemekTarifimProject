package com.yemektarifim.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Set;

@Document("chatting")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Chatting {
    @DBRef
    @Indexed(unique = true)
    @Field(name = "users")
    @NonNull
    Set<User> users;

    @Id
    private String id;
}
