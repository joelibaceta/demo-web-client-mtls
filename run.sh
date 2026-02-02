#!/bin/bash
set -e

# Cargar variables desde .env de forma segura
if [ -f .env ]; then
    echo "Cargando variables desde .env..."
    while IFS='=' read -r key value; do
        # Ignorar comentarios y líneas vacías
        if [[ ! "$key" =~ ^# && -n "$key" ]]; then
            # Remover comillas si existen
            value="${value%\"}"
            value="${value#\"}"
            export "$key=$value"
        fi
    done < <(grep -v '^#' .env | grep -v '^$')
fi

# Si no se cargó API_BASE_URL desde .env, intentar desde keys/pass (retrocompatibilidad)
if [ -z "$API_BASE_URL" ] && [ -f keys/pass ]; then
    echo "API_BASE_URL no encontrada en .env, usando keys/pass"
    ENV=${SPRING_PROFILES_ACTIVE:-dev}
    
    case $ENV in
      dev)
        export KEYSTORE_PASSWORD=$(grep "^dev=" keys/pass | cut -d'=' -f2)
        export TRUSTSTORE_PASSWORD=$(grep "^dev=" keys/pass | cut -d'=' -f2)
        ;;
      qa)
        export KEYSTORE_PASSWORD=$(grep "^qa=" keys/pass | cut -d'=' -f2)
        export TRUSTSTORE_PASSWORD=$(grep "^qa=" keys/pass | cut -d'=' -f2)
        ;;
      prod)
        export KEYSTORE_PASSWORD=$(grep "^prod=" keys/pass | cut -d'=' -f2)
        export TRUSTSTORE_PASSWORD=$(grep "^prod=" keys/pass | cut -d'=' -f2)
        ;;
    esac
fi

ENV=${SPRING_PROFILES_ACTIVE:-dev}

echo "Ejecutando con perfil: $ENV"
echo "API URL: $API_BASE_URL"
echo "Keystore: keys/client-keystore-$ENV.jks"
echo ""

mvn spring-boot:run -Dspring-boot.run.profiles=$ENV
