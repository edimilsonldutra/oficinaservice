package br.com.grupo99.oficinaservice.infrastructure.rest;

import br.com.grupo99.oficinaservice.application.dto.OrdemServicoDetalhesDTO;
import br.com.grupo99.oficinaservice.application.dto.OrdemServicoRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.OrdemServicoResponseDTO;
import br.com.grupo99.oficinaservice.application.dto.OrdemServicoStatusUpdateRequestDTO;
import br.com.grupo99.oficinaservice.application.usecase.AtualizarStatusOrdemServicoUseCase;
import br.com.grupo99.oficinaservice.application.usecase.BuscarOrdemServicoDetalhesUseCase;
import br.com.grupo99.oficinaservice.application.usecase.CriarOrdemServicoUseCase;
import br.com.grupo99.oficinaservice.application.usecase.ListarOrdensServicoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ordens-servico")
@Tag(name = "Ordens de Serviço", description = "APIs para Gerenciamento de Ordens de Serviço")
public class OrdemServicoController {

    private final CriarOrdemServicoUseCase criarOrdemServicoUseCase;
    private final ListarOrdensServicoUseCase listarOrdensServicoUseCase;
    private final BuscarOrdemServicoDetalhesUseCase buscarOrdemServicoDetalhesUseCase;
    private final AtualizarStatusOrdemServicoUseCase atualizarStatusOrdemServicoUseCase;

    public OrdemServicoController(CriarOrdemServicoUseCase criarOrdemServicoUseCase,
                                  ListarOrdensServicoUseCase listarOrdensServicoUseCase,
                                  BuscarOrdemServicoDetalhesUseCase buscarOrdemServicoDetalhesUseCase,
                                  AtualizarStatusOrdemServicoUseCase atualizarStatusOrdemServicoUseCase
    ) {
        this.criarOrdemServicoUseCase = criarOrdemServicoUseCase;
        this.listarOrdensServicoUseCase = listarOrdensServicoUseCase;
        this.buscarOrdemServicoDetalhesUseCase = buscarOrdemServicoDetalhesUseCase;
        this.atualizarStatusOrdemServicoUseCase = atualizarStatusOrdemServicoUseCase;
    }

    @PostMapping
    @Operation(summary = "Cria uma nova Ordem de Serviço")
    public ResponseEntity<OrdemServicoResponseDTO> create(@Valid @RequestBody OrdemServicoRequestDTO requestDTO) {
        OrdemServicoResponseDTO response = criarOrdemServicoUseCase.execute(requestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista todas as Ordens de Serviço")
    public ResponseEntity<List<OrdemServicoResponseDTO>> getAll() {
        return ResponseEntity.ok(listarOrdensServicoUseCase.execute());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca detalhes de uma Ordem de Serviço")
    public ResponseEntity<OrdemServicoDetalhesDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(buscarOrdemServicoDetalhesUseCase.execute(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status de uma Ordem de Serviço")
    public ResponseEntity<OrdemServicoResponseDTO> updateStatus(@PathVariable UUID id, @Valid @RequestBody OrdemServicoStatusUpdateRequestDTO requestDTO) {
        OrdemServicoResponseDTO response = atualizarStatusOrdemServicoUseCase.execute(id, requestDTO.novoStatus());
        return ResponseEntity.ok(response);
    }
}
