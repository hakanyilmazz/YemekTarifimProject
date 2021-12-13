package com.yemektarifim.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document("country")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Country {
    @Id
    private String id;

    @Field(name = "countryName")
    @Indexed(unique = true)
    @NonNull
    private String countryName;
}
