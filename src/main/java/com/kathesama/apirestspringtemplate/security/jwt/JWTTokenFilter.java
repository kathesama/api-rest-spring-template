package com.kathesama.apirestspringtemplate.security.jwt;

import com.kathesama.apirestspringtemplate.security.entity.MyUserDetails;
import com.kathesama.apirestspringtemplate.security.service.implementation.MyUserDetailsServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JWTTokenFilter.class);

    @Autowired
    private  JWTProvider jwtProvider;
    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsServiceImpl;

    @Value("${kathesama.app.jwtCookieName}")
    private String cookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException{
        try {
            String token = getToken(req);
            if (token != null && jwtProvider.validateToken(token)) {
                String userName = jwtProvider.getUserNameFromToken(token);
                MyUserDetails userDetails = myUserDetailsServiceImpl.loadUserByUsername(userName);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
            logger.error(e.getMessage());
        }
        filterChain.doFilter(req, res);
    }

    private String getToken(HttpServletRequest request){
        /*
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer"))
            return header.replace("Bearer ", "");
        return null;
        */
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        return cookie != null ? cookie.getValue():null;
    }
}
