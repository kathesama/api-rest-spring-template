package com.kathesama.apirestspringtemplate.security.entity;

import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor(staticName = "builder")
@NoArgsConstructor
@Document(collection = "tokens")
@ToString
public class TokenEntity {
    @Id
    private String id;

    @DBRef
    private UserEntity user;

    @NonNull
    @Indexed(unique=true)
    private String token;

    @NonNull
    private Instant expiryDate;
}
