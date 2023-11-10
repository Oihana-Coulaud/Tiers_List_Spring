package com.example.tp_spring.controller;

import com.example.tp_spring.entity.User;
import com.example.tp_spring.repository.UserRepository;
import com.example.tp_spring.service.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    UserRepository userRepository;
    private final UserDetail userDetailService;

    @Autowired
    public AccountController(UserDetail userDetailService) {
        this.userDetailService = userDetailService;
    }

    @GetMapping("/account")
        public String accountDetails(Model model) {
            UserDetails userDetails = userDetailService.loadUserByUsername(getLoggedInUsername());
            String username =  userDetails.getUsername();
            User user = userRepository.findByUsernameOrEmail(username, username);
            model.addAttribute("user", user);

            return "account";
        }


    // Helper method to get the username of the currently authenticated user
    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/modify-account")
    public String modifyAccount(
            @RequestParam int id,
            @RequestParam String username,
            @RequestParam String email,
            Model model
    ) {
        model.addAttribute("id", id);
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        return "modifyAccount";
    }

    @PostMapping("/update-user")
    public String updateAccount(
            @RequestParam int id,
            @RequestParam String username,
            @RequestParam String email,
            Model model) {

        User user = userRepository.findById(id);
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
        UserDetails userDetails;

        userDetails = userDetailService.loadUserByUsername(username);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                )
        );
        model.addAttribute("success", "Vos modifications ont bien était enregistré");
        return "redirect:/account";
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @PostMapping("/update-password")
    public String updatePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirm,
            Model model) {
        String username = getLoggedInUsername();
        User user = userRepository.findByUsernameOrEmail(username, username);

        if (user != null && currentPassword != null && newPassword != null && newPassword.equals(confirm)) {
            // Vérifier si le mot de passe actuel correspond
            if (userDetailService.checkPassword(user, currentPassword)) {
                // Mettre à jour le mot de passe
                user.setPassword(hashPassword(newPassword));
                userRepository.save(user);


                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                userDetails.getPassword(),
                                userDetails.getAuthorities()
                        )
                );

                model.addAttribute("success", "Votre mot de passe a été mis à jour avec succès");
                return "account";
            } else {
                model.addAttribute("error", "Le mot de passe actuel est incorrect");
                return "updatePassword";
            }
        } else {
            model.addAttribute("error", "Les mots de passe ne correspondent pas");
            return "updatePassword";
        }

    }
}
