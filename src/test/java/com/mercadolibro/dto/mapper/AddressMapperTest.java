package com.mercadolibro.dto.mapper;

import com.mercadolibro.dto.AddressCreateDTO;
import com.mercadolibro.entity.Address;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressMapperTest {

    private final AddressMapper addressMapper = AddressMapper.INSTANCE;

    @Test
    void shouldMapAddressCreateDTOToAddress() {
        // Given
        AddressCreateDTO addressCreateDTO = AddressCreateDTO.builder()
                .street("Street Name")
                .number(123)
                .zipCode("Postal Code")
                .city("City")
                .state("State")
                .department("Department")
                .district("District")
                .build();

        // When
        Address address = addressMapper.toAddress(addressCreateDTO);

        // Then
        assertNotNull(address);
        assertEquals(addressCreateDTO.getStreet(), address.getStreet());
        assertEquals(addressCreateDTO.getNumber(), address.getNumber());
        assertEquals(addressCreateDTO.getZipCode(), address.getZipCode());
        assertEquals(addressCreateDTO.getCity(), address.getCity());
        assertEquals(addressCreateDTO.getState(), address.getState());
        assertEquals(addressCreateDTO.getDepartment(), address.getDepartment());
        assertEquals(addressCreateDTO.getDistrict(), address.getDistrict());
    }

}