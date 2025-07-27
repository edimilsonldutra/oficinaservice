package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.ClienteRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.ClienteResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.ResourceNotFoundException;
import br.com.grupo99.oficinaservice.domain.model.Cliente;
import br.com.grupo99.oficinaservice.domain.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Teste de Integração Completo - ClienteApplicationService")
class ClienteApplicationServiceIT {

    @Autowired
    private ClienteApplicationService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve criar um novo cliente com sucesso")
    void deveCriarCliente() {
        ClienteRequestDTO request = new ClienteRequestDTO("Cliente de Teste", "123.456.789-00", "11999998888", "teste@email.com");
        ClienteResponseDTO response = clienteService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo("Cliente de Teste");
        assertTrue(clienteRepository.findById(response.id()).isPresent());
    }

    @Test
    @DisplayName("Deve buscar um cliente pelo ID")
    void deveBuscarClientePorId() {
        Cliente cliente = clienteRepository.save(new Cliente("Cliente para Busca", "111.111.111-11"));
        ClienteResponseDTO response = clienteService.getById(cliente.getId());

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(cliente.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar cliente por ID inexistente")
    void deveLancarExcecaoAoBuscarClienteInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> clienteService.getById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve listar todos os clientes")
    void deveListarTodosClientes() {
        clienteRepository.save(new Cliente("Cliente A", "222.222.222-22"));
        clienteRepository.save(new Cliente("Cliente B", "333.333.333-33"));
        List<ClienteResponseDTO> clientes = clienteService.getAll();
        assertThat(clientes).hasSize(2);
    }

    @Test
    @DisplayName("Deve atualizar um cliente existente")
    void deveAtualizarCliente() {
        Cliente clienteExistente = clienteRepository.save(new Cliente("Nome Antigo", "444.444.444-44"));
        ClienteRequestDTO request = new ClienteRequestDTO("Nome Novo", "444.444.444-44", "11222223333", "novo@email.com");

        ClienteResponseDTO response = clienteService.update(clienteExistente.getId(), request);

        assertThat(response.id()).isEqualTo(clienteExistente.getId());
        assertThat(response.nome()).isEqualTo("Nome Novo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar cliente inexistente")
    void deveLancarExcecaoAoAtualizarClienteInexistente() {
        ClienteRequestDTO request = new ClienteRequestDTO("Nome", "cpf", "tel", "email");
        assertThrows(ResourceNotFoundException.class, () -> clienteService.update(UUID.randomUUID(), request));
    }

    @Test
    @DisplayName("Deve deletar um cliente existente")
    void deveDeletarCliente() {
        Cliente cliente = clienteRepository.save(new Cliente("Cliente a ser Deletado", "555.555.555-55"));
        UUID clienteId = cliente.getId();

        assertDoesNotThrow(() -> clienteService.delete(clienteId));
        assertFalse(clienteRepository.existsById(clienteId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar cliente inexistente")
    void deveLancarExcecaoAoDeletarClienteInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> clienteService.delete(UUID.randomUUID()));
    }
}