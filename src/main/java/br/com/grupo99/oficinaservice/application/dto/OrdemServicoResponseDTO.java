package br.com.grupo99.oficinaservice.application.dto;

import br.com.grupo99.oficinaservice.domain.model.OrdemServico;
import br.com.grupo99.oficinaservice.domain.model.StatusOS;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para a resposta resumida de uma Ordem de Serviço.
 *
 * @param id O ID da OS.
 * @param clienteNome Nome do cliente.
 * @param placaVeiculo Placa do veículo.
 * @param status O status atual da OS.
 * @param valorTotal O valor total do orçamento/OS.
 * @param dataCriacao A data de criação da OS.
 */
public record OrdemServicoResponseDTO(
        UUID id,
        String clienteNome,
        String placaVeiculo,
        StatusOS status,
        BigDecimal valorTotal,
        LocalDateTime dataCriacao
) {

    /**
     * Método de fábrica para converter uma entidade OrdemServico em um OrdemServicoResponseDTO.
     * Este é o método que estava faltando.
     */
    public static OrdemServicoResponseDTO fromDomain(OrdemServico os) {
        return new OrdemServicoResponseDTO(
                os.getId(),
                os.getCliente().getNome(),
                os.getVeiculo().getPlaca(),
                os.getStatus(),
                os.getValorTotal(),
                os.getDataCriacao()
        );
    }

}
