package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException(String cpf) {
        super("CPF ja cadastrado: " + cpf);
    }
}
