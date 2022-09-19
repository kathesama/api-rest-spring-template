package com.kathesama.apirestspringtemplate.controller;

import com.kathesama.apirestspringtemplate.exception.GenericCollectionException;
import com.kathesama.apirestspringtemplate.model.dto.SignupRequest;
import com.kathesama.apirestspringtemplate.model.entity.RoleEntity;
import com.kathesama.apirestspringtemplate.model.payload.response.MessageResponseModel;
import com.kathesama.apirestspringtemplate.security.dto.JWTDTO;
import com.kathesama.apirestspringtemplate.model.dto.LoginUser;
import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import com.kathesama.apirestspringtemplate.enums.RoleENUM;
import com.kathesama.apirestspringtemplate.security.jwt.JWTProvider;
import com.kathesama.apirestspringtemplate.service.implementation.UserServiceImpl;
import com.kathesama.apirestspringtemplate.service.implementation.RoleServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("${jms.configuration.base-url}/auth")
public class AuthController {
    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    JWTProvider jwtProvider;

//    @Autowired
//    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder,
//                          UserServiceImpl userService, RoleServiceImpl roleService, JWTProvider jwtProvider) {
//        this.authenticationManagerBuilder = authenticationManagerBuilder;
//        this.passwordEncoder = passwordEncoder;
//        this.userService = userService;
//        this.roleService = roleService;
//        this.jwtProvider = jwtProvider;
//    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginUser loginUser, BindingResult bidBindingResult) {
        if (bidBindingResult.hasErrors()) {
            return new ResponseEntity<>(new MessageResponseModel("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        }
        try {
            UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateToken(authentication);
            JWTDTO jwtDto = new JWTDTO(jwt);
            return new ResponseEntity<>(jwtDto, HttpStatus.OK);
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
}
