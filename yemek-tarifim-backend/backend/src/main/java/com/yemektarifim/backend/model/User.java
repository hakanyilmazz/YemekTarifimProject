package com.yemektarifim.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Set;

@Document("user")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private String id;

    @DBRef
    @Field(name = "login")
    @Indexed(unique = true)
    private Login login;

    @Field(name = "name")
    @NonNull
    private String name;

    @Field(name = "surname")
    @NonNull
    private String surname;

    @Field(name = "age")
    @NonNull
    private Integer age;

    @Field(name = "profilePhoto")
    private byte[] profilePhoto;

    @DBRef
    @Field(name = "country")
    @NonNull
    private Country country;

    @Field(name = "isOnline")
    @NonNull
    private Boolean isOnline;

    @DBRef
    @Field(name = "friends")
    @NonNull
    private Set<User> friends;
}
