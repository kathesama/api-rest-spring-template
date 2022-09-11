package com.kathesama.apirestspringtemplate.apirestspringtemplate.repository;

import com.kathesama.apirestspringtemplate.apirestspringtemplate.model.entity.RoleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<RoleEntity, String> {
}
