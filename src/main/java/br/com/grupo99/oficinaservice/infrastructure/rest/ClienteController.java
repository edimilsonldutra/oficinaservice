package br.com.grupo99.oficinaservice.infrastructure.rest;

import br.com.grupo99.oficinaservice.application.dto.ClienteRequestDTO;
import br.com.grupo99.oficinaservice.application.dto.ClienteResponseDTO;
import br.com.grupo99.oficinaservice.application.usecase.GerenciarClienteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "APIs para Gerenciamento de Clientes")
public class ClienteController {

    private final GerenciarClienteUseCase gerenciarClienteUseCase;

    public ClienteController(GerenciarClienteUseCase gerenciarClienteUseCase) {
        this.gerenciarClienteUseCase = gerenciarClienteUseCase;
    }

    @PostMapping
    @Operation(summary = "Cria um novo cliente")
    public ResponseEntity<ClienteResponseDTO> create(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO response = gerenciarClienteUseCase.create(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um cliente pelo ID")
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(gerenciarClienteUseCase.getById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os clientes")
    public ResponseEntity<List<ClienteResponseDTO>> getAll() {
        return ResponseEntity.ok(gerenciarClienteUseCase.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um cliente existente")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody ClienteRequestDTO requestDTO) {
        return ResponseEntity.ok(gerenciarClienteUseCase.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um cliente")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        gerenciarClienteUseCase.delete(id);
    }
}
