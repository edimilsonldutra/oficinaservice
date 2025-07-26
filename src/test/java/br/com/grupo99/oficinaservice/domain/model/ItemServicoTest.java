package br.com.grupo99.oficinaservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Teste Unitário da Entidade ItemServico")
class ItemServicoTest {

    @Test
    @DisplayName("Deve criar um item de serviço e calcular o valor total corretamente")
    void deveCriarItemServicoECalcularTotal() {
        Servico servico = new Servico();
        servico.setDescricao("Troca de Óleo");
        servico.setPreco(new BigDecimal("250.00"));

        ItemServico itemServico = new ItemServico(servico, 1);

        assertNotNull(itemServico);
        assertEquals(servico, itemServico.getServico());
        assertEquals(1, itemServico.getQuantidade());
        assertEquals(new BigDecimal("250.00"), itemServico.getValorUnitario());
        assertEquals(new BigDecimal("250.00"), itemServico.getValorTotal());
    }
}
