package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern FORMATO = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final int EMAIL_MAX_LENGTH = 254;

    public Email {
        Objects.requireNonNull(value, "email nao pode ser nulo");
        String normalizado = value.trim().toLowerCase();
        if (normalizado.length() > EMAIL_MAX_LENGTH) {
            throw new IllegalArgumentException("email deve ter no maximo " + EMAIL_MAX_LENGTH + " caracteres");
        }
        if (!FORMATO.matcher(normalizado).matches()) {
            throw new IllegalArgumentException("email invalido: " + value);
        }
        value = normalizado;
    }
}
