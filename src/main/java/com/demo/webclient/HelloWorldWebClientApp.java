package com.demo.webclient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@SpringBootApplication
public class HelloWorldWebClientApp {

    @Value("${api.base-url}")
    private String apiBaseUrl;

    @Value("${ssl.keystore.path}")
    private String keystorePath;

    @Value("${ssl.keystore.password}")
    private String keystorePassword;

    @Value("${ssl.truststore.path}")
    private String truststorePath;

    @Value("${ssl.truststore.password}")
    private String truststorePassword;

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldWebClientApp.class, args);
    }

    @Bean
    public WebClient webClient() throws Exception {
        // Configurar KeyStore (certificado del cliente)
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream keyStoreFile = new FileInputStream(keystorePath)) {
            keyStore.load(keyStoreFile, keystorePassword.toCharArray());
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

        // Configurar TrustStore (certificados CA)
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (FileInputStream trustStoreFile = new FileInputStream(truststorePath)) {
            trustStore.load(trustStoreFile, truststorePassword.toCharArray());
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        // Crear SSL Context con mTLS
        SslContext sslContext = SslContextBuilder
                .forClient()
                .keyManager(keyManagerFactory)
                .trustManager(trustManagerFactory)
                .build();

        // Configurar HttpClient con SSL
        HttpClient httpClient = HttpClient.create()
                .secure(sslSpec -> sslSpec.sslContext(sslContext));

        // Crear WebClient con mTLS configurado
        return WebClient.builder()
                .baseUrl(apiBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public CommandLineRunner run(WebClient webClient) {
        return args -> {
            System.out.println("\n=== Hello World con WebClient y mTLS ===\n");
            
            try {
                System.out.println("Conectando a: " + apiBaseUrl);
                System.out.println("Haciendo petición GET con mTLS...\n");
                
                String response = webClient
                        .get()
                        .uri("/")  // Endpoint raíz del API
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                
                System.out.println("Respuesta recibida:");
                System.out.println(response);
                System.out.println("\n=== Conexión mTLS exitosa ===\n");
                
            } catch (Exception e) {
                System.err.println("Error al conectar con mTLS:");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
