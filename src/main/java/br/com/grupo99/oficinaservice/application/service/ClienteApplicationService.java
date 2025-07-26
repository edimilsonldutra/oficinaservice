package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.ClienteRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.ClienteResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.ResourceNotFoundException;
import br.com.grupo99.oficinaservice.application.usecase.GerenciarClienteUseCase;
import br.com.grupo99.oficinaservice.domain.model.Cliente;
import br.com.grupo99.oficinaservice.domain.repository.ClienteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteApplicationService implements GerenciarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public ClienteApplicationService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteRequestDTO requestDTO) {
        // TODO: Adicionar validação para CPF/CNPJ duplicado
        Cliente cliente = new Cliente();
        cliente.setNome(requestDTO.nome());
        cliente.setCpfCnpj(requestDTO.cpfCnpj());
        cliente.setEmail(requestDTO.email());
        cliente.setTelefone(requestDTO.telefone());

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return ClienteResponseDTO.fromDomain(clienteSalvo);
    }

    @Override
    @Transactional
    public ClienteResponseDTO update(UUID id, ClienteRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + id));

        cliente.setNome(requestDTO.nome());
        cliente.setCpfCnpj(requestDTO.cpfCnpj());
        cliente.setEmail(requestDTO.email());
        cliente.setTelefone(requestDTO.telefone());

        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return ClienteResponseDTO.fromDomain(clienteAtualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO getById(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + id));
        return ClienteResponseDTO.fromDomain(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getAll() {
        return clienteRepository.findAll().stream()
                .map(ClienteResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com o id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
