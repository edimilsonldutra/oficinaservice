package br.com.grupo99.oficinaservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste Unitário do Agregado OrdemServico")
class OrdemServicoTest {

    private Cliente cliente;
    private Veiculo veiculo;
    private OrdemServico os;

    @BeforeEach
    void setUp() {
        // Prepara os objetos de teste
        cliente = new Cliente("Cliente Teste", "12345678901");
        cliente.setId(UUID.randomUUID()); // Simula um ID que viria da base de dados

        veiculo = new Veiculo("ABC-1234", "Marca Teste", "Modelo Teste", 2023);
        veiculo.setId(UUID.randomUUID()); // Simula um ID que viria da base de dados

        // Cria a Ordem de Serviço com os IDs, conforme o novo modelo
        os = new OrdemServico(cliente.getId(), veiculo.getId());
    }

    @Test
    @DisplayName("Deve criar uma Ordem de Serviço com os IDs e status corretos")
    void deveCriarOrdemServicoComStatusInicialCorreto() {
        assertNotNull(os);
        assertEquals(StatusOS.RECEBIDA, os.getStatus());
        assertEquals(cliente.getId(), os.getClienteId());
        assertEquals(veiculo.getId(), os.getVeiculoId());
        assertEquals(BigDecimal.ZERO, os.getValorTotal());
    }

    @Test
    @DisplayName("Deve adicionar peça, baixar estoque e recalcular valor total")
    void deveAdicionarPecaCorretamente() {
        Peca peca = new Peca();
        peca.setNome("Filtro de Óleo");
        peca.setPreco(new BigDecimal("50.00"));
        peca.setEstoque(10);

        os.adicionarPeca(peca, 2);

        assertEquals(1, os.getPecas().size());
        assertEquals(8, peca.getEstoque());
        assertEquals(new BigDecimal("100.00"), os.getValorTotal());
    }

    @Test
    @DisplayName("Deve transitar corretamente pelos status até ser FINALIZADA")
    void deveTransitarStatusCorretamente() {
        assertEquals(StatusOS.RECEBIDA, os.getStatus());

        os.iniciarDiagnostico();
        assertEquals(StatusOS.EM_DIAGNOSTICO, os.getStatus());

        os.aguardarAprovacao();
        assertEquals(StatusOS.AGUARDANDO_APROVACAO, os.getStatus());

        os.aprovar();
        assertEquals(StatusOS.EM_EXECUCAO, os.getStatus());

        os.finalizar();
        assertEquals(StatusOS.FINALIZADA, os.getStatus());
        assertNotNull(os.getDataFinalizacao());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar uma OS que não está aguardando aprovação")
    void deveLancarExcecaoAoAprovarOsEmStatusIncorreto() {
        assertEquals(StatusOS.RECEBIDA, os.getStatus());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            os.aprovar();
        });

        assertEquals("OS não pode ser aprovada pois não está aguardando aprovação", exception.getMessage());
    }
}
