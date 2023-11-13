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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

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

    @Test
    void shouldFindUserByEmail() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);

        //WHEN
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDTO userFound = userService.findByEmail(user.getEmail());

        // THEN
        assertNotNull(userFound);
        assertEquals(user.getName(), userFound.getName());
        assertEquals(user.getLastName(), userFound.getLastName());
        assertEquals(user.getEmail(), userFound.getEmail());
        assertEquals(user.getRoles().get(0).getDescription(), userFound.getRoles().get(0).getDescription());
        assertEquals(user.getId(), userFound.getId());
        assertEquals(user.getStatus(), userFound.getStatus());
        assertEquals(user.getDateCreated(), userFound.getDateCreated());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void findUserByEmailShouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);

        //WHEN
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.findByEmail(user.getEmail()));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void loadUserByUsernameShouldReturnUserDetails() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);

        //WHEN
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userFound = userService.loadUserByUsername(user.getEmail());

        // THEN
        assertNotNull(userFound);
        assertEquals(user.getEmail(), userFound.getUsername());
        assertEquals(user.getPassword(), userFound.getPassword());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);

        //WHEN
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // THEN
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user.getEmail()));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void findByIdShouldReturnUser() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);

        //WHEN
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDTO userFound = userService.findById(user.getId());

        // THEN
        assertNotNull(userFound);
        assertEquals(user.getName(), userFound.getName());
        assertEquals(user.getLastName(), userFound.getLastName());
        assertEquals(user.getEmail(), userFound.getEmail());
        assertEquals(user.getRoles().get(0).getDescription(), userFound.getRoles().get(0).getDescription());
        assertEquals(user.getId(), userFound.getId());
        assertEquals(user.getStatus(), userFound.getStatus());
        assertEquals(user.getDateCreated(), userFound.getDateCreated());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);

        //WHEN
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(user.getId()));
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void findAllRolesShouldReturnAllRoles() {
        // GIVEN
        AppUserRole role = users.get(0).getRoles().get(0);

        //WHEN
        when(userRoleRepository.findAll()).thenReturn(Arrays.asList(role));

        List<AppUserRole> rolesFound = userService.findAllRoles();

        // THEN
        assertNotNull(rolesFound);
        assertEquals(1, rolesFound.size());
        assertEquals(role.getId(), rolesFound.get(0).getId());
        assertEquals(role.getDescription(), rolesFound.get(0).getDescription());
        assertEquals(role.getStatus(), rolesFound.get(0).getStatus());
        verify(userRoleRepository, times(1)).findAll();
    }

    @Test
    void findAllShouldReturnAllUsers() {
        // GIVEN
        AppUser user = users.get(0);

        //WHEN
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> usersFound = userService.findAll();

        // THEN
        assertNotNull(usersFound);
        assertEquals(1, usersFound.size());
        assertEquals(user.getName(), usersFound.get(0).getName());
        assertEquals(user.getLastName(), usersFound.get(0).getLastName());
        assertEquals(user.getEmail(), usersFound.get(0).getEmail());
        assertEquals(user.getRoles().get(0).getDescription(), usersFound.get(0).getRoles().get(0).getDescription());
        assertEquals(user.getId(), usersFound.get(0).getId());
        assertEquals(user.getStatus(), usersFound.get(0).getStatus());
        assertEquals(user.getDateCreated(), usersFound.get(0).getDateCreated());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUserWithAllFieldsShouldReturnUser() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(user.getName() + "modified", user.getLastName() + "modified", "modified@email.com", "modifiedpassword", "ACTIVE",
                Arrays.asList(new AppUserRole(1, "ADMIN", "ACTIVE")));

        //WHEN
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRoleRepository.findByDescription("ADMIN")).thenReturn(
                Optional.of(new AppUserRole(1, "ADMIN", "ACTIVE")));
        UserDTO userUpdated = userService.update(userUpdateDTO, user.getId());

        // THEN
        assertNotNull(userUpdated);
        assertEquals(user.getName(), userUpdated.getName());
        assertEquals(user.getLastName(), userUpdated.getLastName());
        assertEquals(user.getEmail(), userUpdated.getEmail());
        assertEquals(user.getRoles().get(0).getDescription(), userUpdated.getRoles().get(0).getDescription());
        assertEquals(user.getId(), userUpdated.getId());
        assertEquals(user.getStatus(), userUpdated.getStatus());
        assertEquals(user.getDateCreated(), userUpdated.getDateCreated());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void updateUserWithSomeFieldsShouldReturnUser() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(user.getName() + "modified", user.getLastName() + "modified", null, null, null,
                null);

        //WHEN
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UserDTO userUpdated = userService.update(userUpdateDTO, user.getId());

        // THEN
        assertNotNull(userUpdated);
        assertEquals(user.getName(), userUpdated.getName());
        assertEquals(user.getLastName(), userUpdated.getLastName());
        assertEquals(user.getEmail(), userUpdated.getEmail());
        assertEquals(user.getRoles().get(0).getDescription(), userUpdated.getRoles().get(0).getDescription());
        assertEquals(user.getId(), userUpdated.getId());
        assertEquals(user.getStatus(), userUpdated.getStatus());
        assertEquals(user.getDateCreated(), userUpdated.getDateCreated());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void updateUserShouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(user.getName() + "modified", user.getLastName() + "modified", null, null, null,
                null);

        //WHEN
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.update(userUpdateDTO, user.getId()));
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, never()).save(any(AppUser.class));
    }

    @Test
    void updateUserShouldThrowResourceNotFoundExceptionWhenRoleDoesNotExist() throws ResourceNotFoundException {
        // GIVEN
        AppUser user = users.get(0);
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(user.getName() + "modified", user.getLastName() + "modified", null, null, null,
                Arrays.asList(new AppUserRole(1, "ADMIN", "ACTIVE")));

        //WHEN
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRoleRepository.findByDescription("ADMIN")).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.update(userUpdateDTO, user.getId()));
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, never()).save(any(AppUser.class));
        verify(userRoleRepository, atLeast(1)).findByDescription("ADMIN");
    }

    @Test
    void findShouldReturnAllUsers() {
        // GIVEN
        AppUser user = users.get(0);

        //WHEN
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> usersFound = userService.findAll();

        // THEN
        assertNotNull(usersFound);
        assertEquals(1, usersFound.size());
        assertEquals(user.getName(), usersFound.get(0).getName());
        assertEquals(user.getLastName(), usersFound.get(0).getLastName());
        assertEquals(user.getEmail(), usersFound.get(0).getEmail());
        assertEquals(user.getRoles().get(0).getDescription(), usersFound.get(0).getRoles().get(0).getDescription());
        assertEquals(user.getId(), usersFound.get(0).getId());
        assertEquals(user.getStatus(), usersFound.get(0).getStatus());
        assertEquals(user.getDateCreated(), usersFound.get(0).getDateCreated());
        verify(userRepository, times(1)).findAll();
    }


}