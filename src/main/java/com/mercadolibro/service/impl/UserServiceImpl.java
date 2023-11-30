package com.mercadolibro.service.impl;

import com.mercadolibro.dto.*;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.dto.mapper.UserMapper;
import com.mercadolibro.entity.AppUser;
import com.mercadolibro.entity.AppUserRole;
import com.mercadolibro.repository.AppUserRepository;
import com.mercadolibro.repository.AppUserRoleRepository;
import com.mercadolibro.repository.InvoiceRepository;
import com.mercadolibro.service.EmailService;
import com.mercadolibro.service.UserService;
import com.mercadolibro.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    /* Variables */
    private final List<String> defaultRoles;

    /**
     * Key: code value: user email
     */
    private Map<String, String> activeCodes = new HashMap<>();
    private Set<String> usersWithResetCode = new HashSet<>();

    /* Dependencies */
    private final AppUserRepository appUserRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final InvoiceRepository invoiceRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(AppUserRepository appUserRepository, AppUserRoleRepository appUserRoleRepository, @Value("${app.user.default-roles}") List<String> defaultRoles, InvoiceRepository invoiceRepository, EmailService emailService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appUserRoleRepository = appUserRoleRepository;
        this.defaultRoles = defaultRoles;
        this.invoiceRepository = invoiceRepository;
        this.emailService = emailService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO create(UserRegisterDTO userRegisterDTO, List<String> roles) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        if (appUserRepository.existsByEmail(userRegisterDTO.getEmail())) throw new ResourceAlreadyExistsException("User already exists");

        AppUser appUser = userMapper.toAppUser(userRegisterDTO);
        appUser.setRoles(getRoles(roles));
        appUser.setStatus("ACTIVE");
        appUser.setDateCreated(LocalDateTime.now());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        return userMapper.toUserDTO(appUserRepository.save(appUser));
    }

    @Override
    public UserDTO create(UserRegisterDTO userRegisterDTO) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        UserDTO user = create(userRegisterDTO, defaultRoles);
        emailService.sendEmail(user.getEmail(), "¡Bienvenido a MercadoLibro!", user.getName(), List.of(
                "Gracias por registrarte",
                "Te damos la bienvenida a la mejor plataforma para comprar libros de todo el mundo",
                "Esperamos que disfrutes de tu experiencia"
        ));
        return user;
    }

    @Override
    public UserDTO findByEmail(String email) throws ResourceNotFoundException {
        return userMapper.toUserDTO(appUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User " + email + " not found")));
    }

    @Override
    public UserDTO findById(Integer id) throws ResourceNotFoundException {
        return userMapper.toUserDTO(appUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found")));
    }

    @Override
    public List<AppUserRole> findAllRoles() {
        return appUserRoleRepository.findAll();
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.toUserDTOs(appUserRepository.findAll());
    }

    @Override
    public UserDTO update(UserUpdateDTO userUpdateDTO, Integer userId) throws ResourceNotFoundException {
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        if (userUpdateDTO.getPassword() != null ) userUpdateDTO.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        if (userUpdateDTO.getRoles() != null) {
            List<AppUserRole> roles = getRoles(userUpdateDTO.getRoles().stream().map(AppUserRole::getDescription).collect(Collectors.toList()));
            userUpdateDTO.setRoles(roles);
        }
        Util.mergeObjects(userUpdateDTO, appUser);

        return userMapper.toUserDTO(appUserRepository.save(appUser));
    }

    @Override
    public PageDTO<UserDTO> find(UserQuery userQuery, @Positive Integer page, @Positive Integer size) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<AppUser> example = Example.of(
                AppUser.builder().
                    name(userQuery.getName()).
                    lastName(userQuery.getLastName()).
                    email(userQuery.getEmail()).
                    status(userQuery.getStatus()).
                    build(),
                matcher);

        Page<UserDTO> userPage = appUserRepository.findAll(example, PageRequest.of(page, size, userQuery.getOrderDirection(), userQuery.getOrderBy().getValue())).map(userMapper::toUserDTO);
        return new PageDTO<UserDTO> (
                userPage.getContent(),
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userPage.getNumber(),
                userPage.getSize()
        );
    }

    private List<AppUserRole> getRoles(List<String> roles) throws ResourceNotFoundException {
        List<AppUserRole> appUserRoles = new ArrayList<>();
        for (String role: roles) {
            appUserRoles.add(appUserRoleRepository.findByDescription(role).orElseThrow(() -> new ResourceNotFoundException("Role " + role + " not found")));
        }

        return appUserRoles;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(appUser.getRoles().stream().map(AppUserRole::getDescription).toArray(String[]::new)).build();
    }

    @Override
    public String generateResetCode(String email) {

        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // Números aleatorios del 0 al 9
        }
        activeCodes.put(code.toString(), email);

        Timer codeTimer = new Timer();

        codeTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        activeCodes.remove(code.toString());
                        codeTimer.cancel();
                    }
                }, 120000
        );
        return code.toString();


    }

    @Override
    public void sendResetCode(String email) throws ResourceNotFoundException {
        UserDTO user = findByEmail(email);
        String code = generateResetCode(email);
        emailService.sendEmail(email, "Recupera tu contraseña de Mercado Libro", user.getName(), List.of(
                "Tu código de recuperación es: " + code
        ));
    }

    @Override
    public void resetPassword(String code, String newPassword) throws ResourceNotFoundException {
        String email = activeCodes.get(code);
        if (email == null ) throw new ResourceNotFoundException("Code " + code + " not found");
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User " + email + " not found"));
        appUser.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(appUser);
        activeCodes.remove(code);

    }

    @Override
    public List<String> findAddressesByEmail(String email) {
        return invoiceRepository.findInvoicesAddressByUserEmail(email);
    }
}