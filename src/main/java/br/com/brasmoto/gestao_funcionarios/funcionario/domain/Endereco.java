package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;

public record Endereco(String value) {
    public Endereco {
        Objects.requireNonNull(value, "endereco nao pode ser nulo");
        if (value.isBlank()) {
            throw new IllegalArgumentException("endereco nao pode ser vazio");
        }
    }
}