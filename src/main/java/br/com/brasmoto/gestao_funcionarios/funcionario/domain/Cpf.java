package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;

public record Cpf(String value) {

    public Cpf {
        Objects.requireNonNull(value, "cpf nao pode ser nulo");
        String normalizado = value.replaceAll("\\D", "");
        if (!isValido(normalizado)) {
            throw new IllegalArgumentException("cpf invalido: " + value);
        }
        value = normalizado;
    }

    private static boolean isValido(String cpf) {
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        try {
            int d1 = digitoVerificador(cpf, 9);
            int d2 = digitoVerificador(cpf, 10);
            return cpf.charAt(9) - '0' == d1 && cpf.charAt(10) - '0' == d2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int digitoVerificador(String cpf, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (cpf.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}