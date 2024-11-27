package com.intern.ChatApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/F:/study/tryhard/uploads/");

        registry.addResourceHandler("/profile/**")
                .addResourceLocations("file:/F:/study/tryhard/profile/");
    }
}
