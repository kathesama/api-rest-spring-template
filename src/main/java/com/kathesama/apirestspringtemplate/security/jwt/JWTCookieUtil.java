package com.kathesama.apirestspringtemplate.security.jwt;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import com.kathesama.apirestspringtemplate.security.entity.MyUserDetails;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class JWTCookieUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTCookieUtil.class);

    @Value("${kathesama.app.jwtSecret}")
    private String jwtSecret;

    @Value("${kathesama.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${kathesama.app.jwtCookieName}")
    private String jwtCookie;

    @Value("${kathesama.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    public ResponseCookie generateJwtCookie(MyUserDetails userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return generateCookie(jwtCookie, jwt, "/api/v1");
    }

    public ResponseCookie generateJwtCookie(UserEntity user) {
        String jwt = generateTokenFromUsername(user.getUsername());
        return generateCookie(jwtCookie, jwt, "/api/v1");
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(jwtRefreshCookie, refreshToken, "/api/v1/auth/refreshtoken");
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookie);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api/v1").build();
        return cookie;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtRefreshCookie, null).path("/api/v1/auth/refreshtoken").build();
        return cookie;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value).path(path).domain("localhost").maxAge(24 * 60 * 60).httpOnly(true).secure(false).build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public static void create(HttpServletResponse httpServletResponse, String name, String value, Boolean secure, Integer maxAge, String domain){
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(secure);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    public static void clear(HttpServletResponse httpServletResponse, String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1);
        cookie.setDomain("localhost");
        httpServletResponse.addCookie(cookie);
    }
}
