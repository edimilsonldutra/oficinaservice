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
import br.com.grupo99.oficinaservice.domain.service.OrcamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final OrcamentoService orcamentoService; // Instanciado diretamente

    public OrdemServicoApplicationService(
            OrdemServicoRepository ordemServicoRepository,
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository,
            PecaRepository pecaRepository,
            ServicoRepository servicoRepository
    ) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.pecaRepository = pecaRepository;
        this.servicoRepository = servicoRepository;
        this.orcamentoService = new OrcamentoService();
    }

    @Override
    @Transactional
    public OrdemServicoResponseDTO execute(OrdemServicoRequestDTO request) {
        // 1. Busca e valida as entidades
        Cliente cliente = clienteRepository.findByCpfCnpj(request.cpfCnpjCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Veiculo veiculo = veiculoRepository.findByPlaca(request.placaVeiculo())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        if (veiculo.getCliente() == null || !veiculo.getCliente().getId().equals(cliente.getId())) {
            throw new BusinessException("A placa informada não pertence ao cliente.");
        }

        // 2. Cria o agregado OrdemServico
        OrdemServico os = new OrdemServico(cliente.getId(), veiculo.getId());

        // 3. Adiciona peças e serviços
        if (request.servicosIds() != null) {
            request.servicosIds().forEach(servicoId -> {
                Servico servico = servicoRepository.findById(servicoId)
                        .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado: " + servicoId));
                os.adicionarServico(servico, 1);
            });
        }
        if (request.pecasIds() != null) {
            request.pecasIds().forEach(pecaId -> {
                Peca peca = pecaRepository.findById(pecaId)
                        .orElseThrow(() -> new ResourceNotFoundException("Peça não encontrada: " + pecaId));
                os.adicionarPeca(peca, 1);
            });
        }

        // 4. USA O SERVIÇO DE DOMÍNIO para calcular o valor
        BigDecimal valorCalculado = orcamentoService.calcularValorTotal(os);
        os.setValorTotal(valorCalculado);

        // 5. Salva o agregado
        OrdemServico osSalva = ordemServicoRepository.save(os);

        // 6. Retorna o DTO
        return OrdemServicoResponseDTO.fromDomain(osSalva, cliente.getNome(), veiculo.getPlaca());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdemServicoResponseDTO> execute() {
        List<OrdemServico> ordens = ordemServicoRepository.findAll();
        return ordens.stream().map(os -> {
            Cliente cliente = clienteRepository.findById(os.getClienteId()).orElse(null);
            Veiculo veiculo = veiculoRepository.findById(os.getVeiculoId()).orElse(null);

            String clienteNome = (cliente != null) ? cliente.getNome() : "Cliente não encontrado";
            String placaVeiculo = (veiculo != null) ? veiculo.getPlaca() : "Veículo não encontrado";

            return OrdemServicoResponseDTO.fromDomain(os, clienteNome, placaVeiculo);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdemServicoDetalhesDTO execute(UUID id) {
        OrdemServico os = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada com o id: " + id));

        Cliente cliente = clienteRepository.findById(os.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente associado à OS não encontrado"));

        Veiculo veiculo = veiculoRepository.findById(os.getVeiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo associado à OS não encontrado"));

        return OrdemServicoDetalhesDTO.fromDomain(os, cliente, veiculo);
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

        OrdemServico osSalva = ordemServicoRepository.save(os);

        Cliente cliente = clienteRepository.findById(osSalva.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente associado à OS não encontrado"));
        Veiculo veiculo = veiculoRepository.findById(osSalva.getVeiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo associado à OS não encontrado"));

        return OrdemServicoResponseDTO.fromDomain(osSalva, cliente.getNome(), veiculo.getPlaca());
    }
}