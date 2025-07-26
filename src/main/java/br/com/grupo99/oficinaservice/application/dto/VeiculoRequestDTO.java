package br.com.grupo99.oficinaservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO para a requisição de criação de um novo Veículo.
 *
 * @param placa A placa do veículo (formato a ser validado).
 * @param marca A marca do veículo.
 * @param modelo O modelo do veículo.
 * @param ano O ano de fabricação do veículo.
 * @param clienteId O ID do cliente proprietário do veículo.
 */
public record VeiculoRequestDTO(
        @NotBlank(message = "A placa não pode ser vazia.")
        // Adicionar validação de formato de placa se necessário
        String placa,

        String renavam,

        @NotBlank(message = "A marca não pode ser vazia.")
        String marca,

        @NotBlank(message = "O modelo não pode ser vazio.")
        String modelo,

        @NotNull(message = "O ano não pode ser nulo.")
        Integer ano,

        @NotNull(message = "O ID do cliente não pode ser nulo.")
        UUID clienteId
) {}

