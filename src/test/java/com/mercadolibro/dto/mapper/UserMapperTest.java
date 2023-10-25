package com.mercadolibro.dto.mapper;

import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.dto.UserRegisterDTO;
import com.mercadolibro.entities.AppUser;
import com.mercadolibro.entities.AppUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    UserMapper mapper = UserMapper.INSTANCE;
    List<AppUser> appUserList = new ArrayList<>();
    List<UserDTO> userDTOList = new ArrayList<>();
    List<UserRegisterDTO> userRegisterDTOList = new ArrayList<>();
    List<AppUserRole> userRoles = Arrays.asList(new AppUserRole(1, "USER", "ACTIVE"));

    @BeforeEach
    void setUp() {

        appUserList.add(new AppUser(1, "Jorge", "Perez", "jorge@perez.com", "123456", "ACTIVE", LocalDateTime.of(2023, 9, 10, 20, 45), userRoles));

        userDTOList.add(new UserDTO(1, "Jorge", "Perez", "jorge@perez.com", "ACTIVE", LocalDateTime.of(2023, 9, 10, 20, 45), userRoles));

        userRegisterDTOList.add(new UserRegisterDTO("Jorge", "Perez", "jorge@perez.com", "123456"));
    }

    @Test
    void shouldMapAppUserToUserDTO() {
        AppUser appUser = appUserList.get(0);
        UserDTO userDTO = userDTOList.get(0);

        assertEquals(userDTO, mapper.toUserDTO(appUser));
    }

    @Test
    void shouldMapUserDTOToAppUser() {
        AppUser appUser = appUserList.get(0);
        UserDTO userDTO = userDTOList.get(0);
        appUser.setPassword(null);

        assertEquals(appUser, mapper.toAppUser(userDTO));
    }

    @Test
    void shouldMapUserRegisterDTOToAppUser() {
        AppUser appUser = appUserList.get(0);
        UserRegisterDTO userRegisterDTO = userRegisterDTOList.get(0);
        appUser.setId(null);
        appUser.setStatus(null);
        appUser.setDateCreated(null);
        appUser.setRoles(null);

        assertEquals(appUser, mapper.toAppUser(userRegisterDTO));
    }

}