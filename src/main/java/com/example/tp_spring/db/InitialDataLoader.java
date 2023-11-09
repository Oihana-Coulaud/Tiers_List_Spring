package com.example.tp_spring.db;

import com.example.tp_spring.entity.Role;
import com.example.tp_spring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public InitialDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        long roleCount = roleRepository.count();

        if (roleCount == 0) {
            Role roleA = new Role("ROLE_USER");
            Role roleB = new Role("ROLE_ADMIN");

            roleRepository.saveAll(List.of(roleA, roleB));
        }
    }
}