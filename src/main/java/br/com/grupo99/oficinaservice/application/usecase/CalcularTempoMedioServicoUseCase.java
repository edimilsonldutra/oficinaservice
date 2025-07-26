package br.com.grupo99.oficinaservice.application.usecase;

import java.util.UUID;

public interface CalcularTempoMedioServicoUseCase {
    String execute(UUID servicoId);
}
