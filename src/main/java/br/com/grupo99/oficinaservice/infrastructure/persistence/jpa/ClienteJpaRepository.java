package br.com.grupo99.oficinaservice.infrastructure.persistence.jpa;

import br.com.grupo99.oficinaservice.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteJpaRepository extends JpaRepository<Cliente, UUID> {
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    boolean existsByCpfCnpj(String cpfCnpj);
}
