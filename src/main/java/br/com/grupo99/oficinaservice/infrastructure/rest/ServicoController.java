package br.com.grupo99.oficinaservice.infrastructure.rest;

import br.com.grupo99.oficinaservice.application.dto.ServicoRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.ServicoResponseDTO;
import br.com.grupo99.oficinaservice.application.usecase.GerenciarServicoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/servicos")
@Tag(name = "Serviços", description = "APIs para Gerenciamento de Serviços")
public class ServicoController {

    private final GerenciarServicoUseCase gerenciarServicoUseCase;

    public ServicoController(GerenciarServicoUseCase gerenciarServicoUseCase) {
        this.gerenciarServicoUseCase = gerenciarServicoUseCase;
    }

    @PostMapping
    @Operation(summary = "Cria um novo serviço")
    public ResponseEntity<ServicoResponseDTO> create(@Valid @RequestBody ServicoRequestDTO requestDTO) {
        ServicoResponseDTO response = gerenciarServicoUseCase.create(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um serviço pelo ID")
    public ResponseEntity<ServicoResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(gerenciarServicoUseCase.getById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os serviços")
    public ResponseEntity<List<ServicoResponseDTO>> getAll() {
        return ResponseEntity.ok(gerenciarServicoUseCase.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um serviço existente")
    public ResponseEntity<ServicoResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody ServicoRequestDTO requestDTO) {
        return ResponseEntity.ok(gerenciarServicoUseCase.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um serviço")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        gerenciarServicoUseCase.delete(id);
    }
}
