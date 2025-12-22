# Demo WebClient - Hello World

Un proyecto simple de ejemplo que demuestra cómo usar Spring WebClient para hacer peticiones HTTP.

## Requisitos

- Java 17 o superior
- Maven 3.6+

## Descripción

Este proyecto muestra un ejemplo básico de "Hello World" usando Spring WebClient. La aplicación hace una petición GET a una API pública (JSONPlaceholder) y muestra el resultado en la consola.

## Estructura del Proyecto

```
demo-webclient/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/demo/webclient/
│       │       └── HelloWorldWebClientApp.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

## Cómo Ejecutar

### Opción 1: Con Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Opción 2: Ejecutar el JAR

```bash
mvn clean package
java -jar target/demo-webclient-1.0.0.jar
```

## Qué Hace el Código

El programa:

1. Configura un **WebClient** con una URL base (`https://jsonplaceholder.typicode.com`)
2. Hace una petición **GET** a `/posts/1`
3. Recibe la respuesta y la muestra en la consola
4. Termina la ejecución

## Salida Esperada

```
=== Hello World con WebClient ===

Haciendo petición GET...
Respuesta recibida:
{
  "userId": 1,
  "id": 1,
  "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
  "body": "quia et suscipit..."
}

=== Petición completada con éxito ===
```

## Tecnologías Utilizadas

- **Spring Boot 3.2.0**
- **Spring WebFlux** (incluye WebClient)
- **Java 17**
- **Maven**

## Próximos Pasos

Para extender este proyecto, puedes:

- Agregar manejo de errores con `onStatus()`
- Implementar peticiones POST, PUT, DELETE
- Usar `exchange()` para acceso completo a la respuesta
- Agregar headers personalizados
- Implementar timeouts y reintentos
- Trabajar con flujos reactivos usando `Flux` y `Mono`

## Recursos

- [Documentación oficial de WebClient](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)
- [JSONPlaceholder API](https://jsonplaceholder.typicode.com/)
