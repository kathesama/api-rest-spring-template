package com.kathesama.apirestspringtemplate.util;

import com.kathesama.apirestspringtemplate.model.dto.RoleDTO;
import com.kathesama.apirestspringtemplate.model.entity.RoleEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RoleMapperInterface {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoleFromDto(RoleDTO roleDto, @MappingTarget RoleEntity entity);
}
