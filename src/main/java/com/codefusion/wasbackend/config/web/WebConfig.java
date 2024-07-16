package com.codefusion.wasbackend.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:5170",
                                "http://localhost:5171",
                                "http://localhost:5172",
                                "http://localhost:5173",
                                "http://localhost:5174",
                                "http://localhost:5175",
                                "http://localhost:5176",
                                "http://localhost:5177",
                                "http://localhost:5178",
                                "http://localhost:5179",
                                "http://localhost:5180"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
