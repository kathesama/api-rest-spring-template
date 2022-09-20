package com.kathesama.apirestspringtemplate.security.service.implementation;

import com.kathesama.apirestspringtemplate.repository.UserRepository;
import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import com.kathesama.apirestspringtemplate.security.entity.MyUserDetails;
import com.kathesama.apirestspringtemplate.security.service.MyUserDetailsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsServiceInterface {
    @Autowired
    UserRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String username) throws ConstraintViolationException {
        UserEntity actualUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return MyUserDetails.build(actualUser);
    }
}
