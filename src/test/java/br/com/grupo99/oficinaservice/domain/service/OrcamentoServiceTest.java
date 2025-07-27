package br.com.grupo99.oficinaservice.domain.service;

import br.com.grupo99.oficinaservice.domain.model.Peca;
import br.com.grupo99.oficinaservice.domain.model.Servico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Teste Unitário - OrcamentoService")
class OrcamentoServiceTest {

    private OrcamentoService orcamentoService;

    @BeforeEach
    void setUp() {
        orcamentoService = new OrcamentoService();
    }

    @Test
    @DisplayName("Deve calcular o orçamento corretamente com peças e serviços")
    void deveCalcularOrcamentoCorretamente() {
        Peca peca1 = new Peca();
        peca1.setPreco(new BigDecimal("100.00"));
        Servico servico1 = new Servico();
        servico1.setPreco(new BigDecimal("150.50"));

        BigDecimal total = orcamentoService.calcularOrcamento(List.of(peca1), List.of(servico1));

        assertEquals(new BigDecimal("250.50"), total);
    }

    @Test
    @DisplayName("Deve retornar zero para listas vazias")
    void deveRetornarZeroParaListasVazias() {
        BigDecimal total = orcamentoService.calcularOrcamento(Collections.emptyList(), Collections.emptyList());
        assertEquals(BigDecimal.ZERO, total);
    }
}

