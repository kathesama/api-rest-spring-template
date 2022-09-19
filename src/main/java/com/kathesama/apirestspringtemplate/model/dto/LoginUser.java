package com.kathesama.apirestspringtemplate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor(staticName = "builder")
@NoArgsConstructor
@ToString
public class LoginUser {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
