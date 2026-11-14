package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;

public record Endereco(String value) {

    private static final int ENDERECO_MAX_LENGTH = 255;

    public Endereco {
        Objects.requireNonNull(value, "endereco nao pode ser nulo");
        String normalizado = value.trim();
        if (normalizado.isEmpty()) {
            throw new IllegalArgumentException("endereco nao pode ser vazio");
        }
        if (normalizado.length() > ENDERECO_MAX_LENGTH) {
            throw new IllegalArgumentException("endereco deve ter no maximo " + ENDERECO_MAX_LENGTH + " caracteres");
        }
        value = normalizado;
    }
}
