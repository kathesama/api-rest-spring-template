package com.kathesama.apirestspringtemplate.model.entity;

import com.kathesama.apirestspringtemplate.enums.RoleENUM;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor(staticName = "builder")
@NoArgsConstructor
@Document(collection = "roles")
@ToString
public class RoleEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    @Id
    private String id;

    @NotNull
    @Indexed(unique = true)
    private RoleENUM name;

    private Boolean isActive;

    @FutureOrPresent
    @CreatedDate
    private Date createdAt;

    @FutureOrPresent
    @LastModifiedDate
    private Date updatedAt;
}
