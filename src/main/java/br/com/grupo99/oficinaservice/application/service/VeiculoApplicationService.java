package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.VeiculoRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.VeiculoResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.ResourceNotFoundException;
import br.com.grupo99.oficinaservice.application.usecase.GerenciarVeiculoUseCase;
import br.com.grupo99.oficinaservice.domain.model.Cliente;
import br.com.grupo99.oficinaservice.domain.model.Veiculo;
import br.com.grupo99.oficinaservice.domain.repository.ClienteRepository;
import br.com.grupo99.oficinaservice.domain.repository.VeiculoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VeiculoApplicationService implements GerenciarVeiculoUseCase {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    public VeiculoApplicationService(VeiculoRepository veiculoRepository, ClienteRepository clienteRepository) {
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public VeiculoResponseDTO create(VeiculoRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(requestDTO.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + requestDTO.clienteId()));

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(requestDTO.placa());
        veiculo.setMarca(requestDTO.marca());
        veiculo.setModelo(requestDTO.modelo());
        veiculo.setAno(requestDTO.ano());
        veiculo.setCliente(cliente);

        return VeiculoResponseDTO.fromDomain(veiculoRepository.save(veiculo));
    }

    @Override
    @Transactional
    public VeiculoResponseDTO update(UUID id, VeiculoRequestDTO requestDTO) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com o id: " + id));

        Cliente cliente = clienteRepository.findById(requestDTO.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + requestDTO.clienteId()));

        veiculo.setPlaca(requestDTO.placa());
        veiculo.setMarca(requestDTO.marca());
        veiculo.setModelo(requestDTO.modelo());
        veiculo.setAno(requestDTO.ano());
        veiculo.setCliente(cliente);

        return VeiculoResponseDTO.fromDomain(veiculoRepository.save(veiculo));
    }

    @Override
    @Transactional(readOnly = true)
    public VeiculoResponseDTO getById(UUID id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com o id: " + id));
        return VeiculoResponseDTO.fromDomain(veiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeiculoResponseDTO> getAll() {
        return veiculoRepository.findAll().stream()
                .map(VeiculoResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo não encontrado com o id: " + id);
        }
        veiculoRepository.deleteById(id);
    }
}
