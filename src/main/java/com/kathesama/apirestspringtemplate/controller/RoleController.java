package com.kathesama.apirestspringtemplate.controller;

import com.kathesama.apirestspringtemplate.exception.GenericCollectionException;
import com.kathesama.apirestspringtemplate.model.dto.RoleDTO;
import com.kathesama.apirestspringtemplate.model.payload.request.RoleRequestModel;
import com.kathesama.apirestspringtemplate.model.payload.response.RoleResponseModel;
import com.kathesama.apirestspringtemplate.service.implementation.RoleServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${jms.configuration.base-url}/roles")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getAllRoles(){
        try {
            List<RoleResponseModel> returnValue = new ModelMapper().map(
                    roleServiceImpl.getAllRoles(),
                    new TypeToken<List<RoleResponseModel>>() {
                    }.getType()
            );

            return new ResponseEntity<>(returnValue, HttpStatus.OK);
        }catch(GenericCollectionException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> creatRole(@RequestBody @Valid RoleRequestModel role) {
        try{
            RoleDTO roleDto = new RoleDTO();
            BeanUtils.copyProperties(role, roleDto);

            roleDto = roleServiceImpl.createRole(roleDto);
            RoleResponseModel returnValue = new RoleResponseModel();
            BeanUtils.copyProperties(roleDto, returnValue);
            return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
        }catch (ConstraintViolationException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (GenericCollectionException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable String id, @RequestBody @Valid RoleRequestModel role) throws GenericCollectionException {
            RoleDTO roleDto = new RoleDTO();
            BeanUtils.copyProperties(role, roleDto);

            roleDto = roleServiceImpl.updateRole(id, roleDto);
            RoleResponseModel returnValue = new RoleResponseModel();
            BeanUtils.copyProperties(roleDto, returnValue);
            return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeRole(@PathVariable String id){
        try{
            RoleDTO roleDto = roleServiceImpl.deleteRole(id);
            RoleResponseModel returnValue = new RoleResponseModel();
            BeanUtils.copyProperties(roleDto, returnValue);
            return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
        }catch (ConstraintViolationException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (GenericCollectionException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
