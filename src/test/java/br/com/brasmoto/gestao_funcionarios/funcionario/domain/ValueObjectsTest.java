package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueObjectsTest {

    @Test
    void salarioDeveSerMaiorQueZero() {
        assertThatThrownBy(() -> new Salario(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maior que zero");
        assertThatThrownBy(() -> new Salario(new BigDecimal("-1")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maior que zero");
    }

    @Test
    void salarioNaoPodeSerNulo() {
        assertThatThrownBy(() -> new Salario(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void telefoneNaoPodeSerVazio() {
        assertThatThrownBy(() -> new Telefone(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("telefone");
    }

    @Test
    void enderecoNaoPodeSerVazio() {
        assertThatThrownBy(() -> new Endereco(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("endereco");
    }

    @Test
    void funcionarioIdDeveGerarUuid() {
        FuncionarioId id = FuncionarioId.novo();

        assertThat(id.value()).isNotNull();
    }

    @Test
    void funcionarioIdNaoPodeSerNulo() {
        assertThatThrownBy(() -> new FuncionarioId((UUID) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id");
    }

    @Test
    void emailDeveSerNormalizado() {
        Email email = new Email("  Joao.Teste@Email.COM  ");

        assertThat(email.value()).isEqualTo("joao.teste@email.com");
    }

    @Test
    void emailInvalidoDeveLancarExcecao() {
        assertThatThrownBy(() -> new Email("email-invalido"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email invalido");
    }

    @Test
    void emailNaoPodeSerNulo() {
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void cpfDeveSerNormalizadoParaSomenteDigitos() {
        Cpf cpf = new Cpf("529.982.247-25");

        assertThat(cpf.value()).isEqualTo("52998224725");
    }

    @Test
    void cpfInvalidoDeveLancarExcecao() {
        assertThatThrownBy(() -> new Cpf("12345678901"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cpf invalido");
        assertThatThrownBy(() -> new Cpf("11111111111"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cpf invalido");
        assertThatThrownBy(() -> new Cpf("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cpf invalido");
    }

    @Test
    void cpfNaoPodeSerNulo() {
        assertThatThrownBy(() -> new Cpf(null))
                .isInstanceOf(NullPointerException.class);
    }
}