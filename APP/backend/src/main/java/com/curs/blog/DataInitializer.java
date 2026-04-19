package com.curs.blog;

import com.curs.blog.entity.ERole;
import com.curs.blog.entity.Role;
import com.curs.blog.entity.User;
import com.curs.blog.repository.RoleRepository;
import com.curs.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Инициализация ролей
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            roleRepository.save(new Role(ERole.ROLE_EDITOR));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
            System.out.println("Roles initialized: USER, EDITOR, ADMIN");
        }

        // 2. Создание дефолтного администратора
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@blog.com", encoder.encode("admin123"));
            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            admin.setRoles(roles);
            userRepository.save(admin);
            System.out.println("Default admin created: admin / admin123");
        }
    }
}
