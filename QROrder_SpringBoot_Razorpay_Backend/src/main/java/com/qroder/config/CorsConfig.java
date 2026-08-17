package com.qroder.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class CorsConfig implements WebMvcConfigurer{
 @Value("${app.cors.allowed-origins}") String origins;
 public void addCorsMappings(CorsRegistry r){r.addMapping("/api/**").allowedOrigins(origins.split(",")).allowedMethods("GET","POST","PATCH","OPTIONS").allowedHeaders("*");}
}