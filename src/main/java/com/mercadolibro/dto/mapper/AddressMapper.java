package com.mercadolibro.dto.mapper;

import com.mercadolibro.dto.AddressCreateDTO;
import com.mercadolibro.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address toAddress(AddressCreateDTO addressCreateDTO);
}
