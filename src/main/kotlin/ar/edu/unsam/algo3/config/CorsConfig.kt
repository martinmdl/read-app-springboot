package ar.edu.unsam.algo3.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class CorsConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // Permite todas las rutas
            .allowedOrigins("http://localhost:4200", "http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
            .allowCredentials(true) // Si necesitas enviar cookies o credenciales
    }
}