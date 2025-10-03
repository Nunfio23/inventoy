package com.ginventory.inventory.CONFIG;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos los endpoints
                .allowedOrigins("*") // Permite cualquier origen (cualquier IP)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite los métodos HTTP comunes
                .allowedHeaders("*") // Permite cualquier cabecera
                .allowCredentials(false) // No requiere credenciales (cookies, etc.)
                .maxAge(3600); // Duración de la caché de pre-vuelo (OPTIONS request)
    }
}
