package br.com.brasmoto.gestao_funcionarios.funcionario.application.dto;

import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Value
@Schema(name = "FuncionarioRequest", description = "Dados para criacao ou atualizacao de um funcionario")
public class FuncionarioRequest {

    @NotBlank(message = "nome e obrigatorio")
    @Size(max = 150, message = "nome deve ter no maximo 150 caracteres")
    @Schema(description = "Nome completo do funcionario", example = "Joao Silva", maxLength = 150)
    private String nome;

    @NotNull(message = "designacao e obrigatoria")
    @Schema(description = "Designacao/cargo do funcionario", example = "DESENVOLVEDOR")
    private Designacao designacao;

    @NotNull(message = "salario e obrigatorio")
    @Positive(message = "salario deve ser maior que zero")
    @Schema(description = "Salario mensal, maior que zero", example = "5000.00")
    private BigDecimal salario;

    @NotBlank(message = "telefone e obrigatorio")
    @Size(max = 20, message = "telefone deve ter no maximo 20 caracteres")
    @Schema(description = "Telefone com DDD", example = "(11) 99999-9999", maxLength = 20)
    private String telefone;

    @NotBlank(message = "endereco e obrigatorio")
    @Size(max = 255, message = "endereco deve ter no maximo 255 caracteres")
    @Schema(description = "Endereco completo", example = "Rua X, 100 - Sao Paulo/SP", maxLength = 255)
    private String endereco;

    @NotBlank(message = "email e obrigatorio")
    @Email(message = "email invalido")
    @Size(max = 254, message = "email deve ter no maximo 254 caracteres")
    @Schema(description = "Email do funcionario", example = "joao@test.com", maxLength = 254)
    private String email;

    @NotBlank(message = "cpf e obrigatorio")
    @CPF(message = "cpf invalido")
    @Schema(description = "CPF do funcionario, com ou sem pontuacao", example = "529.982.247-25")
    private String cpf;
}
