package com.kathesama.apirestspringtemplate.apirestspringtemplate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor(staticName = "builder")
@NoArgsConstructor
@ToString
public class RoleDTO {
    private String id;
    private String role;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
}
