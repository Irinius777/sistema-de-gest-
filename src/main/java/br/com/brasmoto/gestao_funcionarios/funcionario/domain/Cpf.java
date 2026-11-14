package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record Cpf(String value) {

    private static final Pattern FORMATO_PERMITIDO = Pattern.compile("[0-9.\\-\\s]+");

    public Cpf {
        Objects.requireNonNull(value, "cpf nao pode ser nulo");
        if (!FORMATO_PERMITIDO.matcher(value).matches()) {
            throw new IllegalArgumentException("cpf invalido: " + value);
        }
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
        int d1 = digitoVerificador(cpf, 9);
        int d2 = digitoVerificador(cpf, 10);
        return cpf.charAt(9) - '0' == d1 && cpf.charAt(10) - '0' == d2;
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
