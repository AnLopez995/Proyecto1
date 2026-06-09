#!/bin/sh

set -eu

CUSTOMER_SERVICE_URL="http://customer-service:8081"
ACCOUNT_SERVICE_URL="http://account-service:8082"

echo "=========================================="
echo "Iniciando carga inicial de datos bancarios"
echo "=========================================="

echo "Esperando customer-service..."
until curl -s "$CUSTOMER_SERVICE_URL/actuator/health" | grep -q "UP"; do
  echo "customer-service aún no está listo..."
  sleep 3
done

echo "Esperando account-service..."
until curl -s "$ACCOUNT_SERVICE_URL/actuator/health" | grep -q "UP"; do
  echo "account-service aún no está listo..."
  sleep 3
done

echo "Servicios disponibles. Iniciando creación de clientes..."

curl -s -X POST "$CUSTOMER_SERVICE_URL/api/clientes" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Jose Lema",
    "genero": "Masculino",
    "edad": 35,
    "identificacion": "1234567890",
    "direccion": "Otavalo sn y principal",
    "telefono": "098254785",
    "clienteId": "CLI-001",
    "contrasena": "1234",
    "estado": true
  }'

echo ""

curl -s -X POST "$CUSTOMER_SERVICE_URL/api/clientes" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Marianela Montalvo",
    "genero": "Femenino",
    "edad": 30,
    "identificacion": "0987654321",
    "direccion": "Amazonas y NNUU",
    "telefono": "097548965",
    "clienteId": "CLI-002",
    "contrasena": "5678",
    "estado": true
  }'

echo ""

curl -s -X POST "$CUSTOMER_SERVICE_URL/api/clientes" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Osorio",
    "genero": "Masculino",
    "edad": 28,
    "identificacion": "1122334455",
    "direccion": "13 junio y Equinoccial",
    "telefono": "098874587",
    "clienteId": "CLI-003",
    "contrasena": "1245",
    "estado": true
  }'

echo ""

echo "Esperando propagación de eventos RabbitMQ hacia account-service..."
sleep 8

echo "Creando cuentas..."

curl -s -X POST "$ACCOUNT_SERVICE_URL/api/cuentas" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "478758",
    "tipoCuenta": "Ahorros",
    "saldoInicial": 2000.00,
    "estado": true,
    "clienteId": 1
  }'

echo ""

curl -s -X POST "$ACCOUNT_SERVICE_URL/api/cuentas" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "225487",
    "tipoCuenta": "Corriente",
    "saldoInicial": 100.00,
    "estado": true,
    "clienteId": 2
  }'

echo ""

curl -s -X POST "$ACCOUNT_SERVICE_URL/api/cuentas" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "495878",
    "tipoCuenta": "Ahorros",
    "saldoInicial": 0.00,
    "estado": true,
    "clienteId": 3
  }'

echo ""

echo "Registrando movimientos iniciales..."

curl -s -X POST "$ACCOUNT_SERVICE_URL/api/movimientos" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "478758",
    "valor": -575.00
  }'

echo ""

curl -s -X POST "$ACCOUNT_SERVICE_URL/api/movimientos" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "225487",
    "valor": 600.00
  }'

echo ""

curl -s -X POST "$ACCOUNT_SERVICE_URL/api/movimientos" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "495878",
    "valor": 150.00
  }'

echo ""

echo "=========================================="
echo "Carga inicial finalizada"
echo "=========================================="

echo "Clientes:"
curl -s "$CUSTOMER_SERVICE_URL/api/clientes"
echo ""

echo "Cuentas:"
curl -s "$ACCOUNT_SERVICE_URL/api/cuentas"
echo ""

echo "Movimientos:"
curl -s "$ACCOUNT_SERVICE_URL/api/movimientos"
echo ""