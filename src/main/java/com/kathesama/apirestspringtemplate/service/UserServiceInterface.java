package com.kathesama.apirestspringtemplate.service;

import com.kathesama.apirestspringtemplate.exception.GenericCollectionException;
import com.kathesama.apirestspringtemplate.model.entity.UserEntity;
import com.kathesama.apirestspringtemplate.security.entity.MyUserDetails;

import javax.validation.ConstraintViolationException;

public interface UserServiceInterface {
    public UserEntity getByUsername(String username) throws ConstraintViolationException, GenericCollectionException;
    boolean existsByUserName(String username) throws ConstraintViolationException, GenericCollectionException;
    public void save(UserEntity user) throws ConstraintViolationException, GenericCollectionException;
}
