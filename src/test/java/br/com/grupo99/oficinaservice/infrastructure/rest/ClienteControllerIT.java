package br.com.grupo99.oficinaservice.infrastructure.rest;
import br.com.grupo99.oficinaservice.application.dto.ClienteRequestDTO;
import br.com.grupo99.oficinaservice.domain.model.Cliente;
import br.com.grupo99.oficinaservice.domain.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Teste de Integração - ClienteController")
class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve criar um cliente e retornar 201 Created")
    @WithMockUser(username = "admin")
    void deveCriarCliente() throws Exception {
        ClienteRequestDTO request = new ClienteRequestDTO("Novo Cliente", "111.222.333-44", "11987654321", "novo@cliente.com");

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("Novo Cliente")));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao tentar criar cliente sem autenticação")
    void deveRetornarForbiddenAoCriarSemAuth() throws Exception {
        ClienteRequestDTO request = new ClienteRequestDTO("Cliente Fantasma", "111.222.333-44", "11987654321", "fantasma@cliente.com");

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve buscar um cliente pelo ID")
    @WithMockUser(username = "admin")
    void deveBuscarClientePorId() throws Exception {
        Cliente cliente = clienteRepository.save(new Cliente("Cliente Existente", "555.666.777-88"));

        mockMvc.perform(get("/api/v1/clientes/{id}", cliente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(cliente.getId().toString())))
                .andExpect(jsonPath("$.nome", is("Cliente Existente")));
    }
}
