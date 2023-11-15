package com.example.tp_spring.controller;

import com.example.tp_spring.entity.TiersList;
import com.example.tp_spring.repository.TiersListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TiersListRepository tiersListRepository; // Assume you have a service to handle the data retrieval

    @GetMapping("/")
    public String showUserLists(Model model) {
        // Retrieve the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        // Retrieve user's lists from the repository
        List<TiersList> userLists = tiersListRepository.findByUserName(currentUserName);

        // Add the lists to the model
        model.addAttribute("userLists", userLists);

        boolean isAdmin = hasAdminRole(authentication);

        if(isAdmin){
            return "redirect:/admin-dashboard";
        }
        return "dashboard";
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

}
