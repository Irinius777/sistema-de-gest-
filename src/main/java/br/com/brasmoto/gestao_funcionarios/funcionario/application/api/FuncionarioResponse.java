package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Funcionario;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class FuncionarioResponse {
    private UUID id;
    private String nome;
    private Designacao designacao;
    private BigDecimal salario;
    private String telefone;
    private String endereco;
    private String email;
    private String cpf;

    public static FuncionarioResponse from(Funcionario funcionario) {
        return new FuncionarioResponse(
                funcionario.getId().value(),
                funcionario.getNome(),
                funcionario.getDesignacao(),
                funcionario.getSalario().value(),
                funcionario.getTelefone().value(),
                funcionario.getEndereco().value(),
                funcionario.getEmail().value(),
                funcionario.getCpf().value()
        );
    }
}