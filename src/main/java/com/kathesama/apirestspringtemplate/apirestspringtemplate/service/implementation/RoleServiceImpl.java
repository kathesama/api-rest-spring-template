package com.kathesama.apirestspringtemplate.apirestspringtemplate.service.implementation;

import com.kathesama.apirestspringtemplate.apirestspringtemplate.exception.GenericCollectionException;
import com.kathesama.apirestspringtemplate.apirestspringtemplate.model.dto.RoleDTO;
import com.kathesama.apirestspringtemplate.apirestspringtemplate.model.entity.RoleEntity;
import com.kathesama.apirestspringtemplate.apirestspringtemplate.repository.RoleRepository;
import com.kathesama.apirestspringtemplate.apirestspringtemplate.util.RoleMapperInterface;
import com.kathesama.apirestspringtemplate.apirestspringtemplate.service.RoleServiceInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleServiceInterface {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapperInterface mapper;

    @Override
    public List<RoleDTO> getAllRoles() throws ConstraintViolationException, GenericCollectionException {
        return new ModelMapper().map(roleRepository.findAll(), new TypeToken<List<RoleDTO>>() {}.getType());
    }

    @Override
    public RoleDTO getOneRole(String id) throws ConstraintViolationException, GenericCollectionException {
        return null;
    }

    @Override
    public RoleDTO createRole(RoleDTO role) throws ConstraintViolationException, GenericCollectionException {
        RoleEntity roleEntity = new RoleEntity();

        BeanUtils.copyProperties(role, roleEntity);

        roleEntity = roleRepository.save(roleEntity);
        BeanUtils.copyProperties(roleEntity, role);

        return role;
    }

    @Override
    public RoleDTO updateRole(String id, RoleDTO roleDto) throws GenericCollectionException {
        Optional<RoleEntity> actualRole = roleRepository.findById(id);
        if (actualRole.isEmpty()){
            throw new GenericCollectionException(GenericCollectionException.NotFound(id));
        }
        RoleEntity roleRegister = actualRole.get();
        mapper.updateRoleFromDto(roleDto, roleRegister);

        return new ModelMapper().map(roleRepository.save(roleRegister), new TypeToken<RoleDTO>() {}.getType());
    }

    @Override
    public RoleDTO deleteRole(String id) throws ConstraintViolationException, GenericCollectionException {
        Optional<RoleEntity> actualRole = roleRepository.findById(id);
        if (actualRole.isEmpty()){
            throw new GenericCollectionException(GenericCollectionException.NotFound(id));
        }

        RoleEntity roleRegister = actualRole.get();
        roleRegister.setIsActive(false);
        return new ModelMapper().map(roleRepository.save(roleRegister), new TypeToken<RoleDTO>() {}.getType());
    }
}
