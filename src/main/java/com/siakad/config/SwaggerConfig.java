package com.siakad.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityRequirement;

import java.util.List;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .info(new Info()
                        .title("SIMAKAD API")
                        .version("1.0")
                        .description("API Sistem Informasi Manajemen Akademik (SIMAKAD) untuk mengelola data mahasiswa, mata kuliah, nilai, dan jadwal"))
                .servers(List.of(
                        new Server().url("http://localhost:8081/api/v1").description("Localhost Server"),
                        new Server().url("https://backend-simakad.azurewebsites.net/api/v1").description("Production Server"),
                        new Server().url("http://103.158.196.106/spring/api/v1").description("Siakad Server")
                ));
    }
}
