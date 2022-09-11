package com.kathesama.apirestspringtemplate.apirestspringtemplate.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponseModel {
    private String id;
    private String role;
    private Boolean isActive;
}
