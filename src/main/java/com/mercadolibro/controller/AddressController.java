package com.mercadolibro.controller;

import com.mercadolibro.dto.AddressCreateDTO;
import com.mercadolibro.entity.Address;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.service.AddressService;
import com.mercadolibro.util.Util;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH })
public class AddressController {
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @ApiOperation(value = "Create a new address", notes = "Returns the created address", authorizations = {@Authorization(value = "JWT Token")})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Address created successfully", response = Address.class),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 404, message = "User Not found")
            }
    )
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody AddressCreateDTO addressCreateDTO) throws ResourceNotFoundException, ResourceAlreadyExistsException {
        return new ResponseEntity<>(addressService.create(addressCreateDTO, Util.getUserEmail()), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get the user address", authorizations = {@Authorization(value = "JWT Token")})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Address found", response = Address.class),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 404, message = "Address not found")
            }
    )
    @GetMapping
    public ResponseEntity<Address> getAddress() throws ResourceNotFoundException {
        return new ResponseEntity<>(addressService.findByUserEmail(Util.getUserEmail()), HttpStatus.OK);
    }

}
