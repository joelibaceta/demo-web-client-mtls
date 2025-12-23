#!/bin/bash
set -e

# Cargar passwords desde archivo pass
if [ ! -f keys/pass ]; then
    echo "Error: Archivo keys/pass no encontrado"
    exit 1
fi

# Cargar URLs desde .env
if [ -f .env ]; then
    export $(grep -v '^#' .env | grep API_BASE_URL | xargs)
    export $(grep -v '^#' .env | grep SPRING_PROFILES_ACTIVE | xargs)
fi

ENV=${SPRING_PROFILES_ACTIVE:-dev}

# Cargar passwords y URLs según ambiente
case $ENV in
  dev)
    export KEYSTORE_PASSWORD=$(grep "^dev=" keys/pass | cut -d'=' -f2)
    export TRUSTSTORE_PASSWORD=$(grep "^dev=" keys/pass | cut -d'=' -f2)
    export API_BASE_URL=${API_BASE_URL_DEV}
    ;;
  qa)
    export KEYSTORE_PASSWORD=$(grep "^qa=" keys/pass | cut -d'=' -f2)
    export TRUSTSTORE_PASSWORD=$(grep "^qa=" keys/pass | cut -d'=' -f2)
    export API_BASE_URL=${API_BASE_URL_QA}
    ;;
  prod)
    export KEYSTORE_PASSWORD=$(grep "^prod=" keys/pass | cut -d'=' -f2)
    export TRUSTSTORE_PASSWORD=$(grep "^prod=" keys/pass | cut -d'=' -f2)
    export API_BASE_URL=${API_BASE_URL_PROD}
    ;;
esac

echo "✓ Ejecutando con perfil: $ENV"
echo "✓ API URL: $API_BASE_URL"
mvn spring-boot:run -Dspring-boot.run.profiles=$ENV
