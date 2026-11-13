package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FuncionarioTest {

    static final String CPF_VALIDO = "52998224725";

    private Funcionario novoFuncionario() {
        return new Funcionario(
                FuncionarioId.novo(),
                "Joao Silva",
                Designacao.DESENVOLVEDOR,
                new Salario(new BigDecimal("5000.00")),
                new Telefone("11999999999"),
                new Endereco("Rua X, 100"),
                new Email("joao@test.com"),
                new Cpf(CPF_VALIDO)
        );
    }

    @Test
    void deveCriarFuncionarioComDadosValidos() {
        Funcionario funcionario = novoFuncionario();

        assertThat(funcionario.getId()).isNotNull();
        assertThat(funcionario.getNome()).isEqualTo("Joao Silva");
        assertThat(funcionario.getDesignacao()).isEqualTo(Designacao.DESENVOLVEDOR);
        assertThat(funcionario.getSalario().value()).isEqualByComparingTo("5000.00");
        assertThat(funcionario.getEmail().value()).isEqualTo("joao@test.com");
        assertThat(funcionario.getCpf().value()).isEqualTo(CPF_VALIDO);
    }

    @Test
    void deveRemoverEspacosDoNome() {
        Funcionario funcionario = new Funcionario(
                FuncionarioId.novo(),
                "  Maria  ",
                Designacao.ANALISTA,
                new Salario(BigDecimal.TEN),
                new Telefone("11999999999"),
                new Endereco("Rua Y"),
                new Email("maria@test.com"),
                new Cpf(CPF_VALIDO)
        );

        assertThat(funcionario.getNome()).isEqualTo("Maria");
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        assertThatThrownBy(() -> new Funcionario(
                FuncionarioId.novo(), null, Designacao.ANALISTA,
                new Salario(BigDecimal.TEN), new Telefone("11999999999"), new Endereco("Rua Y"),
                new Email("maria@test.com"), new Cpf(CPF_VALIDO)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nome");
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        assertThatThrownBy(() -> new Funcionario(
                FuncionarioId.novo(), "   ", Designacao.ANALISTA,
                new Salario(BigDecimal.TEN), new Telefone("11999999999"), new Endereco("Rua Y"),
                new Email("maria@test.com"), new Cpf(CPF_VALIDO)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nome");
    }

    @Test
    void deveLancarExcecaoQuandoDesignacaoForNula() {
        assertThatThrownBy(() -> new Funcionario(
                FuncionarioId.novo(), "Joao", null,
                new Salario(BigDecimal.TEN), new Telefone("11999999999"), new Endereco("Rua Y"),
                new Email("joao@test.com"), new Cpf(CPF_VALIDO)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("designacao");
    }

    @Test
    void deveLancarExcecaoQuandoEmailForNulo() {
        assertThatThrownBy(() -> new Funcionario(
                FuncionarioId.novo(), "Joao", Designacao.ANALISTA,
                new Salario(BigDecimal.TEN), new Telefone("11999999999"), new Endereco("Rua Y"),
                null, new Cpf(CPF_VALIDO)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("email");
    }

    @Test
    void deveLancarExcecaoQuandoCpfForNulo() {
        assertThatThrownBy(() -> new Funcionario(
                FuncionarioId.novo(), "Joao", Designacao.ANALISTA,
                new Salario(BigDecimal.TEN), new Telefone("11999999999"), new Endereco("Rua Y"),
                new Email("joao@test.com"), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("cpf");
    }

    @Test
    void deveAtualizarDadosDoFuncionario() {
        Funcionario funcionario = novoFuncionario();

        funcionario.atualizar(
                "Joao Atualizado",
                Designacao.GERENTE,
                new Salario(new BigDecimal("9000.00")),
                new Telefone("11888888888"),
                new Endereco("Rua Z, 200"),
                new Email("joao.atualizado@test.com"),
                new Cpf("11144477735")
        );

        assertThat(funcionario.getNome()).isEqualTo("Joao Atualizado");
        assertThat(funcionario.getDesignacao()).isEqualTo(Designacao.GERENTE);
        assertThat(funcionario.getSalario().value()).isEqualByComparingTo("9000.00");
        assertThat(funcionario.getTelefone().value()).isEqualTo("11888888888");
        assertThat(funcionario.getEndereco().value()).isEqualTo("Rua Z, 200");
        assertThat(funcionario.getEmail().value()).isEqualTo("joao.atualizado@test.com");
        assertThat(funcionario.getCpf().value()).isEqualTo("11144477735");
    }
}