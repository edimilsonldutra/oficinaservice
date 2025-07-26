package br.com.grupo99.oficinaservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Teste Unitário da Entidade Serviço")
class ServicoTest {

    @Test
    @DisplayName("Deve criar um serviço com descrição e preço")
    void deveCriarServico() {
        Servico servico = new Servico();
        servico.setDescricao("Alinhamento e Balanceamento");
        servico.setPreco(new BigDecimal("120.00"));

        assertNotNull(servico);
        assertEquals("Alinhamento e Balanceamento", servico.getDescricao());
        assertEquals(new BigDecimal("120.00"), servico.getPreco());
    }
}
