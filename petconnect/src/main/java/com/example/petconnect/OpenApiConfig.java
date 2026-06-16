package com.example.petconnect;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Petconnect API")
                        .version("1.0")
                        .description("API do sistema Petconnect para gerenciamento de pets.")
                        .contact(new Contact()
                                .name("Suporte Petconnect")));
    }
}