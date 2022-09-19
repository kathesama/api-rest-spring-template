package com.kathesama.apirestspringtemplate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor(staticName = "builder")
@NoArgsConstructor
@ToString
public class RoleDTO {
    private String id;
    private String role;
    private Boolean isActive;
}
