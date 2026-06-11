package com.proyecto1.accountservice.integration;

import com.proyecto1.accountservice.domain.model.ClienteReadModel;
import com.proyecto1.accountservice.domain.model.Cuenta;
import com.proyecto1.accountservice.infrastructure.persistence.ClienteReadModelRepository;
import com.proyecto1.accountservice.infrastructure.persistence.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CuentaMovimientoReporteIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ClienteReadModelRepository clienteReadModelRepository;

        @Autowired
        private CuentaRepository cuentaRepository;

        @Test
        void deberiaCrearListarConsultarActualizarYCambiarEstadoCuenta() throws Exception {
                crearClienteReadModel(1L, "CLI-001", "Jose Lema", "1234567890", true);

                crearCuenta("478758", "Ahorros", "2000.00", true, 1L);

                mockMvc.perform(get("/api/cuentas"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data[0].numeroCuenta").value("478758"))
                                .andExpect(jsonPath("$.data[0].tipoCuenta").value("Ahorros"));

                Cuenta cuentaCreada = cuentaRepository.findByNumeroCuenta("478758")
                                .orElseThrow();

                Long cuentaId = cuentaCreada.getId();

                mockMvc.perform(get("/api/cuentas/" + cuentaId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.numeroCuenta").value("478758"))
                                .andExpect(jsonPath("$.data.saldoInicial").value(2000.00))
                                .andExpect(jsonPath("$.data.saldoDisponible").value(2000.00));

                mockMvc.perform(put("/api/cuentas/" + cuentaId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "478758",
                                                  "tipoCuenta": "Corriente",
                                                  "saldoInicial": 3000.00,
                                                  "estado": true,
                                                  "clienteId": 1
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.tipoCuenta").value("Corriente"))
                                .andExpect(jsonPath("$.data.saldoInicial").value(3000.00));

                mockMvc.perform(patch("/api/cuentas/" + cuentaId + "/estado")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "estado": false
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.estado").value(false));
        }

        @Test
        void deberiaRegistrarDepositoYRetiroActualizandoSaldo() throws Exception {
                crearClienteReadModel(1L, "CLI-001", "Jose Lema", "1234567890", true);
                crearCuenta("478758", "Ahorros", "2000.00", true, 1L);

                mockMvc.perform(post("/api/movimientos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "478758",
                                                  "valor": 600.00
                                                }
                                                """))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.data.tipoMovimiento").value("DEPOSITO"))
                                .andExpect(jsonPath("$.data.valor").value(600.00))
                                .andExpect(jsonPath("$.data.saldo").value(2600.00));

                mockMvc.perform(post("/api/movimientos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "478758",
                                                  "valor": -575.00
                                                }
                                                """))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.data.tipoMovimiento").value("RETIRO"))
                                .andExpect(jsonPath("$.data.valor").value(-575.00))
                                .andExpect(jsonPath("$.data.saldo").value(2025.00));

                Cuenta cuenta = cuentaRepository.findByNumeroCuenta("478758")
                                .orElseThrow();

                assertEquals(0, new BigDecimal("2025.00").compareTo(cuenta.getSaldoDisponible()));
        }

        @Test
        void deberiaRechazarRetiroCuandoSaldoNoEstaDisponible() throws Exception {
                crearClienteReadModel(2L, "CLI-002", "Marianela Montalvo", "0987654321", true);
                crearCuenta("225487", "Corriente", "100.00", true, 2L);

                mockMvc.perform(post("/api/movimientos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "225487",
                                                  "valor": -500.00
                                                }
                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Saldo no disponible"));

                Cuenta cuenta = cuentaRepository.findByNumeroCuenta("225487")
                                .orElseThrow();

                assertEquals(0, new BigDecimal("100.00").compareTo(cuenta.getSaldoDisponible()));
        }

        @Test
        void deberiaRechazarMovimientoConValorCero() throws Exception {
                crearClienteReadModel(3L, "CLI-003", "Juan Osorio", "1122334455", true);
                crearCuenta("495878", "Ahorros", "0.00", true, 3L);

                mockMvc.perform(post("/api/movimientos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "495878",
                                                  "valor": 0.00
                                                }
                                                """))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void deberiaRechazarMovimientoCuandoCuentaEstaInactiva() throws Exception {
                crearClienteReadModel(4L, "CLI-004", "Cliente Inactivo", "4444444444", true);
                crearCuenta("111222", "Ahorros", "500.00", false, 4L);

                mockMvc.perform(post("/api/movimientos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "111222",
                                                  "valor": 100.00
                                                }
                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("La cuenta 111222 se encuentra inactiva"));
        }

        @Test
        void deberiaGenerarReporteEstadoCuenta() throws Exception {
                crearClienteReadModel(5L, "CLI-005", "Pedro Perez", "5566778899", true);
                crearCuenta("333444", "Ahorros", "1000.00", true, 5L);

                mockMvc.perform(post("/api/movimientos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "333444",
                                                  "valor": 250.00
                                                }
                                                """))
                                .andExpect(status().isCreated());

                String fechaInicio = LocalDate.now().minusDays(1).toString();
                String fechaFin = LocalDate.now().plusDays(1).toString();

                mockMvc.perform(get("/api/reportes")
                                .param("clienteId", "5")
                                .param("fechaInicio", fechaInicio)
                                .param("fechaFin", fechaFin))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.clienteId").value(5))
                                .andExpect(jsonPath("$.data.nombre").value("Pedro Perez"))
                                .andExpect(jsonPath("$.data.cuentas[0].numeroCuenta").value("333444"));
        }

        @Test
        void deberiaRechazarCuentaParaClienteInexistente() throws Exception {
                mockMvc.perform(post("/api/cuentas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "777888",
                                                  "tipoCuenta": "Ahorros",
                                                  "saldoInicial": 1000.00,
                                                  "estado": true,
                                                  "clienteId": 999
                                                }
                                                """))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deberiaRechazarCuentaDuplicadaPorNumeroCuenta() throws Exception {
                crearClienteReadModel(6L, "CLI-006", "Cliente Duplicado", "6666666666", true);
                crearCuenta("999000", "Ahorros", "1000.00", true, 6L);

                mockMvc.perform(post("/api/cuentas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "999000",
                                                  "tipoCuenta": "Corriente",
                                                  "saldoInicial": 2000.00,
                                                  "estado": true,
                                                  "clienteId": 6
                                                }
                                                """))
                                .andExpect(status().isConflict());
        }

        private void crearClienteReadModel(
                        Long clienteId,
                        String codigoCliente,
                        String nombre,
                        String identificacion,
                        Boolean estado) {
                ClienteReadModel cliente = new ClienteReadModel(
                                clienteId,
                                codigoCliente,
                                nombre,
                                identificacion,
                                estado);

                clienteReadModelRepository.save(cliente);
        }

        private void crearCuenta(
                        String numeroCuenta,
                        String tipoCuenta,
                        String saldoInicial,
                        Boolean estado,
                        Long clienteId) throws Exception {
                mockMvc.perform(post("/api/cuentas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "numeroCuenta": "%s",
                                                  "tipoCuenta": "%s",
                                                  "saldoInicial": %s,
                                                  "estado": %s,
                                                  "clienteId": %d
                                                }
                                                """.formatted(numeroCuenta, tipoCuenta, saldoInicial, estado,
                                                clienteId)))
                                .andExpect(status().isCreated());
        }
}