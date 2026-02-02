package com.demo.webclient.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @Bean
    public Dotenv dotenv() {
        try {
            return Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo cargar el archivo .env: " + e.getMessage());
            return Dotenv.configure()
                    .ignoreIfMissing()
                    .ignoreIfMalformed()
                    .load();
        }
    }
}
