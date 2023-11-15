package com.example.tp_spring.controller;

import com.example.tp_spring.entity.Image;
import com.example.tp_spring.repository.ImageRepository;
import com.example.tp_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;


    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {

        List<Image> createdImages = imageRepository.getImagesByStatus("created");
        model.addAttribute("images", createdImages);
        return "adminDashboard";
    }

        @PostMapping("/accept-img")
        public String acceptImage(
                @RequestParam Integer imageId,
                @RequestParam(name = "reject", defaultValue = "false") boolean reject,
                Model model) {

            if (reject) {
                imageRepository.updateStatusById(imageId, "rejected");
                model.addAttribute("success", "Image rejetée");
                return "adminDashboard";
            }
            imageRepository.updateStatusById(imageId, "validated");
            model.addAttribute("success", "Image acceptée");
            return "adminDashboard";
        }
}