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
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("Teste de Integração - ClienteApplicationService")
class ClienteApplicationServiceIT {

    @Autowired
    private ClienteApplicationService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve criar um novo cliente com sucesso")
    void deveCriarCliente() {
        ClienteRequestDTO request = new ClienteRequestDTO("Cliente Teste", "12345678900", "11999998888", "teste@email.com");

        ClienteResponseDTO response = clienteService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo("Cliente Teste");
        assertThat(clienteRepository.findById(response.id())).isPresent();
    }

    @Test
    @DisplayName("Deve atualizar um cliente existente")
    void deveAtualizarCliente() {
        Cliente clienteExistente = clienteRepository.save(new Cliente("Antigo Nome", "98765432100"));
        ClienteRequestDTO request = new ClienteRequestDTO("Novo Nome", "98765432100", "11222223333", "novo@email.com");

        ClienteResponseDTO response = clienteService.update(clienteExistente.getId(), request);

        assertThat(response.id()).isEqualTo(clienteExistente.getId());
        assertThat(response.nome()).isEqualTo("Novo Nome");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar buscar cliente inexistente")
    void deveLancarExcecaoAoBuscarClienteInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.getById(java.util.UUID.randomUUID());
        });
    }
}
