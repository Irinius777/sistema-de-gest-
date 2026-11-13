package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;
import java.util.UUID;

public record FuncionarioId(UUID value) {
    public FuncionarioId {
        Objects.requireNonNull(value, "id nao pode ser nulo");
    }

    public static FuncionarioId novo() {
        return new FuncionarioId(UUID.randomUUID());
    }
}