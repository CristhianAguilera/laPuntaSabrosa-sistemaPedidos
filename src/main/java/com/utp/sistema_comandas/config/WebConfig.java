package com.utp.sistema_comandas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class WebConfig implements WebMvcConfigurer{
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("file:css/");
        
        registry.addResourceHandler("/js/**")
                .addResourceLocations("file:js/");

        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:image/");
    }
    
}
