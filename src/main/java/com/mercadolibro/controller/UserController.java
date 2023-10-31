package com.mercadolibro.controller;

import com.mercadolibro.entity.AppUserRole;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.dto.UserRegisterDTO;
import com.mercadolibro.service.UserService;
import com.mercadolibro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.headers.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    @ApiOperation(value = "Create a new user with default role", notes = "Returns the created user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "User created successfully", response = UserDTO.class),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 409, message = "User already exists"),
                    @ApiResponse(code = 404, message = "Role Not found")
            }
    )
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRegisterDTO userRegisterDTO) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        return new ResponseEntity<>(userService.create(userRegisterDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Get user by token", notes = "Returns the user", authorizations = {@Authorization(value = "JWT Token")})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "User found", response = UserDTO.class),
                    @ApiResponse(code = 401, message = "Unauthorized")
            }
    )
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request) throws ResourceNotFoundException {
        String token = request.getHeader("Authorization");
        if (token == null ) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        String jwt = token.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDTO user = userService.findByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/roles")
    @ApiOperation(value = "Get all roles", notes = "Returns all roles")
    public ResponseEntity<List<AppUserRole>> getAllRoles() {
        return new ResponseEntity<>(userService.findAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id", notes = "Returns the user")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "User found", response = UserDTO.class),
                    @ApiResponse(code = 404, message = "User not found")
            }
    )
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }
}
