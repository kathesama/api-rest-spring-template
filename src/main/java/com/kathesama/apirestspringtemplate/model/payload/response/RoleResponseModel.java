package com.kathesama.apirestspringtemplate.model.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseModel {
    private String id;
    private String role;
    private Boolean isActive;
}
