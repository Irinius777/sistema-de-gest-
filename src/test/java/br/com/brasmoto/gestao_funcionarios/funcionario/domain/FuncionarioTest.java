package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FuncionarioTest {

    static final String CPF_VALIDO = "52998224725";
    static final String CPF_VALIDO_2 = "11144477735";

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
    void deveLancarExcecaoQuandoNomeForMuitoLongo() {
        String nomeLongo = "a".repeat(151);

        assertThatThrownBy(() -> new Funcionario(
                FuncionarioId.novo(), nomeLongo, Designacao.ANALISTA,
                new Salario(BigDecimal.TEN), new Telefone("11999999999"), new Endereco("Rua Y"),
                new Email("maria@test.com"), new Cpf(CPF_VALIDO)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("150");
    }

    @Test
    void deveLancarExcecaoQuandoIdForNulo() {
        assertThatThrownBy(() -> new Funcionario(
                null, "Joao", Designacao.ANALISTA,
                new Salario(BigDecimal.TEN), new Telefone("11999999999"), new Endereco("Rua Y"),
                new Email("joao@test.com"), new Cpf(CPF_VALIDO)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id");
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
    void deveLancarExcecaoQuandoSalarioForNulo() {
        assertThatThrownBy(() -> new Funcionario(
                FuncionarioId.novo(), "Joao", Designacao.ANALISTA,
                null, new Telefone("11999999999"), new Endereco("Rua Y"),
                new Email("joao@test.com"), new Cpf(CPF_VALIDO)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("salario");
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneForNulo() {
        assertThatThrownBy(() -> new Funcionario(
                FuncionarioId.novo(), "Joao", Designacao.ANALISTA,
                new Salario(BigDecimal.TEN), null, new Endereco("Rua Y"),
                new Email("joao@test.com"), new Cpf(CPF_VALIDO)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("telefone");
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoForNulo() {
        assertThatThrownBy(() -> new Funcionario(
                FuncionarioId.novo(), "Joao", Designacao.ANALISTA,
                new Salario(BigDecimal.TEN), new Telefone("11999999999"), null,
                new Email("joao@test.com"), new Cpf(CPF_VALIDO)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("endereco");
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
    void deveAtualizarDadosDoFuncionarioSemAlterarAInstanciaOriginal() {
        Funcionario funcionario = novoFuncionario();

        Funcionario atualizado = funcionario.atualizar(
                "Joao Atualizado",
                Designacao.GERENTE,
                new Salario(new BigDecimal("9000.00")),
                new Telefone("11888888888"),
                new Endereco("Rua Z, 200"),
                new Email("joao.atualizado@test.com"),
                new Cpf(CPF_VALIDO_2)
        );

        assertThat(atualizado.getId()).isEqualTo(funcionario.getId());
        assertThat(atualizado.getNome()).isEqualTo("Joao Atualizado");
        assertThat(atualizado.getDesignacao()).isEqualTo(Designacao.GERENTE);
        assertThat(atualizado.getSalario().value()).isEqualByComparingTo("9000.00");
        assertThat(atualizado.getTelefone().value()).isEqualTo("11888888888");
        assertThat(atualizado.getEndereco().value()).isEqualTo("Rua Z, 200");
        assertThat(atualizado.getEmail().value()).isEqualTo("joao.atualizado@test.com");
        assertThat(atualizado.getCpf().value()).isEqualTo(CPF_VALIDO_2);

        assertThat(funcionario.getNome()).isEqualTo("Joao Silva");
        assertThat(funcionario.getCpf().value()).isEqualTo(CPF_VALIDO);
    }

    @Test
    void deveConsiderarFuncionariosIguaisPeloId() {
        FuncionarioId id = FuncionarioId.novo();
        Funcionario primeiro = new Funcionario(id, "Joao", Designacao.ANALISTA,
                new Salario(BigDecimal.TEN), new Telefone("11999999999"), new Endereco("Rua X"),
                new Email("joao@test.com"), new Cpf(CPF_VALIDO));
        Funcionario segundo = new Funcionario(id, "Maria", Designacao.GERENTE,
                new Salario(BigDecimal.ONE), new Telefone("11888888888"), new Endereco("Rua Y"),
                new Email("maria@test.com"), new Cpf(CPF_VALIDO_2));

        assertThat(primeiro).isEqualTo(segundo);
        assertThat(primeiro).hasSameHashCodeAs(segundo);
    }
}
