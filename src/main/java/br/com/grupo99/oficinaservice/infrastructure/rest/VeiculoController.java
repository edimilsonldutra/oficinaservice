package br.com.grupo99.oficinaservice.infrastructure.rest;

import br.com.grupo99.oficinaservice.application.dto.VeiculoRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.VeiculoResponseDTO;
import br.com.grupo99.oficinaservice.application.usecase.GerenciarVeiculoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/veiculos")
@Tag(name = "Veículos", description = "APIs para Gerenciamento de Veículos")
public class VeiculoController {

    private final GerenciarVeiculoUseCase gerenciarVeiculoUseCase;

    public VeiculoController(GerenciarVeiculoUseCase gerenciarVeiculoUseCase) {
        this.gerenciarVeiculoUseCase = gerenciarVeiculoUseCase;
    }

    @PostMapping
    @Operation(summary = "Cria um novo veículo")
    public ResponseEntity<VeiculoResponseDTO> create(@Valid @RequestBody VeiculoRequestDTO requestDTO) {
        VeiculoResponseDTO response = gerenciarVeiculoUseCase.create(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um veículo pelo ID")
    public ResponseEntity<VeiculoResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(gerenciarVeiculoUseCase.getById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os veículos")
    public ResponseEntity<List<VeiculoResponseDTO>> getAll() {
        return ResponseEntity.ok(gerenciarVeiculoUseCase.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um veículo existente")
    public ResponseEntity<VeiculoResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody VeiculoRequestDTO requestDTO) {
        return ResponseEntity.ok(gerenciarVeiculoUseCase.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um veículo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        gerenciarVeiculoUseCase.delete(id);
    }
}
