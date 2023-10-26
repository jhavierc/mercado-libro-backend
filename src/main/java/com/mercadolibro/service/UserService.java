package com.mercadolibro.service;

import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.dto.UserRegisterDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Creates a new user based on the provided UserRegistrationDTO and assigns the specified roles to the user.
     *
     * This method creates a new user account by taking the user's registration data in the form of a UserRegistrationDTO
     * and a list of role names to associate with the user. The user is then stored in the system, and the assigned roles
     * are granted to the user.
     *
     * @param userRegisterDTO containing the user's registration information.
     * @param roles A list of role names to assign to the newly created user.
     * @return A UserDTO representing the newly created user, including their unique identifier and associated information.
     * @throws ResourceAlreadyExistsException If a user with the same identifier or unique constraint already exists.
     * @throws ResourceNotFoundException If one or more roles provided do not exist in the system.
     */
    UserDTO create(UserRegisterDTO userRegisterDTO, List<String> roles) throws ResourceAlreadyExistsException, ResourceNotFoundException;

    /**
    * Creates a new user based on the provided UserRegistrationDTO.
    *
    * This method creates a new user account by taking the user's registration data in the form of a UserRegistrationDTO
    * the roles are assigned by default.
    * To configure default roles you should set the property "app.user.default-roles" in the application.yml file.
    *
    * @param userRegisterDTO containing the user's registration information.
    * @return A UserDTO representing the newly created user, including their unique identifier and associated information.
    * @throws ResourceAlreadyExistsException If a user with the same identifier or unique constraint already exists.
    * @throws ResourceNotFoundException If one or more roles provided do not exist in the system.
     */
    UserDTO create(UserRegisterDTO userRegisterDTO) throws ResourceAlreadyExistsException, ResourceNotFoundException;
}
