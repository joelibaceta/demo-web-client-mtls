# Demo WebClient con mTLS

Aplicación Spring Boot que demuestra cómo conectarse a una API usando **mTLS** (autenticación mutua TLS) con Spring WebClient.

## Configuración Rápida

### 1. Configurar Contraseñas

Crea el archivo `keys/pass` con las contraseñas de los keystores:

```bash
cp keys/pass.example keys/pass
```

Edita `keys/pass` con tus contraseñas en formato:
```
dev=your-dev-password
qa=your-qa-password
prod=your-prod-password
```

### 2. Configurar URLs

```bash
cp .env.example .env
```

Edita `.env` con las URLs de tus ambientes:
```bash
SPRING_PROFILES_ACTIVE=dev

API_BASE_URL_DEV=https://your-api-dev.com
API_BASE_URL_QA=https://your-api-qa.com
API_BASE_URL_PROD=https://your-api-prod.com
```

### 3. Generar Keystores

Coloca tus certificados en `keys/` y ejecuta:

```bash
cd keys
./generate-keystores.sh
```

El script espera:
- `bn-{env}.pem` - Certificado del cliente por ambiente
- `bn-{env}.key` - Clave privada por ambiente
- `truststore.pem` - Certificados CA

Genera:
- `client-keystore-{env}.jks` - Keystores del cliente
- `client-truststore.jks` - Truststore común

## Ejecución

```bash
# Ejecutar con el ambiente del .env
./run.sh

# O especificar ambiente
SPRING_PROFILES_ACTIVE=qa ./run.sh
SPRING_PROFILES_ACTIVE=prod ./run.sh
```

## Estructura de Archivos

```
keys/
├── bn-{env}.key            # Claves privadas (no subir)
├── bn-{env}.pem            # Certificados (no subir)
├── truststore.pem          # CAs (no subir)
├── pass                    # Passwords (no subir)
├── pass.example            # Plantilla de passwords
├── generate-keystores.sh   # Script generador
├── client-keystore-*.jks   # Generados (no subir)
└── client-truststore.jks   # Generado (no subir)
```

