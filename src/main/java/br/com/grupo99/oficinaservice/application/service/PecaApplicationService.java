package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.PecaRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.PecaResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.ResourceNotFoundException;
import br.com.grupo99.oficinaservice.application.usecase.GerenciarPecaUseCase;
import br.com.grupo99.oficinaservice.domain.model.Peca;
import br.com.grupo99.oficinaservice.domain.repository.PecaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PecaApplicationService implements GerenciarPecaUseCase {

    private final PecaRepository pecaRepository;

    public PecaApplicationService(PecaRepository pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    @Override
    @Transactional
    public PecaResponseDTO create(PecaRequestDTO requestDTO) {
        Peca peca = new Peca();
        peca.setNome(requestDTO.nome());
        peca.setFabricante(requestDTO.fabricante());
        peca.setPreco(requestDTO.preco());
        peca.setEstoque(requestDTO.estoque());
        return PecaResponseDTO.fromDomain(pecaRepository.save(peca));
    }

    @Override
    @Transactional
    public PecaResponseDTO update(UUID id, PecaRequestDTO requestDTO) {
        Peca peca = pecaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Peça não encontrada com o id: " + id));

        peca.setNome(requestDTO.nome());
        peca.setFabricante(requestDTO.fabricante());
        peca.setPreco(requestDTO.preco());
        peca.setEstoque(requestDTO.estoque());

        return PecaResponseDTO.fromDomain(pecaRepository.save(peca));
    }

    @Override
    @Transactional(readOnly = true)
    public PecaResponseDTO getById(UUID id) {
        Peca peca = pecaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Peça não encontrada com o id: " + id));
        return PecaResponseDTO.fromDomain(peca);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PecaResponseDTO> getAll() {
        return pecaRepository.findAll().stream()
                .map(PecaResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!pecaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Peça não encontrada com o id: " + id);
        }
        pecaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PecaResponseDTO adicionarEstoque(UUID id, int quantidade) {
        Peca peca = pecaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Peça não encontrada com o id: " + id));

        peca.adicionarEstoque(quantidade);

        return PecaResponseDTO.fromDomain(pecaRepository.save(peca));
    }
}
