package com.kathesama.apirestspringtemplate.controller;

import com.kathesama.apirestspringtemplate.exception.GenericCollectionException;
import com.kathesama.apirestspringtemplate.exception.TokenRefreshException;
import com.kathesama.apirestspringtemplate.model.dto.SignupRequest;
import com.kathesama.apirestspringtemplate.model.entity.RoleEntity;
import com.kathesama.apirestspringtemplate.model.payload.response.MessageResponseModel;
import com.kathesama.apirestspringtemplate.model.payload.response.UserInfoResponse;
import com.kathesama.apirestspringtemplate.model.dto.LoginUser;
import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import com.kathesama.apirestspringtemplate.enums.RoleENUM;
import com.kathesama.apirestspringtemplate.security.entity.MyUserDetails;
import com.kathesama.apirestspringtemplate.security.entity.TokenEntity;
import com.kathesama.apirestspringtemplate.security.jwt.JWTCookieUtil;
import com.kathesama.apirestspringtemplate.security.jwt.JWTProvider;
import com.kathesama.apirestspringtemplate.security.service.implementation.TokenServiceImpl;
import com.kathesama.apirestspringtemplate.service.implementation.UserServiceImpl;
import com.kathesama.apirestspringtemplate.service.implementation.RoleServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${jms.configuration.base-url}/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    JWTProvider jwtProvider;

    @Autowired
    JWTCookieUtil jwtUtils;

    @Autowired
    TokenServiceImpl refreshTokenService;

    @Value("${kathesama.app.jwtCookieName}")
    private String cookieName;

    @PostMapping("/login")
    public ResponseEntity<Object> login(HttpServletResponse httpServletResponse,
                                        @Valid @RequestBody LoginUser loginUser,
                                        BindingResult bidBindingResult) {
        if (bidBindingResult.hasErrors()) {
            return new ResponseEntity<>(new MessageResponseModel("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            List<String> roles = userDetails.getAuthoritiesList();

            TokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(new UserInfoResponse(userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
                            roles));
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponseModel("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody SignupRequest newUser, BindingResult bindingResult) throws GenericCollectionException {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new MessageResponseModel("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);

        UserEntity user =  new ModelMapper().map(newUser, new TypeToken<UserEntity>() {}.getType());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Set<RoleEntity> roles = new HashSet<>();

        if(newUser.getRoles() == null){
            roles.add(new ModelMapper().map(roleService.getOneRoleByName(RoleENUM.ROLE_USER.getName()), new TypeToken<RoleEntity>() {}.getType()));
        }else{
            newUser.getRoles().forEach(role -> {
                try {
                    roles.add(new ModelMapper().map(roleService.getOneRoleByName("ROLE_" + role.toUpperCase()), new TypeToken<RoleEntity>() {}.getType()));
                } catch (GenericCollectionException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        user.setRoles(roles);
        userService.save(user);

        return new ResponseEntity<>(new MessageResponseModel("Registro exitoso! Inicie sesión"), HttpStatus.CREATED);
    }

    @GetMapping("/details")
    public ResponseEntity<Object> getUserDetails() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userName = userDetails.getUsername();
        try {
            UserEntity user= this.userService.getByUsername(userName);
            return new ResponseEntity<>(user, HttpStatus.OK) ;
        }catch(GenericCollectionException ex){
            return new ResponseEntity<>(new MessageResponseModel("No encontrado"), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<MessageResponseModel> logOut(HttpServletResponse httpServletResponse){
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle.toString() != "anonymousUser") {
            String userId = ((MyUserDetails) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponseModel("You've been signed out!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(TokenEntity::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .header(HttpHeaders.SET_COOKIE, refreshToken)
                                .body(new MessageResponseModel("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponseModel("Refresh Token is empty!"));
    }
}
