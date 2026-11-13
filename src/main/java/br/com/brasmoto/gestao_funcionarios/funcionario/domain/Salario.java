package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Salario(BigDecimal value) {
    public Salario {
        Objects.requireNonNull(value, "salario nao pode ser nulo");
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("salario deve ser maior que zero");
        }
    }
}