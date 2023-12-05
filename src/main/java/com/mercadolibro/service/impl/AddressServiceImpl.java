package com.mercadolibro.service.impl;

import com.mercadolibro.dto.AddressCreateDTO;
import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.dto.mapper.AddressMapper;
import com.mercadolibro.entity.Address;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.repository.AddressRepository;
import com.mercadolibro.service.AddressService;
import com.mercadolibro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserService userService;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, UserService userService, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.addressMapper = addressMapper;
    }

    @Override
    public Address create(AddressCreateDTO address, String userEmail) throws ResourceNotFoundException, ResourceAlreadyExistsException {
        UserDTO userDTO = userService.findByEmail(userEmail);
        Integer addressId = addressRepository.findByUserId(userDTO.getId()).orElse(new Address()).getId();
        Address addressEntity = addressMapper.toAddress(address);
        addressEntity.setId(addressId);
        addressEntity.setUserId(userDTO.getId());
        return addressRepository.save(addressEntity);
    }

    @Override
    public Address findByUserEmail(String userEmail) throws ResourceNotFoundException {
        return addressRepository.findByUserEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    }
}
