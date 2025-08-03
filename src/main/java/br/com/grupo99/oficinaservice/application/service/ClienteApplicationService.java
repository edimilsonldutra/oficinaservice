package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.ClienteRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.ClienteResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.BusinessException;
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

    //teste
    private final ClienteRepository clienteRepository;

    public ClienteApplicationService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteRequestDTO requestDTO) {
        validarCamposObrigatorios(requestDTO);
        verificarDuplicidadeCpfCnpj(requestDTO.cpfCnpj());

        Cliente cliente = mapearParaEntidade(requestDTO);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        return ClienteResponseDTO.fromDomain(clienteSalvo);
    }

    @Override
    @Transactional
    public ClienteResponseDTO update(UUID id, ClienteRequestDTO requestDTO) {
        Cliente clienteExistente = buscarClientePorId(id);

        atualizarDadosCliente(clienteExistente, requestDTO);

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return ClienteResponseDTO.fromDomain(clienteAtualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO getById(UUID id) {
        Cliente cliente = buscarClientePorId(id);
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

    // MÉTODOS AUXILIARES

    private void validarCamposObrigatorios(ClienteRequestDTO dto) {
        if (isBlank(dto.nome())) throw new BusinessException("Nome é obrigatório.");
        if (isBlank(dto.cpfCnpj())) throw new BusinessException("CPF/CNPJ é obrigatório.");
        if (isBlank(dto.email())) throw new BusinessException("E-mail é obrigatório.");
        if (isBlank(dto.telefone())) throw new BusinessException("Telefone é obrigatório.");
    }

    private void verificarDuplicidadeCpfCnpj(String cpfCnpj) {
        if (clienteRepository.existsByCpfCnpj(cpfCnpj)) {
            throw new BusinessException("Já existe um cliente com este CPF/CNPJ.");
        }
    }

    private Cliente buscarClientePorId(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + id));
    }

    private Cliente mapearParaEntidade(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpfCnpj(dto.cpfCnpj());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        return cliente;
    }

    private void atualizarDadosCliente(Cliente cliente, ClienteRequestDTO dto) {
        cliente.setNome(dto.nome());
        cliente.setCpfCnpj(dto.cpfCnpj());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
