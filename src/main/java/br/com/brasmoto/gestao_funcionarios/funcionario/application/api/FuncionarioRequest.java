package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Value
public class FuncionarioRequest {

    @NotBlank
    private String nome;

    @NotNull
    private Designacao designacao;

    @NotNull
    @Positive
    private BigDecimal salario;

    @NotBlank
    private String telefone;

    @NotBlank
    private String endereco;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @CPF
    private String cpf;
}