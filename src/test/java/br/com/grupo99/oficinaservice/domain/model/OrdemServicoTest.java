package br.com.grupo99.oficinaservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste Unitário do Agregado OrdemServico")
class OrdemServicoTest {

    private Cliente cliente;
    private Veiculo veiculo;
    private OrdemServico os;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Cliente Teste", "12345678901");
        veiculo = new Veiculo("ABC-1234", "Marca Teste", "Modelo Teste", 2023);
        os = new OrdemServico(cliente, veiculo);
    }

    @Test
    @DisplayName("Deve criar uma Ordem de Serviço com status RECEBIDA")
    void deveCriarOrdemServicoComStatusInicialCorreto() {
        assertNotNull(os);
        assertEquals(StatusOS.RECEBIDA, os.getStatus());
        assertEquals(cliente, os.getCliente());
        assertEquals(veiculo, os.getVeiculo());
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
    @DisplayName("Deve lançar exceção ao tentar adicionar peça sem estoque")
    void deveLancarExcecaoAoAdicionarPecaSemEstoque() {
        Peca peca = new Peca();
        peca.setNome("Bateria");
        peca.setPreco(new BigDecimal("350.00"));
        peca.setEstoque(1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            os.adicionarPeca(peca, 2);
        });

        assertEquals("Estoque insuficiente para a peça: Bateria", exception.getMessage());
    }

    @Test
    @DisplayName("Deve adicionar serviço e recalcular valor total")
    void deveAdicionarServicoCorretamente() {
        Servico servico = new Servico();
        servico.setDescricao("Troca de óleo");
        servico.setPreco(new BigDecimal("150.00"));

        os.adicionarServico(servico, 1);

        assertEquals(1, os.getServicos().size());
        assertEquals(new BigDecimal("150.00"), os.getValorTotal());
    }

    @Test
    @DisplayName("Deve adicionar múltiplos itens e calcular o valor total corretamente")
    void deveCalcularValorTotalComMultiplosItens() {
        Servico servico = new Servico();
        servico.setDescricao("Revisão Completa");
        servico.setPreco(new BigDecimal("500.00"));
        os.adicionarServico(servico, 1);

        Peca peca = new Peca();
        peca.setNome("Correia Dentada");
        peca.setPreco(new BigDecimal("120.00"));
        peca.setEstoque(5);
        os.adicionarPeca(peca, 1);

        assertEquals(new BigDecimal("620.00"), os.getValorTotal());
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

