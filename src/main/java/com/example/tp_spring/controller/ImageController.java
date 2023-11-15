package com.example.tp_spring.controller;

import com.example.tp_spring.entity.Image;
import com.example.tp_spring.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ImageController {

    @Autowired
    private ImageRepository imageRepository; // Assuming you have a service to handle image-related operations

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {

        List<Image> createdImages = imageRepository.getImagesByStatus("created");
        model.addAttribute("images", createdImages);
        return "adminDashboard";
    }
}