package com.kathesama.apirestspringtemplate.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ToString
public enum RoleENUM {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER"),
    ROLE_CREATOR("ROLE_CREATOR"),
    ROLE_EDITOR("ROLE_EDITOR"),
    ROLE_MODERATOR("ROLE_MODERATOR");

    private final String name;

    public static Stream<RoleENUM> stream() {
        return Stream.of(RoleENUM.values());
    }
}
