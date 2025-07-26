package br.com.grupo99.oficinaservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste Unitário da Entidade Cliente")
class ClienteTest {

    @Test
    @DisplayName("Deve criar um cliente com nome e CPF/CNPJ")
    void deveCriarCliente() {
        Cliente cliente = new Cliente("João da Silva", "123.456.789-00");
        assertNotNull(cliente);
        assertEquals("João da Silva", cliente.getNome());
        assertEquals("123.456.789-00", cliente.getCpfCnpj());
        assertTrue(cliente.getVeiculos().isEmpty());
    }

    @Test
    @DisplayName("Deve adicionar um veículo à lista de veículos do cliente")
    void deveAdicionarVeiculo() {
        Cliente cliente = new Cliente("Maria Oliveira", "987.654.321-00");
        Veiculo veiculo = new Veiculo("ABC-1234", "Fiat", "Uno", 2010);

        // Em uma associação bidirecional, é importante setar os dois lados
        cliente.getVeiculos().add(veiculo);
        veiculo.setCliente(cliente);

        assertEquals(1, cliente.getVeiculos().size());
        assertEquals(veiculo, cliente.getVeiculos().get(0));
        assertEquals(cliente, veiculo.getCliente());
    }
}
