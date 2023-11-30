package com.mercadolibro.service.impl;

import com.mercadolibro.dto.AddressCreateDTO;
import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.dto.mapper.AddressMapper;
import com.mercadolibro.entity.Address;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.repository.AddressRepository;
import com.mercadolibro.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    AddressRepository addressRepository;
    @Mock
    UserService userService;

    @Spy
    AddressMapper addressMapper = AddressMapper.INSTANCE;

    @InjectMocks
    AddressServiceImpl addressService;

    @Test
    void shouldCreateAddressWhenUserExistsAndAddressDoesNotExist() throws ResourceNotFoundException, ResourceAlreadyExistsException {
        // GIVEN
        AddressCreateDTO addressCreateDTO = AddressCreateDTO.builder()
                .street("street")
                .city("city")
                .number(1)
                .zipCode("zipCode")
                .state("state")
                .build();
        String userEmail = "userEmail";
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setEmail(userEmail);

        // WHEN
        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(addressRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
            Address address = invocation.getArgument(0);
            address.setId(1);
            return address;
        });

        // THEN
        Address addressCreated = addressService.create(addressCreateDTO, userEmail);
        assertNotNull(addressCreated);
        assertEquals(addressCreateDTO.getStreet(), addressCreated.getStreet());
        assertEquals(addressCreateDTO.getCity(), addressCreated.getCity());
        assertEquals(addressCreateDTO.getNumber(), addressCreated.getNumber());
        assertEquals(addressCreateDTO.getZipCode(), addressCreated.getZipCode());
        assertEquals(addressCreateDTO.getState(), addressCreated.getState());
        assertEquals(user.getId(), addressCreated.getUserId());
    }

    @Test
    void shouldUpdateAddressWhenUserExistsAndAddressExists() throws ResourceNotFoundException, ResourceAlreadyExistsException {
        // GIVEN
        AddressCreateDTO addressCreateDTO = AddressCreateDTO.builder()
                .street("street")
                .city("city")
                .number(1)
                .zipCode("zipCode")
                .state("state")
                .build();
        String userEmail = "userEmail";
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setEmail(userEmail);
        Address oldAddress = Address.builder()
                .id(1)
                .street("oldStreet")
                .city("oldCity")
                .number(1)
                .zipCode("oldZipCode")
                .state("oldState")
                .userId(user.getId())
                .build();

        // WHEN
        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(addressRepository.findByUserId(user.getId())).thenReturn(Optional.of(oldAddress));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
            Address address = invocation.getArgument(0);
            if (address.getId() == null) {
                address.setId(3);
            }
            return address;
        });

        // THEN
        Address addressCreated = addressService.create(addressCreateDTO, userEmail);
        assertNotNull(addressCreated);
        assertEquals(oldAddress.getId(), addressCreated.getId());
        assertEquals(addressCreateDTO.getStreet(), addressCreated.getStreet());
        assertEquals(addressCreateDTO.getCity(), addressCreated.getCity());
        assertEquals(addressCreateDTO.getNumber(), addressCreated.getNumber());
        assertEquals(addressCreateDTO.getZipCode(), addressCreated.getZipCode());
        assertEquals(addressCreateDTO.getState(), addressCreated.getState());
        assertEquals(user.getId(), addressCreated.getUserId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() throws ResourceNotFoundException {
        // GIVEN
        AddressCreateDTO addressCreateDTO = AddressCreateDTO.builder()
                .street("street")
                .city("city")
                .number(1)
                .zipCode("zipCode")
                .state("state")
                .build();
        String userEmail = "userEmail";

        // WHEN
        when(userService.findByEmail(userEmail)).thenThrow(new ResourceNotFoundException("User not found"));

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> addressService.create(addressCreateDTO, userEmail));
    }

    @Test
    void shouldGetAddressByUserEmail() throws ResourceNotFoundException {
        // GIVEN
        String userEmail = "userEmail";
        Address address = Address.builder()
                .id(1)
                .street("street")
                .city("city")
                .number(1)
                .zipCode("zipCode")
                .state("state")
                .userId(1)
                .build();

        // WHEN
        when(addressRepository.findByUserEmail(userEmail)).thenReturn(Optional.of(address));

        // THEN
        Address addressFound = addressService.findByUserEmail(userEmail);
        assertNotNull(addressFound);
        assertEquals(address.getId(), addressFound.getId());
        assertEquals(address.getStreet(), addressFound.getStreet());
        assertEquals(address.getCity(), addressFound.getCity());
        assertEquals(address.getNumber(), addressFound.getNumber());
        assertEquals(address.getZipCode(), addressFound.getZipCode());
        assertEquals(address.getState(), addressFound.getState());
        assertEquals(address.getUserId(), addressFound.getUserId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAddressDoesNotExist() throws ResourceNotFoundException {
        // GIVEN
        String userEmail = "userEmail";

        // WHEN
        when(addressRepository.findByUserEmail(userEmail)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> addressService.findByUserEmail(userEmail));
    }


}