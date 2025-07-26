package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.VeiculoRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.VeiculoResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.ResourceNotFoundException;
import br.com.grupo99.oficinaservice.domain.model.Cliente;
import br.com.grupo99.oficinaservice.domain.repository.ClienteRepository;
import br.com.grupo99.oficinaservice.domain.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("Teste de Integração - VeiculoApplicationService")
class VeiculoApplicationServiceIT {

    @Autowired
    private VeiculoApplicationService veiculoService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = clienteRepository.save(new Cliente("Dono de Veículo", "777.888.999-00"));
    }

    @Test
    @DisplayName("Deve criar um veículo para um cliente existente")
    void deveCriarVeiculo() {
        VeiculoRequestDTO request = new VeiculoRequestDTO("VEI-2024", "angshgu", "VW", "Nivus", 2024, cliente.getId());

        VeiculoResponseDTO response = veiculoService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.placa()).isEqualTo("VEI-2024");
     //   assertThat(veiculoRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar veículo para cliente inexistente")
    void deveLancarExcecaoAoCriarVeiculoParaClienteInexistente() {
        VeiculoRequestDTO request = new VeiculoRequestDTO("VEI-FAIL", "test", "Marca", "Modelo", 2024, UUID.randomUUID());

        assertThrows(ResourceNotFoundException.class, () -> veiculoService.create(request));
    }
}
