package com.mercadolibro.service;

import com.mercadolibro.dto.UserUpdateDTO;
import com.mercadolibro.entity.AppUserRole;
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

    /**
     * Finds a user by their email address.
     * @param email The email address of the user to find.
     * @return A UserDTO representing the user found.
     * @throws ResourceNotFoundException If no user with the specified email address exists.
     * @see UserDTO
     */
    UserDTO findByEmail(String email) throws ResourceNotFoundException;

    /**
     * Finds a user by their id.
     * @param id The id of the user to find.
     * @return A UserDTO representing the user found.
     * @throws ResourceNotFoundException If no user with the specified id exists.
     */
    UserDTO findById(Integer id) throws ResourceNotFoundException;

    /**
     * Find all roles
     * @return A list of all roles
     */
    List<AppUserRole> findAllRoles();

    /**
     * Find all users
     * @return A list of all users
     */
    List<UserDTO> findAll();

    /**
     * Update a user
     * @param userId The id of the user to update
     * @param userUpdateDTO The user to update
     * @return The updated user
     * @throws ResourceNotFoundException If no user with the specified id exists.
     * @throws ResourceNotFoundException If one or more roles provided do not exist in the system.
     */
    UserDTO update(UserUpdateDTO userUpdateDTO, Integer userId) throws ResourceNotFoundException;

}
