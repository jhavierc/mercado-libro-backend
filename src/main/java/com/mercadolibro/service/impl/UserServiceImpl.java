package com.mercadolibro.service.impl;

import com.mercadolibro.dto.UserUpdateDTO;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.dto.UserRegisterDTO;
import com.mercadolibro.dto.mapper.UserMapper;
import com.mercadolibro.entity.AppUser;
import com.mercadolibro.entity.AppUserRole;
import com.mercadolibro.repository.AppUserRepository;
import com.mercadolibro.repository.AppUserRoleRepository;
import com.mercadolibro.service.UserService;
import com.mercadolibro.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    /* Variables */
    private final List<String> defaultRoles;

    /* Dependencies */
    private final AppUserRepository appUserRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(AppUserRepository appUserRepository, AppUserRoleRepository appUserRoleRepository, @Value("${app.user.default-roles}") List<String> defaultRoles, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appUserRoleRepository = appUserRoleRepository;
        this.defaultRoles = defaultRoles;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO create(UserRegisterDTO userRegisterDTO, List<String> roles) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        if (appUserRepository.existsByEmail(userRegisterDTO.getEmail())) throw new ResourceAlreadyExistsException("User already exists");

        AppUser appUser = userMapper.toAppUser(userRegisterDTO);
        appUser.setRoles(getRoles(roles));
        appUser.setStatus("ACTIVE");
        appUser.setDateCreated(LocalDateTime.now());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        return userMapper.toUserDTO(appUserRepository.save(appUser));
    }

    @Override
    public UserDTO create(UserRegisterDTO userRegisterDTO) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        return create(userRegisterDTO, defaultRoles);
    }

    @Override
    public UserDTO findByEmail(String email) throws ResourceNotFoundException {
        return userMapper.toUserDTO(appUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User " + email + " not found")));
    }

    @Override
    public UserDTO findById(Integer id) throws ResourceNotFoundException {
        return userMapper.toUserDTO(appUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found")));
    }

    @Override
    public List<AppUserRole> findAllRoles() {
        return appUserRoleRepository.findAll();
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.toUserDTOs(appUserRepository.findAll());
    }

    @Override
    public UserDTO update(UserUpdateDTO userUpdateDTO, Integer userId) throws ResourceNotFoundException {
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        if (userUpdateDTO.getPassword() != null ) userUpdateDTO.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        if (userUpdateDTO.getRoles() != null) {
            List<AppUserRole> roles = getRoles(userUpdateDTO.getRoles().stream().map(AppUserRole::getDescription).collect(Collectors.toList()));
            userUpdateDTO.setRoles(roles);
        }
        Util.mergeObjects(userUpdateDTO, appUser);

        return userMapper.toUserDTO(appUserRepository.save(appUser));
    }

    private List<AppUserRole> getRoles(List<String> roles) throws ResourceNotFoundException {
        List<AppUserRole> appUserRoles = new ArrayList<>();
        for (String role: roles) {
            appUserRoles.add(appUserRoleRepository.findByDescription(role).orElseThrow(() -> new ResourceNotFoundException("Role " + role + " not found")));
        }

        return appUserRoles;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(appUser.getRoles().stream().map(AppUserRole::getDescription).toArray(String[]::new)).build();
    }
}
