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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private AppUserRepository userRepository;
    @Mock
    private AppUserRoleRepository userRoleRepository;

    @Spy
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @InjectMocks
    private UserServiceImpl userService;

    List<AppUser> users;

    @BeforeEach
    void setUp(){
        users = Arrays.asList(
                new AppUser(1, "Jorge", "Perez", "jorge@perez.com", "123456", "ACTIVE", LocalDateTime.of(2023, 9, 10, 20, 45), Arrays.asList(new AppUserRole(1, "USER", "ACTIVE")))
        );

    }

    @Test
    void shouldCreateUser() throws ResourceAlreadyExistsException, ResourceNotFoundException {

        // GIVEN
        AppUser user = users.get(0);
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(user.getName(), user.getLastName(), user.getEmail(), user.getPassword());

        //WHEN

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser userToSave = invocation.getArgument(0);
            userToSave.setId(1);
            userToSave.setStatus("ACTIVE");
            userToSave.setDateCreated(LocalDateTime.now());
            return userToSave;
        });
        when(userRoleRepository.findByDescription(users.get(0).getRoles().get(0).getDescription())).thenReturn(
                Optional.of(users.get(0).getRoles().get(0))
        );

        UserDTO userSaved = userService.create(userRegisterDTO, users.get(0).getRoles().stream().map(AppUserRole::getDescription).collect(Collectors.toList()));

        // THEN
        assertNotNull(userSaved);
        assertEquals(user.getName(), userSaved.getName());
        assertEquals(user.getLastName(), userSaved.getLastName());
        assertEquals(user.getEmail(), userSaved.getEmail());
        assertEquals(user.getRoles().get(0).getDescription(), userSaved.getRoles().get(0).getDescription());
        assertEquals(1, userSaved.getId());
        assertEquals("ACTIVE", userSaved.getStatus());
        assertNotNull(userSaved.getDateCreated());
        assertTrue(userSaved.getDateCreated().isBefore(LocalDateTime.now()));
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(AppUser.class));
        verify(userRoleRepository, atLeast(1)).findByDescription(users.get(0).getRoles().get(0).getDescription());
    }

    @Test
    void createUserShouldThrowResourceAlreadyExistsExceptionWhenUserAlreadyExists() throws ResourceAlreadyExistsException, ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(user.getName(), user.getLastName(), user.getEmail(), user.getPassword());

        //WHEN
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // THEN
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.create(userRegisterDTO, users.get(0).getRoles().stream().map(AppUserRole::getDescription).collect(Collectors.toList())));
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(any(AppUser.class));
        verify(userRoleRepository, never()).findByDescription(users.get(0).getRoles().get(0).getDescription());
    }

    @Test
    void createUserShouldThrowResourceNotFoundExceptionWhenRoleDoesNotExist() throws ResourceAlreadyExistsException, ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(user.getName(), user.getLastName(), user.getEmail(), user.getPassword());

        //WHEN
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRoleRepository.findByDescription(users.get(0).getRoles().get(0).getDescription())).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.create(userRegisterDTO, users.get(0).getRoles().stream().map(AppUserRole::getDescription).collect(Collectors.toList())));
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(any(AppUser.class));
        verify(userRoleRepository, atLeast(1)).findByDescription(users.get(0).getRoles().get(0).getDescription());
    }
}