package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.PecaRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.PecaResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.ResourceNotFoundException;
import br.com.grupo99.oficinaservice.domain.model.Peca;
import br.com.grupo99.oficinaservice.domain.repository.PecaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Teste de Integração Completo - PecaApplicationService")
class PecaApplicationServiceIT {

    @Autowired
    private PecaApplicationService pecaService;

    @Autowired
    private PecaRepository pecaRepository;

    @Test
    @DisplayName("Deve criar uma nova peça")
    void deveCriarPeca() {
        PecaRequestDTO request = new PecaRequestDTO("Filtro de Óleo", "Mann Filter", new BigDecimal("35.50"), 20);
        PecaResponseDTO response = pecaService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo("Filtro de Óleo");
        assertThat(pecaRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Deve adicionar estoque a uma peça existente")
    void deveAdicionarEstoque() {
        Peca peca = createPeca("Vela", 10);
        peca = pecaRepository.save(peca);

        PecaResponseDTO response = pecaService.adicionarEstoque(peca.getId(), 5);

        assertThat(response.estoque()).isEqualTo(15);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar estoque a peça inexistente")
    void deveLancarExcecaoAoAdicionarEstoquePecaInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> pecaService.adicionarEstoque(UUID.randomUUID(), 5));
    }

    @Test
    @DisplayName("Deve buscar uma peça pelo ID")
    void deveBuscarPecaPorId() {
        Peca peca = createPeca("Amortecedor", 8);
        peca = pecaRepository.save(peca);

        PecaResponseDTO response = pecaService.getById(peca.getId());
        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo("Amortecedor");
    }

    @Test
    @DisplayName("Deve atualizar uma peça existente")
    void deveAtualizarPeca() {
        Peca peca = pecaRepository.save(createPeca("Nome Antigo", 10));
        PecaRequestDTO request = new PecaRequestDTO("Nome Novo", "Novo Fabricante", new BigDecimal("99.99"), 50);

        PecaResponseDTO response = pecaService.update(peca.getId(), request);

        assertThat(response.nome()).isEqualTo("Nome Novo");
        assertThat(response.estoque()).isEqualTo(50);
    }

    @Test
    @DisplayName("Deve deletar uma peça existente")
    void deveDeletarPeca() {
        Peca peca = pecaRepository.save(createPeca("Peca para Deletar", 1));
        UUID pecaId = peca.getId();

        pecaService.delete(pecaId);

        assertThat(pecaRepository.findById(pecaId)).isEmpty();
    }

    private Peca createPeca(String nome, int estoque) {
        Peca peca = new Peca();
        peca.setNome(nome);
        peca.setEstoque(estoque);
        peca.setPreco(BigDecimal.ONE);
        return peca;
    }
}