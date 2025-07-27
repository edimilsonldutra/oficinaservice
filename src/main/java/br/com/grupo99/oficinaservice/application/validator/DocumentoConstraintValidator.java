package br.com.grupo99.oficinaservice.application.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentoConstraintValidator implements ConstraintValidator<DocumentoValido, String> {

    @Override
    public boolean isValid(String documento, ConstraintValidatorContext context) {
        if (documento == null || documento.isBlank()) {
            return true; // A validação de nulo/vazio é responsabilidade de @NotBlank
        }

        if (!documento.matches("[\\d./-]+")) {
            return false;
        }

        String documentoLimpo = documento.replaceAll("[^0-9]", "");

        if (documentoLimpo.length() == 11) {
            return isValidCPF(documentoLimpo);
        } else if (documentoLimpo.length() == 14) {
            return isValidCNPJ(documentoLimpo);
        }

        return false;
    }

    /**
     * Valida se o CPF é válido.
     */
    private boolean isValidCPF(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Cálculo do primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int resto = soma % 11;
            int digito1 = (resto < 2) ? 0 : 11 - resto;

            // Verifica o primeiro dígito
            if (digito1 != (cpf.charAt(9) - '0')) {
                return false;
            }

            // Cálculo do segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            resto = soma % 11;
            int digito2 = (resto < 2) ? 0 : 11 - resto;

            // Verifica o segundo dígito
            return digito2 == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida se o CNPJ é válido.
     */
    private boolean isValidCNPJ(String cnpj) {
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // Cálculo do primeiro dígito verificador
            int soma = 0;
            int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            for (int i = 0; i < 12; i++) {
                soma += (cnpj.charAt(i) - '0') * pesos1[i];
            }
            int resto = soma % 11;
            int digito1 = (resto < 2) ? 0 : 11 - resto;

            // Verifica se o primeiro dígito calculado confere com o dígito do CNPJ
            if (digito1 != (cnpj.charAt(12) - '0')) {
                return false;
            }

            // Cálculo do segundo dígito verificador
            soma = 0;
            int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            for (int i = 0; i < 13; i++) {
                soma += (cnpj.charAt(i) - '0') * pesos2[i];
            }
            resto = soma % 11;
            int digito2 = (resto < 2) ? 0 : 11 - resto;

            // Verifica se o segundo dígito calculado confere com o dígito do CNPJ
            return digito2 == (cnpj.charAt(13) - '0');
        } catch (Exception e) {
            return false;
        }
    }
}
