package cl.bci.users.v1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userRegistrationOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API creacion de usuarios (Rodrigo pino) — Ejercicio JAVA BCI")
                .description("Registro RESTful con validacion de correo y clave por "
                        + "regex configurable, token JWT HS256 persistido y errores "
                        + "en formato {\"mensaje\": \"...\"}.")
                .version("0.0.1-SNAPSHOT")
                .contact(new Contact().name("Rodrigo Pino")));
    }

}
