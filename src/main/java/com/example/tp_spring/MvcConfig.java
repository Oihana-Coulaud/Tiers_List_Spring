package com.example.tp_spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void  addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("dashboard");
        registry.addViewController("/account").setViewName("account");
        registry.addViewController("/modify-account").setViewName("modifyAccount");
        registry.addViewController("/modify-password").setViewName("updatePassword");
        registry.addViewController("/add-image").setViewName("uploadForm");
        registry.addViewController("/add-list").setViewName("addList");
    }

}
