package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.OrdemServicoRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.OrdemServicoResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.BusinessException;
import br.com.grupo99.oficinaservice.application.exception.ResourceNotFoundException;
import br.com.grupo99.oficinaservice.domain.model.*;
import br.com.grupo99.oficinaservice.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Teste de Integração Completo - OrdemServicoApplicationService")
class OrdemServicoApplicationServiceIT {

    @Autowired
    private OrdemServicoApplicationService osService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private PecaRepository pecaRepository;
    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private OrdemServicoRepository osRepository;

    private Cliente cliente;
    private Veiculo veiculo;
    private Peca peca;
    private Servico servico;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Cliente OS", "333.333.333-33");
        veiculo = new Veiculo("OS-1234", "Ford", "Ka", 2018);
        veiculo.setCliente(cliente);
        cliente.getVeiculos().add(veiculo);
        cliente = clienteRepository.save(cliente);
        veiculo = cliente.getVeiculos().get(0);

        peca = new Peca();
        peca.setEstoque(10);
        peca.setPreco(new BigDecimal("100.00"));
        peca.setNome("Peca Teste");
        peca = pecaRepository.save(peca);

        servico = new Servico();
        servico.setPreco(new BigDecimal("200.00"));
        servico.setDescricao("Servico Teste");
        servico = servicoRepository.save(servico);
    }

    @Test
    @DisplayName("Deve criar uma Ordem de Serviço com sucesso")
    void deveCriarOsComSucesso() {
        OrdemServicoRequestDTO request = new OrdemServicoRequestDTO(
                cliente.getCpfCnpj(),
                veiculo.getPlaca(),
                List.of(servico.getId()),
                List.of(peca.getId())
        );

        OrdemServicoResponseDTO response = osService.execute(request);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(StatusOS.RECEBIDA);
        assertThat(response.valorTotal()).isEqualByComparingTo(new BigDecimal("300.00"));
        assertThat(osRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar OS com peça inexistente")
    void deveLancarExcecaoComPecaInexistente() {
        OrdemServicoRequestDTO request = new OrdemServicoRequestDTO(
                cliente.getCpfCnpj(),
                veiculo.getPlaca(),
                Collections.emptyList(),
                List.of(UUID.randomUUID())
        );

        assertThrows(ResourceNotFoundException.class, () -> osService.execute(request));
    }

    @Test
    @DisplayName("Deve atualizar o status de uma OS corretamente")
    void deveAtualizarStatusDaOs() {
        OrdemServico os = osRepository.save(new OrdemServico(cliente, veiculo));

        osService.execute(os.getId(), StatusOS.EM_DIAGNOSTICO);
        osService.execute(os.getId(), StatusOS.AGUARDANDO_APROVACAO);
        OrdemServicoResponseDTO response = osService.execute(os.getId(), StatusOS.EM_EXECUCAO);

        assertThat(response.status()).isEqualTo(StatusOS.EM_EXECUCAO);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar uma transição de status inválida")
    void deveLancarExcecaoEmTransicaoDeStatusInvalida() {
        OrdemServico os = osRepository.save(new OrdemServico(cliente, veiculo));

        assertThrows(BusinessException.class, () -> osService.execute(os.getId(), StatusOS.EM_EXECUCAO));
    }
}
