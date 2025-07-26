package br.com.grupo99.oficinaservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Teste Unitário da Entidade ItemPeca")
class ItemPecaTest {

    @Test
    @DisplayName("Deve criar um item de peça e calcular o valor total corretamente")
    void deveCriarItemPecaECalcularTotal() {
        Peca peca = new Peca();
        peca.setNome("Pastilha de Freio");
        peca.setPreco(new BigDecimal("90.50"));

        ItemPeca itemPeca = new ItemPeca(peca, 2);

        assertNotNull(itemPeca);
        assertEquals(peca, itemPeca.getPeca());
        assertEquals(2, itemPeca.getQuantidade());
        assertEquals(new BigDecimal("90.50"), itemPeca.getValorUnitario());
        assertEquals(new BigDecimal("181.00"), itemPeca.getValorTotal());
    }
}
