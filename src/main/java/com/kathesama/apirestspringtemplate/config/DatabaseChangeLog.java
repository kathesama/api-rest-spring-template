package com.kathesama.apirestspringtemplate.config;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.kathesama.apirestspringtemplate.model.entity.RoleEntity;
import com.kathesama.apirestspringtemplate.repository.RoleRepository;
import com.kathesama.apirestspringtemplate.enums.RoleENUM;

import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class DatabaseChangeLog {

    @ChangeSet(order = "001", id = "seedDatabase", author = "kathesama")
    public void seedDatabase(RoleRepository roleRepository){
        List<RoleEntity> roleList = new ArrayList<>();
        RoleENUM.stream().forEach(role -> roleList.add(createNewRole(role)));

        roleRepository.insert(roleList);
    }

    private RoleEntity createNewRole(RoleENUM name){
        RoleEntity role = new RoleEntity();
        role.setName(name);
        role.setIsActive(true);
        return role;
    }
}
