package br.com.grupo99.oficinaservice.infrastructure.rest;

import br.com.grupo99.oficinaservice.application.dto.TempoMedioServicoResponseDTO;
import br.com.grupo99.oficinaservice.application.usecase.CalcularTempoMedioServicoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/relatorios")
@Tag(name = "Relatórios", description = "APIs para geração de relatórios e métricas")
public class RelatorioController {

    private final CalcularTempoMedioServicoUseCase calcularTempoMedioServicoUseCase;

    public RelatorioController(CalcularTempoMedioServicoUseCase calcularTempoMedioServicoUseCase) {
        this.calcularTempoMedioServicoUseCase = calcularTempoMedioServicoUseCase;
    }

    @GetMapping("/tempo-medio/servicos/{servicoId}")
    @Operation(summary = "Calcula o tempo médio de execução para um tipo de serviço")
    public ResponseEntity<TempoMedioServicoResponseDTO> getTempoMedioPorServico(@PathVariable UUID servicoId) {
        TempoMedioServicoResponseDTO response = calcularTempoMedioServicoUseCase.execute(servicoId);
        return ResponseEntity.ok(response);
    }
}
