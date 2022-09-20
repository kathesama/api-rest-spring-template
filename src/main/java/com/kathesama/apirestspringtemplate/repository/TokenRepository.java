package com.kathesama.apirestspringtemplate.repository;

import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import com.kathesama.apirestspringtemplate.security.entity.TokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<TokenEntity, String> {
    Optional<TokenEntity> findByToken(String token);
    @Query("{'username': ?0}")
    Optional<TokenEntity> findByUsername(String username);

    int deleteByUser(UserEntity user);
}
