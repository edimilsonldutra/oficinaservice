package br.com.grupo99.oficinaservice.domain.repository;

import br.com.grupo99.oficinaservice.domain.model.Cliente;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(UUID id);
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    void deleteById(UUID id);
    List<Cliente> findAll();
    boolean existsById(UUID id);

    boolean existsByCpfCnpj(@NotBlank(message = "O CPF/CNPJ não pode ser vazio.") String s);
}
