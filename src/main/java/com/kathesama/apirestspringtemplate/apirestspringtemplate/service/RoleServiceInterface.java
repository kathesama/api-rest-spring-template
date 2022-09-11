package com.kathesama.apirestspringtemplate.apirestspringtemplate.service;

import com.kathesama.apirestspringtemplate.apirestspringtemplate.exception.GenericCollectionException;
import com.kathesama.apirestspringtemplate.apirestspringtemplate.model.dto.RoleDTO;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface RoleServiceInterface {
    public List<RoleDTO> getAllRoles() throws ConstraintViolationException, GenericCollectionException;
    public RoleDTO getOneRole(String id) throws ConstraintViolationException, GenericCollectionException;
    public RoleDTO createRole(RoleDTO todo) throws ConstraintViolationException, GenericCollectionException;
    public RoleDTO updateRole(String id, RoleDTO todo) throws ConstraintViolationException, GenericCollectionException;
    public RoleDTO deleteRole(String id) throws ConstraintViolationException, GenericCollectionException;
}
