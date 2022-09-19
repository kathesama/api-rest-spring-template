package com.kathesama.apirestspringtemplate.repository;

import com.kathesama.apirestspringtemplate.model.entity.RoleEntity;
import com.kathesama.apirestspringtemplate.enums.RoleENUM;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<RoleEntity, String> {
//    Optional<RoleEntity> findByRole(String role);
    Optional<RoleEntity> findByName(String name);
}
