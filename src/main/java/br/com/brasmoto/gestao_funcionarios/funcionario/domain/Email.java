package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern FORMATO = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        Objects.requireNonNull(value, "email nao pode ser nulo");
        String normalizado = value.trim().toLowerCase();
        if (!FORMATO.matcher(normalizado).matches()) {
            throw new IllegalArgumentException("email invalido: " + value);
        }
        value = normalizado;
    }
}