package com.example.inventorymanager.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDirectory = Paths.get("./ProductPhoto");
        String uploadPath = uploadDirectory.toFile().getAbsolutePath();
        registry.addResourceHandler("/ProductPhoto/**").addResourceLocations("file:/" + uploadPath + "/");

        Path uploadDirectory2 = Paths.get("./Apply Photo");
        String uploadPath2 = uploadDirectory2.toFile().getAbsolutePath();
        registry.addResourceHandler("/Apply Photo/**").addResourceLocations("file:/" + uploadPath2 + "/");
    }
}
