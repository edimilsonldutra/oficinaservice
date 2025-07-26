package br.com.grupo99.oficinaservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Teste Unitário da Entidade Peça")
class PecaTest {

    private Peca peca;

    @BeforeEach
    void setUp() {
        peca = new Peca();
        peca.setNome("Vela de Ignição");
        peca.setFabricante("NGK");
        peca.setPreco(new BigDecimal("45.00"));
        peca.setEstoque(10);
    }

    @Test
    @DisplayName("Deve adicionar quantidade ao estoque com sucesso")
    void deveAdicionarEstoque() {
        peca.adicionarEstoque(5);
        assertEquals(15, peca.getEstoque());
    }

    @Test
    @DisplayName("Deve baixar quantidade do estoque com sucesso")
    void deveBaixarEstoque() {
        peca.baixarEstoque(3);
        assertEquals(7, peca.getEstoque());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar baixar mais do que o estoque disponível")
    void deveLancarExcecaoPorEstoqueInsuficiente() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            peca.baixarEstoque(11);
        });
        assertEquals("Estoque insuficiente.", exception.getMessage());
    }
}
