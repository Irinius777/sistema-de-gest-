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
    void telefoneDeveSerNormalizadoParaSomenteDigitos() {
        Telefone telefone = new Telefone("(11) 99999-9999");

        assertThat(telefone.value()).isEqualTo("11999999999");
    }

    @Test
    void telefoneComDezDigitosDeveSerAceito() {
        Telefone telefone = new Telefone("(11) 3333-4444");

        assertThat(telefone.value()).isEqualTo("1133334444");
    }

    @Test
    void telefoneNaoPodeSerVazio() {
        assertThatThrownBy(() -> new Telefone(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("telefone");
    }

    @Test
    void telefoneComQuantidadeInvalidaDeDigitosDeveLancarExcecao() {
        assertThatThrownBy(() -> new Telefone("119999999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("10 ou 11");
        assertThatThrownBy(() -> new Telefone("119999999999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("10 ou 11");
    }

    @Test
    void telefoneComLetrasDeveLancarExcecao() {
        assertThatThrownBy(() -> new Telefone("11999abc999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("telefone invalido");
    }

    @Test
    void enderecoDeveSerNormalizado() {
        Endereco endereco = new Endereco("  Rua X, 100  ");

        assertThat(endereco.value()).isEqualTo("Rua X, 100");
    }

    @Test
    void enderecoNaoPodeSerVazio() {
        assertThatThrownBy(() -> new Endereco(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("endereco");
    }

    @Test
    void enderecoMuitoLongoDeveLancarExcecao() {
        assertThatThrownBy(() -> new Endereco("a".repeat(256)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("255");
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
    void emailMuitoLongoDeveLancarExcecao() {
        String emailLongo = "a".repeat(250) + "@test.com";

        assertThatThrownBy(() -> new Email(emailLongo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("254");
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
    void cpfComLetrasDeveLancarExcecao() {
        assertThatThrownBy(() -> new Cpf("abc52998224725"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cpf invalido");
    }

    @Test
    void cpfNaoPodeSerNulo() {
        assertThatThrownBy(() -> new Cpf(null))
                .isInstanceOf(NullPointerException.class);
    }
}
