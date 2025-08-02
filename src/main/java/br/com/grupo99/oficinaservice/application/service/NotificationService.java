package br.com.grupo99.oficinaservice.application.service;

import br.com.grupo99.oficinaservice.domain.model.OrdemServico;

public interface NotificationService {
    void notificarClienteParaAprovacao(OrdemServico os);
}
