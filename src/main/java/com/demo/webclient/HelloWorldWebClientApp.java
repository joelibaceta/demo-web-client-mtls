package com.demo.webclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class HelloWorldWebClientApp {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldWebClientApp.class, args);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    @Bean
    public CommandLineRunner run(WebClient webClient) {
        return args -> {
            System.out.println("\n=== Hello World con WebClient ===\n");
            
            // Ejemplo 1: GET simple
            System.out.println("Haciendo petición GET...");
            String response = webClient
                    .get()
                    .uri("/posts/1")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            System.out.println("Respuesta recibida:");
            System.out.println(response);
            
            System.out.println("\n=== Petición completada con éxito ===\n");
        };
    }
}
