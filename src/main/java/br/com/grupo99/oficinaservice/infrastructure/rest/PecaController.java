package br.com.grupo99.oficinaservice.infrastructure.rest;

import br.com.grupo99.oficinaservice.application.dto.PecaRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.PecaResponseDTO;
import br.com.grupo99.oficinaservice.application.dto.PecaUpdateEstoqueRequestDTO;
import br.com.grupo99.oficinaservice.application.usecase.GerenciarPecaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pecas")
@Tag(name = "Peças", description = "APIs para Gerenciamento de Peças e Estoque")
public class PecaController {

    private final GerenciarPecaUseCase gerenciarPecaUseCase;

    public PecaController(GerenciarPecaUseCase gerenciarPecaUseCase) {
        this.gerenciarPecaUseCase = gerenciarPecaUseCase;
    }

    @PostMapping
    @Operation(summary = "Cria uma nova peça")
    public ResponseEntity<PecaResponseDTO> create(@Valid @RequestBody PecaRequestDTO requestDTO) {
        PecaResponseDTO response = gerenciarPecaUseCase.create(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma peça pelo ID")
    public ResponseEntity<PecaResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(gerenciarPecaUseCase.getById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todas as peças")
    public ResponseEntity<List<PecaResponseDTO>> getAll() {
        return ResponseEntity.ok(gerenciarPecaUseCase.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma peça existente")
    public ResponseEntity<PecaResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody PecaRequestDTO requestDTO) {
        return ResponseEntity.ok(gerenciarPecaUseCase.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma peça")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        gerenciarPecaUseCase.delete(id);
    }

    @PatchMapping("/{id}/estoque")
    @Operation(summary = "Adiciona ou remove itens do estoque de uma peça")
    public ResponseEntity<PecaResponseDTO> updateEstoque(@PathVariable UUID id, @Valid @RequestBody PecaUpdateEstoqueRequestDTO requestDTO) {
        PecaResponseDTO response = gerenciarPecaUseCase.adicionarEstoque(id, requestDTO.quantidade());
        return ResponseEntity.ok(response);
    }
}
