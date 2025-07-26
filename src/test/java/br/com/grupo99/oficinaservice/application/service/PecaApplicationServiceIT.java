package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.PecaRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.PecaResponseDTO;
import br.com.grupo99.oficinaservice.domain.model.Peca;
import br.com.grupo99.oficinaservice.domain.repository.PecaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Teste de Integração - PecaApplicationService")
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
    //    assertThat(pecaRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve adicionar estoque a uma peça existente")
    void deveAdicionarEstoque() {
        Peca peca = new Peca();
        peca.setEstoque(10);
        peca.setPreco(BigDecimal.TEN);
        peca = pecaRepository.save(peca);

        PecaResponseDTO response = pecaService.adicionarEstoque(peca.getId(), 5);

        assertThat(response.estoque()).isEqualTo(15);
    }
}
