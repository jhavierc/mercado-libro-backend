package com.mercadolibro.service;

import com.mercadolibro.dto.AddressCreateDTO;
import com.mercadolibro.entity.Address;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;

public interface AddressService {
    Address create(AddressCreateDTO address, String userEmail) throws ResourceNotFoundException, ResourceAlreadyExistsException;
    Address findByUserEmail(String userEmail) throws ResourceNotFoundException;
}
