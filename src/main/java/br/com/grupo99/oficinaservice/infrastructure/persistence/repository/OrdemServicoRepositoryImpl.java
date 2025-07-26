package br.com.grupo99.oficinaservice.infrastructure.persistence.repository;

import br.com.grupo99.oficinaservice.domain.model.OrdemServico;
import br.com.grupo99.oficinaservice.domain.repository.OrdemServicoRepository;
import br.com.grupo99.oficinaservice.infrastructure.persistence.jpa.OrdemServicoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrdemServicoRepositoryImpl implements OrdemServicoRepository {

    private final OrdemServicoJpaRepository jpaRepository;

    public OrdemServicoRepositoryImpl(OrdemServicoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public OrdemServico save(OrdemServico ordemServico) {
        return jpaRepository.save(ordemServico);
    }

    @Override
    public Optional<OrdemServico> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<OrdemServico> findAll() {
        return jpaRepository.findAll();
    }
}
