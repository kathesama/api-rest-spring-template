package com.kathesama.apirestspringtemplate.service.implementation;

import com.kathesama.apirestspringtemplate.exception.GenericCollectionException;
import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import com.kathesama.apirestspringtemplate.repository.UserRepository;
import com.kathesama.apirestspringtemplate.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserServiceInterface {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserEntity getByUsername(String username) throws ConstraintViolationException, GenericCollectionException {
        UserEntity actualUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new GenericCollectionException(GenericCollectionException.NotFound(username)));

        return actualUser;
    }

    @Override
    public boolean existsByUserName(String username) {
        Optional<UserEntity> actualUser = userRepository.findByUsername(username);

        return actualUser.isEmpty();
    }

    @Override
    public void save(UserEntity user) throws ConstraintViolationException, GenericCollectionException {
        userRepository.save(user);
    }
}
