package com.yemektarifim.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Set;

@Document("login")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Login {
    @Id
    private String id;

    @Field(name = "username")
    @Indexed(unique = true)
    @NonNull
    private String username;

    @Field(name = "password")
    @NonNull
    private String password;

    @Field(name = "email")
    @Indexed(unique = true)
    @NonNull
    private String email;

    @Field(name = "userTokenList")
    private Set<String> userTokenList;

    @DBRef
    @Field(name = "userRoleList")
    private Set<Role> userRoleList;
}
