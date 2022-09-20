package com.kathesama.apirestspringtemplate.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor(staticName = "builder")
@NoArgsConstructor
@Builder
public class MyUserDetails implements UserDetails {
    private String id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static MyUserDetails build(UserEntity user){
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        new MyUserDetails();
        return MyUserDetails.builder()
                .username(user.getUsername())
                .authorities(authorities)
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public List<String> getAuthoritiesList() {
        return this.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MyUserDetails user = (MyUserDetails) o;
        return Objects.equals(id, user.id);
    }
}
