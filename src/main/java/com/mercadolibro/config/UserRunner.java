package com.mercadolibro.config;

import com.mercadolibro.entity.AppUserRole;
import com.mercadolibro.repository.AppUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;

@Configuration
public class UserRunner implements ApplicationRunner {

    // Variables

    /* True to create roles */
    @Value("${app.create-roles}")
    boolean createRoles = true;

    /* Roles to create (if create roles is true) */
    @Value("${app.roles}")
    private String[] roles;

    // Dependencies
    private final AppUserRoleRepository appUserRoleRepository;

    @Autowired
    public UserRunner(AppUserRoleRepository appUserRoleRepository) {
        this.appUserRoleRepository = appUserRoleRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createRoles();
    }

    private void createRoles() {
        if (!createRoles) return;
        for (String role: roles) {
            AppUserRole appUserRole = appUserRoleRepository.findByDescription(role).orElse(new AppUserRole());
            appUserRole.setDescription(role);
            appUserRole.setStatus("ACTIVE");
            appUserRoleRepository.save(appUserRole);
        }
    }
}
