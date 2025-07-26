package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.ServicoRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.ServicoResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.ResourceNotFoundException;
import br.com.grupo99.oficinaservice.application.usecase.GerenciarServicoUseCase;
import br.com.grupo99.oficinaservice.domain.model.Servico;
import br.com.grupo99.oficinaservice.domain.repository.ServicoRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServicoApplicationService implements GerenciarServicoUseCase {

    private final ServicoRepository servicoRepository;

    public ServicoApplicationService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @Override
    @Transactional
    public ServicoResponseDTO create(ServicoRequestDTO requestDTO) {
        Servico servico = new Servico();
        servico.setDescricao(requestDTO.descricao());
        servico.setPreco(requestDTO.preco());
        return ServicoResponseDTO.fromDomain(servicoRepository.save(servico));
    }

    @Override
    @Transactional
    public ServicoResponseDTO update(UUID id, ServicoRequestDTO requestDTO) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com o id: " + id));

        servico.setDescricao(requestDTO.descricao());
        servico.setPreco(requestDTO.preco());

        return ServicoResponseDTO.fromDomain(servicoRepository.save(servico));
    }

    @Override
    @Transactional(readOnly = true)
    public ServicoResponseDTO getById(UUID id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com o id: " + id));
        return ServicoResponseDTO.fromDomain(servico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicoResponseDTO> getAll() {
        return servicoRepository.findAll().stream()
                .map(ServicoResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!servicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço não encontrado com o id: " + id);
        }
        servicoRepository.deleteById(id);
    }
}
