package com.kathesama.apirestspringtemplate.security.service.implementation;

import com.kathesama.apirestspringtemplate.exception.TokenRefreshException;
import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import com.kathesama.apirestspringtemplate.repository.TokenRepository;
import com.kathesama.apirestspringtemplate.repository.UserRepository;
import com.kathesama.apirestspringtemplate.security.entity.TokenEntity;
import com.kathesama.apirestspringtemplate.security.service.TokenServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenServiceInterface {
    @Value("${kathesama.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<TokenEntity> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
    public Optional<TokenEntity> findByUsername(String username) {
        return tokenRepository.findByUsername(username);
    }

    public TokenEntity createRefreshToken(String username) {
        UserEntity actualUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        TokenEntity refreshToken = new TokenEntity();

        refreshToken.setUser(actualUser);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = tokenRepository.save(refreshToken);
        return refreshToken;
    }

    public TokenEntity createTokenEntity(String userId) {
        UserEntity actualUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));

        TokenEntity refreshToken = new TokenEntity();

        refreshToken.setUser(actualUser);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return tokenRepository.save(refreshToken);
    }

    public TokenEntity verifyExpiration(TokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public int deleteByUserId(String userId) {
        UserEntity actualUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));

        return tokenRepository.deleteByUser(actualUser);
    }
}
