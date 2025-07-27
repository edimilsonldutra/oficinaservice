package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.ServicoRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.ServicoResponseDTO;
import br.com.grupo99.oficinaservice.domain.model.Servico;
import br.com.grupo99.oficinaservice.domain.repository.ServicoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Teste de Integração Completo - ServicoApplicationService")
class ServicoApplicationServiceIT {

    @Autowired
    private ServicoApplicationService servicoService;

    @Autowired
    private ServicoRepository servicoRepository;

    @Test
    @DisplayName("Deve criar um novo serviço")
    void deveCriarServico() {
        ServicoRequestDTO request = new ServicoRequestDTO("Alinhamento", new BigDecimal("150.00"));
        ServicoResponseDTO response = servicoService.create(request);
        assertThat(response).isNotNull();
        assertThat(response.descricao()).isEqualTo("Alinhamento");
    }

    @Test
    @DisplayName("Deve listar todos os serviços")
    void deveListarServicos() {
        servicoRepository.save(createServico("Serviço A"));
        servicoRepository.save(createServico("Serviço B"));
        List<ServicoResponseDTO> servicos = servicoService.getAll();
        assertThat(servicos).hasSize(2);
    }

    private Servico createServico(String desc) {
        Servico servico = new Servico();
        servico.setDescricao(desc);
        servico.setPreco(BigDecimal.TEN);
        return servico;
    }
}