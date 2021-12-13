package com.yemektarifim.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("message")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Message {
    @Id
    private String id;

    @DBRef
    @Field(name = "senderUser")
    @NonNull
    private String senderUser;

    @DBRef
    @Field(name = "receiverUser")
    @NonNull
    private User receiverUser;

    @Field(name = "messageContent")
    @NonNull
    private String messageContent;

    @DBRef
    @Field(name = "recipe")
    private Recipe recipe;

    @DBRef
    @Field(name = "chatting")
    @NonNull
    private Chatting chatting;
}
