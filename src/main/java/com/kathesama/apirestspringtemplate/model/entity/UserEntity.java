package com.kathesama.apirestspringtemplate.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251206L;

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    @Size(max = 20)
    private String username;

    @JsonIgnore
    private String password;

    @Email
    @Size(max = 50)
    private String email;

    private String firstName;

    private String lastName;

    private int age;

    private boolean isActive;

    private boolean isGoogle;

    private String image;

    @DBRef(lazy = true)
    private Set<RoleEntity> roles = new HashSet<>();

    @FutureOrPresent
    @CreatedDate
    private Date createdAt;

    @FutureOrPresent
    @LastModifiedDate
    private Date updatedAt;
}
