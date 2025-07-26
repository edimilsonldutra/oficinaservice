package br.com.grupo99.oficinaservice.application.dto;

import br.com.grupo99.oficinaservice.domain.model.OrdemServico;
import br.com.grupo99.oficinaservice.domain.model.StatusOS;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO para a resposta detalhada de uma Ordem de Serviço, para consultas.
 *
 * @param id O ID da OS.
 * @param status O status atual da OS.
 * @param valorTotal O valor total do orçamento/OS.
 * @param dataCriacao A data de criação da OS.
 * @param dataFinalizacao A data de finalização (se aplicável).
 * @param dataEntrega A data de entrega (se aplicável).
 * @param cliente Os dados do cliente.
 * @param veiculo Os dados do veículo.
 * @param servicos A lista de serviços na OS.
 * @param pecas A lista de peças na OS.
 */
public record OrdemServicoDetalhesDTO(
        UUID id,
        StatusOS status,
        BigDecimal valorTotal,
        LocalDateTime dataCriacao,
        LocalDateTime dataFinalizacao,
        LocalDateTime dataEntrega,
        ClienteResponseDTO cliente,
        VeiculoResponseDTO veiculo,
        List<ServicoResponseDTO> servicos,
        List<PecaResponseDTO> pecas
) {
    /**
     * Método de fábrica para converter uma entidade OrdemServico em um DTO detalhado.
     */
    public static OrdemServicoDetalhesDTO fromDomain(OrdemServico os) {
        List<ServicoResponseDTO> servicosDTO = os.getServicos().stream()
                .map(item -> ServicoResponseDTO.fromDomain(item.getServico()))
                .collect(Collectors.toList());

        List<PecaResponseDTO> pecasDTO = os.getPecas().stream()
                .map(item -> PecaResponseDTO.fromDomain(item.getPeca()))
                .collect(Collectors.toList());

        return new OrdemServicoDetalhesDTO(
                os.getId(),
                os.getStatus(),
                os.getValorTotal(),
                os.getDataCriacao(),
                os.getDataFinalizacao(),
                os.getDataEntrega(),
                ClienteResponseDTO.fromDomain(os.getCliente()),
                VeiculoResponseDTO.fromDomain(os.getVeiculo()),
                servicosDTO,
                pecasDTO
        );
    }
}

