package com.proyecto1.customerservice.integration;

import com.proyecto1.customerservice.domain.model.Cliente;
import com.proyecto1.customerservice.infrastructure.messaging.ClienteEventPublisher;
import com.proyecto1.customerservice.infrastructure.persistence.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClienteEndpointIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ClienteRepository clienteRepository;

        @MockBean
        private ClienteEventPublisher clienteEventPublisher;

        @Test
        void deberiaCrearCliente() throws Exception {
                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "nombre": "Jose Lema",
                                                  "genero": "Masculino",
                                                  "edad": 30,
                                                  "identificacion": "1234567890",
                                                  "direccion": "Otavalo sn y principal",
                                                  "telefono": "0987654321",
                                                  "clienteId": "CLI-001",
                                                  "contrasena": "1234",
                                                  "estado": true
                                                }
                                                """))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.message").value("Cliente creado exitosamente"))
                                .andExpect(jsonPath("$.data.nombre").value("Jose Lema"))
                                .andExpect(jsonPath("$.data.clienteId").value("CLI-001"))
                                .andExpect(jsonPath("$.data.estado").value(true))
                                .andExpect(jsonPath("$.data.contrasena").doesNotExist());
        }

        @Test
        void deberiaListarClientes() throws Exception {
                crearClienteJose();

                mockMvc.perform(get("/api/clientes"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data[0].nombre").value("Jose Lema"))
                                .andExpect(jsonPath("$.data[0].clienteId").value("CLI-001"));
        }

        @Test
        void deberiaConsultarClientePorId() throws Exception {
                crearClienteJose();

                Long clienteId = obtenerIdClientePorCodigo("CLI-001");

                mockMvc.perform(get("/api/clientes/" + clienteId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.id").value(clienteId))
                                .andExpect(jsonPath("$.data.nombre").value("Jose Lema"))
                                .andExpect(jsonPath("$.data.clienteId").value("CLI-001"));
        }

        @Test
        void deberiaActualizarCliente() throws Exception {
                crearClienteJose();

                Long clienteId = obtenerIdClientePorCodigo("CLI-001");

                mockMvc.perform(put("/api/clientes/" + clienteId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "nombre": "Jose Actualizado",
                                                  "genero": "Masculino",
                                                  "edad": 31,
                                                  "identificacion": "1234567890",
                                                  "direccion": "Nueva direccion",
                                                  "telefono": "0999999999",
                                                  "clienteId": "CLI-001",
                                                  "contrasena": "5678",
                                                  "estado": true
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.nombre").value("Jose Actualizado"))
                                .andExpect(jsonPath("$.data.edad").value(31))
                                .andExpect(jsonPath("$.data.direccion").value("Nueva direccion"))
                                .andExpect(jsonPath("$.data.telefono").value("0999999999"));
        }

        @Test
        void deberiaCambiarEstadoCliente() throws Exception {
                crearClienteJose();

                Long clienteId = obtenerIdClientePorCodigo("CLI-001");

                mockMvc.perform(patch("/api/clientes/" + clienteId + "/estado")
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
        void deberiaEliminarCliente() throws Exception {
                crearClienteJose();

                Long clienteId = obtenerIdClientePorCodigo("CLI-001");

                mockMvc.perform(delete("/api/clientes/" + clienteId))
                                .andExpect(status().isOk());

                mockMvc.perform(get("/api/clientes/" + clienteId))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deberiaRechazarClienteDuplicadoPorClienteId() throws Exception {
                crearClienteJose();

                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "nombre": "Otro Cliente",
                                                  "genero": "Masculino",
                                                  "edad": 40,
                                                  "identificacion": "9999999999",
                                                  "direccion": "Otra direccion",
                                                  "telefono": "3000000000",
                                                  "clienteId": "CLI-001",
                                                  "contrasena": "1234",
                                                  "estado": true
                                                }
                                                """))
                                .andExpect(status().isConflict());
        }

        private void crearClienteJose() throws Exception {
                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "nombre": "Jose Lema",
                                                  "genero": "Masculino",
                                                  "edad": 30,
                                                  "identificacion": "1234567890",
                                                  "direccion": "Otavalo sn y principal",
                                                  "telefono": "0987654321",
                                                  "clienteId": "CLI-001",
                                                  "contrasena": "1234",
                                                  "estado": true
                                                }
                                                """))
                                .andExpect(status().isCreated());
        }

        private Long obtenerIdClientePorCodigo(String codigoCliente) {
                Cliente cliente = clienteRepository.findByClienteId(codigoCliente)
                                .orElseThrow();

                return cliente.getId();
        }
}