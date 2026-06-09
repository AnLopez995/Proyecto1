#!/bin/sh

set -eu

CUSTOMER_SERVICE_URL="http://customer-service:8081"
ACCOUNT_SERVICE_URL="http://account-service:8082"

echo "=========================================="
echo "Iniciando carga inicial de datos"
echo "=========================================="

wait_for_service() {
  SERVICE_NAME="$1"
  SERVICE_URL="$2"

  echo "Esperando disponibilidad de $SERVICE_NAME..."

  ATTEMPTS=0
  MAX_ATTEMPTS=60

  until curl -fsS "$SERVICE_URL/actuator/health" > /dev/null 2>&1; do
    ATTEMPTS=$((ATTEMPTS + 1))

    if [ "$ATTEMPTS" -ge "$MAX_ATTEMPTS" ]; then
      echo "ERROR: $SERVICE_NAME no estuvo disponible después de $MAX_ATTEMPTS intentos."
      exit 1
    fi

    echo "$SERVICE_NAME aún no está disponible. Reintentando..."
    sleep 3
  done

  echo "$SERVICE_NAME disponible."
}

post_json() {
  DESCRIPTION="$1"
  URL="$2"
  BODY="$3"

  echo ""
  echo "------------------------------------------"
  echo "$DESCRIPTION"
  echo "POST $URL"
  echo "------------------------------------------"

  RESPONSE_FILE="/tmp/response.json"

  HTTP_STATUS=$(curl -sS -o "$RESPONSE_FILE" -w "%{http_code}" \
    -X POST "$URL" \
    -H "Content-Type: application/json" \
    -d "$BODY")

  echo "HTTP status: $HTTP_STATUS"
  echo "Response:"
  cat "$RESPONSE_FILE"
  echo ""

  if [ "$HTTP_STATUS" -lt 200 ] || [ "$HTTP_STATUS" -ge 300 ]; then
    echo "ERROR: Falló la operación: $DESCRIPTION"
    exit 1
  fi
}

get_json() {
  DESCRIPTION="$1"
  URL="$2"

  echo ""
  echo "------------------------------------------"
  echo "$DESCRIPTION"
  echo "GET $URL"
  echo "------------------------------------------"

  RESPONSE_FILE="/tmp/response-get.json"

  HTTP_STATUS=$(curl -sS -o "$RESPONSE_FILE" -w "%{http_code}" "$URL")

  echo "HTTP status: $HTTP_STATUS"
  echo "Response:"
  cat "$RESPONSE_FILE"
  echo ""

  if [ "$HTTP_STATUS" -lt 200 ] || [ "$HTTP_STATUS" -ge 300 ]; then
    echo "ERROR: Falló la consulta: $DESCRIPTION"
    exit 1
  fi
}

wait_for_service "customer-service" "$CUSTOMER_SERVICE_URL"
wait_for_service "account-service" "$ACCOUNT_SERVICE_URL"

echo ""
echo "Servicios disponibles. Iniciando creación de clientes..."

post_json "Creando cliente Jose Lema" "$CUSTOMER_SERVICE_URL/api/clientes" '{
  "nombre": "Jose Lema",
  "genero": "Masculino",
  "edad": 30,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "0987654321",
  "clienteId": "CLI-001",
  "contrasena": "1234",
  "estado": true
}'

post_json "Creando cliente Marianela Montalvo" "$CUSTOMER_SERVICE_URL/api/clientes" '{
  "nombre": "Marianela Montalvo",
  "genero": "Femenino",
  "edad": 28,
  "identificacion": "0987654321",
  "direccion": "Amazonas y NNUU",
  "telefono": "0999999999",
  "clienteId": "CLI-002",
  "contrasena": "5678",
  "estado": true
}'

post_json "Creando cliente Juan Osorio" "$CUSTOMER_SERVICE_URL/api/clientes" '{
  "nombre": "Juan Osorio",
  "genero": "Masculino",
  "edad": 28,
  "identificacion": "1122334455",
  "direccion": "13 junio y Equinoccial",
  "telefono": "098874587",
  "clienteId": "CLI-003",
  "contrasena": "abcd",
  "estado": true
}'

echo ""
echo "Esperando sincronización de eventos RabbitMQ hacia account-service..."
sleep 10

echo ""
echo "Verificando clientes en customer-service..."
get_json "Listar clientes" "$CUSTOMER_SERVICE_URL/api/clientes"

echo ""
echo "Creando cuentas..."

post_json "Creando cuenta 478758" "$ACCOUNT_SERVICE_URL/api/cuentas" '{
  "numeroCuenta": "478758",
  "tipoCuenta": "Ahorros",
  "saldoInicial": 2000.00,
  "estado": true,
  "clienteId": 1
}'

post_json "Creando cuenta 225487" "$ACCOUNT_SERVICE_URL/api/cuentas" '{
  "numeroCuenta": "225487",
  "tipoCuenta": "Corriente",
  "saldoInicial": 100.00,
  "estado": true,
  "clienteId": 2
}'

post_json "Creando cuenta 495878" "$ACCOUNT_SERVICE_URL/api/cuentas" '{
  "numeroCuenta": "495878",
  "tipoCuenta": "Ahorros",
  "saldoInicial": 0.00,
  "estado": true,
  "clienteId": 3
}'

echo ""
echo "Registrando movimientos..."

post_json "Registrando retiro en cuenta 478758" "$ACCOUNT_SERVICE_URL/api/movimientos" '{
  "numeroCuenta": "478758",
  "valor": -575.00
}'

post_json "Registrando depósito en cuenta 225487" "$ACCOUNT_SERVICE_URL/api/movimientos" '{
  "numeroCuenta": "225487",
  "valor": 600.00
}'

post_json "Registrando depósito en cuenta 495878" "$ACCOUNT_SERVICE_URL/api/movimientos" '{
  "numeroCuenta": "495878",
  "valor": 150.00
}'

echo ""
echo "Verificando cuentas..."
get_json "Listar cuentas" "$ACCOUNT_SERVICE_URL/api/cuentas"

echo ""
echo "Verificando movimientos..."
get_json "Listar movimientos" "$ACCOUNT_SERVICE_URL/api/movimientos"

echo ""
echo "=========================================="
echo "Carga inicial finalizada correctamente"
echo "=========================================="