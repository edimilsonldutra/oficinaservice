package br.com.grupo99.oficinaservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Teste Unitário da Entidade Veículo")
class VeiculoTest {

    @Test
    @DisplayName("Deve criar um veículo com seus atributos principais")
    void deveCriarVeiculo() {
        Veiculo veiculo = new Veiculo("XYZ-9876", "Chevrolet", "Onix", 2020);
        assertNotNull(veiculo);
        assertEquals("XYZ-9876", veiculo.getPlaca());
        assertEquals("Chevrolet", veiculo.getMarca());
        assertEquals("Onix", veiculo.getModelo());
        assertEquals(2020, veiculo.getAno());
    }
}
