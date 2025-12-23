#!/bin/bash
set -e

KEYS_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Leer passwords
if [ -f "$KEYS_DIR/pass" ]; then
    PASSWORD_DEV=$(grep "^dev=" "$KEYS_DIR/pass" | cut -d'=' -f2)
    PASSWORD_QA=$(grep "^qa=" "$KEYS_DIR/pass" | cut -d'=' -f2)
    PASSWORD_PROD=$(grep "^prod=" "$KEYS_DIR/pass" | cut -d'=' -f2)
else
    read -s -p "Password DEV: " PASSWORD_DEV && echo
    read -s -p "Password QA: " PASSWORD_QA && echo
    read -s -p "Password PROD: " PASSWORD_PROD && echo
fi

generate_keystore_for_env() {
    local ENV=$1
    local PASSWORD=$2
    
    openssl pkcs12 -export \
        -in "$KEYS_DIR/bn-$ENV.pem" \
        -inkey "$KEYS_DIR/bn-$ENV.key" \
        -out "$KEYS_DIR/client-keystore-$ENV.p12" \
        -name "client-cert-$ENV" \
        -passin "pass:$PASSWORD" \
        -password "pass:$PASSWORD" 2>/dev/null
    
    keytool -importkeystore \
        -srckeystore "$KEYS_DIR/client-keystore-$ENV.p12" \
        -srcstoretype PKCS12 \
        -srcstorepass "$PASSWORD" \
        -destkeystore "$KEYS_DIR/client-keystore-$ENV.jks" \
        -deststoretype JKS \
        -deststorepass "$PASSWORD" \
        -noprompt 2>/dev/null
    
    echo "✓ $ENV keystore generado"
}

generate_keystore_for_env "dev" "$PASSWORD_DEV"
generate_keystore_for_env "qa" "$PASSWORD_QA"
generate_keystore_for_env "prod" "$PASSWORD_PROD"

# Crear truststore
rm -f "$KEYS_DIR/client-truststore.jks"
csplit -z -f "$KEYS_DIR/cert-" -b "%02d.pem" "$KEYS_DIR/truststore.pem" '/-----BEGIN CERTIFICATE-----/' '{*}' 2>/dev/null || true

CERT_ALIAS=1
for cert_file in "$KEYS_DIR"/cert-*.pem; do
    [ -f "$cert_file" ] || continue
    keytool -import \
        -file "$cert_file" \
        -alias "ca-cert-$CERT_ALIAS" \
        -keystore "$KEYS_DIR/client-truststore.jks" \
        -storepass "$PASSWORD_DEV" \
        -noprompt 2>/dev/null
    CERT_ALIAS=$((CERT_ALIAS + 1))
    rm "$cert_file"
done

echo "✓ Truststore generado"
echo "✓ Keystores listos: dev, qa, prod"
