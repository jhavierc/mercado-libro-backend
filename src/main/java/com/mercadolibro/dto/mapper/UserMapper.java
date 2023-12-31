package com.mercadolibro.dto.mapper;

import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.dto.UserRegisterDTO;
import com.mercadolibro.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    AppUser toAppUser(UserDTO userDTO);
    AppUser toAppUser(UserRegisterDTO userRegistrationDTO);


    UserDTO toUserDTO(AppUser appUser);
    List<UserDTO> toUserDTOs(List<AppUser> appUsers);
}
