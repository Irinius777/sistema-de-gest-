package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record Telefone(String value) {

    private static final Pattern FORMATO_PERMITIDO = Pattern.compile("[0-9()\\-\\s+]+");

    public Telefone {
        Objects.requireNonNull(value, "telefone nao pode ser nulo");
        if (!FORMATO_PERMITIDO.matcher(value).matches()) {
            throw new IllegalArgumentException("telefone invalido: " + value);
        }
        String normalizado = value.replaceAll("\\D", "");
        if (normalizado.length() < 10 || normalizado.length() > 11) {
            throw new IllegalArgumentException("telefone deve conter 10 ou 11 digitos");
        }
        value = normalizado;
    }
}
