package com.example.tp_spring.controller;

import com.example.tp_spring.entity.Role;
import com.example.tp_spring.entity.User;
import com.example.tp_spring.repository.RoleRepository;
import com.example.tp_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @PostMapping(path = "/registration")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
            return "register";
        }

        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Utilisateur déjà enregistré avec cet email ou ce nom d'utilisateur.");
            return "register";
        }
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }

        User u = new User(email, username, password);
        u.setRole(userRole);
        userRepository.save(u);

        model.addAttribute("success", "Utilisateur ajouté avec succès.");
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
