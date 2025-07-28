package br.com.grupo99.oficinaservice.domain.service;

import br.com.grupo99.oficinaservice.domain.model.*;

import java.math.BigDecimal;

public class OrcamentoService {

    public BigDecimal calcularValorTotal(OrdemServico ordemServico) {
        if (ordemServico == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalServicos = ordemServico.getServicos().stream()
                .map(ItemServico::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPecas = ordemServico.getPecas().stream()
                .map(ItemPeca::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalServicos.add(totalPecas);
    }
}
