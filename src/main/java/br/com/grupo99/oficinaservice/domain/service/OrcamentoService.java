package br.com.grupo99.oficinaservice.domain.service;

import br.com.grupo99.oficinaservice.domain.model.OrdemServico;
import br.com.grupo99.oficinaservice.domain.model.Peca;
import br.com.grupo99.oficinaservice.domain.model.Servico;

import java.math.BigDecimal;
import java.util.List;

public class OrcamentoService {
    /**
     * Calcula o valor total de um orçamento com base em uma lista de peças e serviços.
     * Esta lógica é um bom candidato para um serviço de domínio, pois coordena
     * informações de múltiplas entidades (Peças e Serviços) para produzir um resultado.
     *
     * @param pecas A lista de peças a serem incluídas no orçamento.
     * @param servicos A lista de serviços a serem incluídos no orçamento.
     * @return O valor total do orçamento como um BigDecimal.
     */
    public BigDecimal calcularOrcamento(List<Peca> pecas, List<Servico> servicos) {
        if (pecas == null && servicos == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalPecas = BigDecimal.ZERO;
        if (pecas != null) {
            totalPecas = pecas.stream()
                    .map(Peca::getPreco)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        BigDecimal totalServicos = BigDecimal.ZERO;
        if (servicos != null) {
            totalServicos = servicos.stream()
                    .map(Servico::getPreco)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return totalPecas.add(totalServicos);
    }

    /**
     * Atualiza o valor total de uma Ordem de Serviço existente.
     * Este método demonstra como um serviço de domínio pode operar sobre um agregado.
     *
     * @param ordemServico O agregado OrdemServico a ser recalculado.
     */
    public void recalcularValorTotalOrdemServico(OrdemServico ordemServico) {
        if (ordemServico == null) {
            return;
        }
        // A lógica de recálculo já está (corretamente) dentro do agregado OrdemServico.
        // O serviço de domínio aqui apenas orquestra a chamada, garantindo que a
        // responsabilidade de manter o estado consistente permaneça no agregado.
        ordemServico.recalcularValorTotal();
    }
}
