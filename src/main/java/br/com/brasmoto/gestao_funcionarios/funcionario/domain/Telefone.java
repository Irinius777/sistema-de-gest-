package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;

public record Telefone(String value) {
    public Telefone {
        Objects.requireNonNull(value, "telefone nao pode ser nulo");
        if (value.isBlank()) {
            throw new IllegalArgumentException("telefone nao pode ser vazio");
        }
    }
}