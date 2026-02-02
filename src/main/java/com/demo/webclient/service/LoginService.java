package com.demo.webclient.service;

import com.demo.webclient.model.LoginRequest;
import com.demo.webclient.model.LoginResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LoginService {

    private final WebClient webClient;
    private final Dotenv dotenv;

    @Autowired
    public LoginService(WebClient webClient, Dotenv dotenv) {
        this.webClient = webClient;
        this.dotenv = dotenv;
    }

    /**
     * Obtiene las credenciales desde el archivo .env
     */
    public LoginRequest getCredentialsFromEnv() {
        String username = dotenv.get("LOGIN_USERNAME", "bn_qa");
        String clientname = dotenv.get("LOGIN_CLIENTNAME", "cred001");
        String password = dotenv.get("LOGIN_PASSWORD", "11##aa..");

        return new LoginRequest(username, clientname, password);
    }

    /**
     * Realiza el login usando las credenciales del .env
     * Conecta al API mediante mTLS y obtiene un token de autenticación
     */
    public Mono<LoginResponse> login() {
        LoginRequest credentials = getCredentialsFromEnv();
        String loginEndpoint = dotenv.get("LOGIN_ENDPOINT", "/auth/login");

        System.out.println("\n=== PRUEBA DE LOGIN CON mTLS ===");
        System.out.println("Credenciales cargadas desde .env:");
        System.out.println("   Usuario: " + credentials.getUsername());
        System.out.println("   Cliente: " + credentials.getClientname());
        System.out.println("   Endpoint: " + loginEndpoint);
        System.out.println("Conectando con certificado mTLS...\n");

        return webClient
                .post()
                .uri(loginEndpoint)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .bodyValue(credentials)
                .exchangeToMono(response -> {
                    System.out.println("Status: " + response.statusCode());
                    System.out.println("Headers: " + response.headers().asHttpHeaders());
                    
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(LoginResponse.class);
                    } else {
                        return response.bodyToMono(String.class)
                                .doOnNext(body -> {
                                    System.err.println("Respuesta del servidor (" + response.statusCode() + "): " + body);
                                })
                                .then(Mono.error(new RuntimeException("Error " + response.statusCode() + " en login")));
                    }
                })
                .doOnSuccess(response -> {
                    if (response.isSuccess()) {
                        System.out.println("Login exitoso!");
                        System.out.println("Access Token: " + 
                            (response.getAccessToken() != null ? 
                                response.getAccessToken().substring(0, Math.min(30, response.getAccessToken().length())) + "..." : 
                                "null"));
                        System.out.println("Expira en: " + response.getExpiresIn() + " segundos");
                        System.out.println("Token Type: " + response.getTokenType());
                        System.out.println("Scope: " + response.getScope());
                    } else {
                        System.err.println("Login fallido: No se recibió access_token");
                    }
                })
                .doOnError(error -> {
                    System.err.println("Error en login: " + error.getMessage());
                    error.printStackTrace();
                })
                .onErrorResume(error -> {
                    System.err.println("Retornando respuesta de error");
                    return Mono.just(new LoginResponse(null, null, null, null));
                });
    }

    /**
     * Realiza login con credenciales específicas (útil para testing)
     */
    public Mono<LoginResponse> login(String username, String clientname, String password) {
        LoginRequest credentials = new LoginRequest(username, clientname, password);
        String loginEndpoint = dotenv.get("LOGIN_ENDPOINT", "/auth/login");

        return webClient
                .post()
                .uri(loginEndpoint)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .bodyValue(credentials)
                .retrieve()
                .bodyToMono(LoginResponse.class);
    }
}
