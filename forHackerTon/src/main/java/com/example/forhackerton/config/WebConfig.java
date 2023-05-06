package com.example.forhackerton.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    public void addCorsMappings(CorsRegistry registry) {
//        registry
//                .addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(false);
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/ImageSend/**, /KeyWord/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .maxAge(3600);
    }
}
