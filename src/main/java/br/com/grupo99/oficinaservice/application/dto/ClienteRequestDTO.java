package br.com.grupo99.oficinaservice.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


/**
 * DTO para a requisição de criação ou atualização de um Cliente.
 *
 * @param nome O nome completo do cliente.
 * @param cpfCnpj O CPF ou CNPJ do cliente (deve ser validado).
 * @param telefone O telefone de contato.
 * @param email O e-mail de contato (deve ser um formato válido).
 */
public record ClienteRequestDTO(
        @NotBlank(message = "O nome não pode ser vazio.")
        String nome,
        @NotBlank(message = "O CPF/CNPJ não pode ser vazio.")
        @Pattern(regexp = "^(\\d{11}|\\d{14})$", message = "CPF deve ter 11 dígitos ou CNPJ deve ter 14 dígitos")
        String cpfCnpj,
        String telefone,
        @Email(message = "O formato do e-mail é inválido.")
        String email
) {}
