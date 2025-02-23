    package com.example.tp_spring.db;

    import com.example.tp_spring.entity.Role;
    import com.example.tp_spring.entity.User;
    import com.example.tp_spring.repository.RoleRepository;
    import com.example.tp_spring.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.stereotype.Component;

    import java.util.List;

    @Component
    public class InitialDataLoader implements CommandLineRunner {

        private final RoleRepository roleRepository;
        private final UserRepository userRepository;

        @Autowired
        public InitialDataLoader(RoleRepository roleRepository, UserRepository userRepository) {
            this.roleRepository = roleRepository;
            this.userRepository = userRepository;
        }

        @Override
        public void run(String... args) {
            long roleCount = roleRepository.count();

            if (roleCount == 0) {
                Role roleA = new Role("ROLE_USER");
                Role roleB = new Role("ROLE_ADMIN");

                roleRepository.saveAll(List.of(roleA, roleB));
            }
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            User existingAdmin = userRepository.findByUsername("admin");
            if (existingAdmin == null) {
                User adminUser = new User("admin@admin.fr", "admin", "admin");
                adminUser.setRole(adminRole);
                userRepository.save(adminUser);
            }
        }
    }