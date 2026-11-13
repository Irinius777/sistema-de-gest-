package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.UUID;

public class FuncionarioNaoEncontradoException extends RuntimeException {
    public FuncionarioNaoEncontradoException(UUID id) {
        super("Funcionario não encontrado: " + id);
    }
}