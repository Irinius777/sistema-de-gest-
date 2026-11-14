package br.com.brasmoto.gestao_funcionarios.funcionario.application.dto;

import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Funcionario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Schema(name = "FuncionarioResponse", description = "Dados de um funcionario")
public class FuncionarioResponse {

    @Schema(description = "Identificador unico do funcionario", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Nome completo", example = "Joao Silva")
    private String nome;

    @Schema(description = "Designacao/cargo", example = "DESENVOLVEDOR")
    private Designacao designacao;

    @Schema(description = "Salario mensal", example = "5000.00")
    private BigDecimal salario;

    @Schema(description = "Telefone com DDD", example = "11999999999")
    private String telefone;

    @Schema(description = "Endereco completo", example = "Rua X, 100 - Sao Paulo/SP")
    private String endereco;

    @Schema(description = "Email do funcionario", example = "joao@test.com")
    private String email;

    @Schema(description = "CPF do funcionario (somente digitos)", example = "52998224725")
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
