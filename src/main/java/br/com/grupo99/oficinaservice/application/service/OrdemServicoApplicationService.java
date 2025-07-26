package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.application.dto.OrdemServicoDetalhesDTO;
import br.com.grupo99.oficinaservice.application.dto.OrdemServicoRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.OrdemServicoResponseDTO;
import br.com.grupo99.oficinaservice.application.exception.BusinessException;
import br.com.grupo99.oficinaservice.application.exception.ResourceNotFoundException;
import br.com.grupo99.oficinaservice.application.usecase.AtualizarStatusOrdemServicoUseCase;
import br.com.grupo99.oficinaservice.application.usecase.BuscarOrdemServicoDetalhesUseCase;
import br.com.grupo99.oficinaservice.application.usecase.CriarOrdemServicoUseCase;
import br.com.grupo99.oficinaservice.application.usecase.ListarOrdensServicoUseCase;


import br.com.grupo99.oficinaservice.domain.model.*;
import br.com.grupo99.oficinaservice.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdemServicoApplicationService implements CriarOrdemServicoUseCase, ListarOrdensServicoUseCase, BuscarOrdemServicoDetalhesUseCase, AtualizarStatusOrdemServicoUseCase {

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final PecaRepository pecaRepository;
    private final ServicoRepository servicoRepository;

    public OrdemServicoApplicationService(OrdemServicoRepository ordemServicoRepository, ClienteRepository clienteRepository, VeiculoRepository veiculoRepository, PecaRepository pecaRepository, ServicoRepository servicoRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.pecaRepository = pecaRepository;
        this.servicoRepository = servicoRepository;
    }

    @Override
    @Transactional
    public OrdemServicoResponseDTO execute(OrdemServicoRequestDTO request) {
        Cliente cliente = clienteRepository.findByCpfCnpj(request.cpfCnpjCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o CPF/CNPJ: " + request.cpfCnpjCliente()));

        Veiculo veiculo = veiculoRepository.findByPlaca(request.placaVeiculo())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com a placa: " + request.placaVeiculo()));

        OrdemServico os = new OrdemServico(cliente, veiculo);

        request.servicosIds().forEach(servicoId -> {
            Servico servico = servicoRepository.findById(servicoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado: " + servicoId));
            os.adicionarServico(servico, 1);
        });

        request.pecasIds().forEach(pecaId -> {
            Peca peca = pecaRepository.findById(pecaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Peça não encontrada: " + pecaId));
            os.adicionarPeca(peca, 1);
        });

        OrdemServico osSalva = ordemServicoRepository.save(os);
        return OrdemServicoResponseDTO.fromDomain(osSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdemServicoResponseDTO> execute() {
        return ordemServicoRepository.findAll().stream()
                .map(OrdemServicoResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdemServicoDetalhesDTO execute(UUID id) {
        OrdemServico os = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada com o id: " + id));
        return OrdemServicoDetalhesDTO.fromDomain(os);
    }

    @Override
    @Transactional
    public OrdemServicoResponseDTO execute(UUID id, StatusOS novoStatus) {
        OrdemServico os = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada com o id: " + id));

        try {
            switch (novoStatus) {
                case EM_DIAGNOSTICO -> os.iniciarDiagnostico();
                case AGUARDANDO_APROVACAO -> os.aguardarAprovacao();
                case EM_EXECUCAO -> os.aprovar();
                case FINALIZADA -> os.finalizar();
                case ENTREGUE -> os.entregar();
                default -> throw new BusinessException("Transição de status inválida: " + novoStatus);
            }
        } catch (IllegalStateException e) {
            throw new BusinessException(e.getMessage());
        }

        return OrdemServicoResponseDTO.fromDomain(ordemServicoRepository.save(os));
    }
}