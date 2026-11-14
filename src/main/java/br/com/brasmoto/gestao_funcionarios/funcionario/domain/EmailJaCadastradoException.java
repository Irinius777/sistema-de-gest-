package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

public class EmailJaCadastradoException extends RuntimeException {
    public EmailJaCadastradoException(String email) {
        super("Email ja cadastrado: " + email);
    }
}
