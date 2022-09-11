package com.kathesama.apirestspringtemplate.apirestspringtemplate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor(staticName = "builder")
@NoArgsConstructor
@Document(collection = "roles")
public class RoleEntity {
    @Id
    private String id;

    @NotNull
    private String role;

    private Boolean isActive;

    @FutureOrPresent
    private Date createdAt;

    @FutureOrPresent
    private Date updatedAt;
}
