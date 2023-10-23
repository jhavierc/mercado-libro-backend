package com.mercadolibro.service.impl;

import com.mercadolibro.Exception.ResourceAlreadyExistsException;
import com.mercadolibro.Exception.ResourceNotFoundException;
import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.dto.UserRegisterDTO;
import com.mercadolibro.dto.mapper.UserMapper;
import com.mercadolibro.entities.AppUser;
import com.mercadolibro.entities.AppUserRole;
import com.mercadolibro.repositories.AppUserRepository;
import com.mercadolibro.repositories.AppUserRoleRepository;
import com.mercadolibro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    /* Variables */
    @Value("${app.user.default-roles}")
    private final List<String> defaultRoles;

    /* Dependencies */
    private final AppUserRepository appUserRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(AppUserRepository appUserRepository, AppUserRoleRepository appUserRoleRepository, @Value("${app.user.default-roles}") List<String> defaultRoles, UserMapper userMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserRoleRepository = appUserRoleRepository;
        this.defaultRoles = defaultRoles;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO create(UserRegisterDTO userRegisterDTO, List<String> roles) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        //TODO encrypt password
        if (appUserRepository.existsByEmail(userRegisterDTO.getEmail())) throw new ResourceAlreadyExistsException("User already exists");

        AppUser appUser = userMapper.toAppUser(userRegisterDTO);
        appUser.setRoles(getRoles(roles));
        appUser.setStatus("ACTIVE");
        appUser.setDateCreated(LocalDateTime.now());

        return userMapper.toUserDTO(appUserRepository.save(appUser));
    }

    @Override
    public UserDTO create(UserRegisterDTO userRegisterDTO) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        return create(userRegisterDTO, defaultRoles);
    }

    private List<AppUserRole> getRoles(List<String> roles) throws ResourceNotFoundException {
        List<AppUserRole> appUserRoles = new ArrayList<>();
        for (String role: roles) {
            appUserRoles.add(appUserRoleRepository.findByDescription(role).orElseThrow(() -> new ResourceNotFoundException("Role " + role + " not found")));
        }

        return appUserRoles;
    }
}
