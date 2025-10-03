package com.newsroom.config;

import com.newsroom.enums.RoleType;
import com.newsroom.model.Role;
import com.newsroom.model.User;
import com.newsroom.repository.RoleRepository;
import com.newsroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Database initialization completed!");
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();
        if (countRoles == 0) {
            Role role1 = new Role();
            role1.setRoleType(RoleType.ADMIN);

            Role role2 = new Role();
            role2.setRoleType(RoleType.MODERATOR);

            Role role3 = new Role();
            role3.setRoleType(RoleType.AUTHOR);

            Role role4 = new Role();
            role4.setRoleType(RoleType.VIEWER);

            List<Role> roles = Arrays.asList(role1, role2, role3, role4);
            roles.forEach(this.roleRepository::save);
        }
        if (countUsers == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@admin.com");
            admin.setFullName("Admin");
            admin.setPhone("0123456789");
            admin.setAge(21);
            admin.setAvatarUrl(null);
            admin.setRole(RoleType.ADMIN.toString().toUpperCase());

            User moderator = new User();
            moderator.setUsername("moderator");
            moderator.setPassword(passwordEncoder.encode("moderator"));
            moderator.setEmail("moderator@moderator.com");
            moderator.setFullName("Moderator");
            moderator.setPhone("0123456788");
            moderator.setAge(22);
            moderator.setAvatarUrl(null);
            moderator.setRole(RoleType.MODERATOR.toString().toUpperCase());

            User author = new User();
            author.setUsername("author");
            author.setPassword(passwordEncoder.encode("author"));
            author.setEmail("author@author.com");
            author.setFullName("Author");
            author.setPhone("0123456781");
            author.setAge(21);
            author.setAvatarUrl(null);
            author.setRole(RoleType.AUTHOR.toString().toUpperCase());

            User viewer = new User();
            viewer.setUsername("viewer");
            viewer.setPassword(passwordEncoder.encode("viewer"));
            viewer.setEmail("viewer@viewer.com");
            viewer.setFullName("Viewer");
            viewer.setPhone("0123456780");
            viewer.setAge(21);
            viewer.setAvatarUrl(null);
            viewer.setRole(RoleType.VIEWER.toString().toUpperCase());

            List<User> users = Arrays.asList(admin, moderator, author, viewer);
            users.forEach(this.userRepository::save);
        }

        if (countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else {
            System.out.println(">>> END INIT DATABASE");
        }
    }
}
