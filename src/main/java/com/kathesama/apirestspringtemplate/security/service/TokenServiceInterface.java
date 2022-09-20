package com.kathesama.apirestspringtemplate.security.service;

import com.kathesama.apirestspringtemplate.security.entity.TokenEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TokenServiceInterface {
    public Optional<TokenEntity> findByToken(String token);
}
