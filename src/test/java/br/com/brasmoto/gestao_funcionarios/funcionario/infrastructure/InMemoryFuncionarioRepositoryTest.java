package br.com.brasmoto.gestao_funcionarios.funcionario.infrastructure;

import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Cpf;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Email;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Endereco;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Funcionario;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioId;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Salario;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Telefone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryFuncionarioRepositoryTest {

    private static final String CPF_VALIDO = "52998224725";
    private static final String CPF_VALIDO_2 = "11144477735";

    private InMemoryFuncionarioRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryFuncionarioRepository();
    }

    private Funcionario funcionario(FuncionarioId id, String nome, String cpf, String email) {
        return new Funcionario(
                id,
                nome,
                Designacao.ANALISTA,
                new Salario(new BigDecimal("3000.00")),
                new Telefone("11999999999"),
                new Endereco("Rua X, 100"),
                new Email(email),
                new Cpf(cpf)
        );
    }

    @Test
    void deveSalvarEBuscarPorId() {
        Funcionario funcionario = funcionario(FuncionarioId.novo(), "Joao", CPF_VALIDO, "joao@test.com");

        repository.salvar(funcionario);

        assertThat(repository.buscarPorId(funcionario.getId())).contains(funcionario);
    }

    @Test
    void deveRetornarVazioQuandoIdNaoExistir() {
        assertThat(repository.buscarPorId(FuncionarioId.novo())).isEmpty();
    }

    @Test
    void deveBuscarPorCpf() {
        Funcionario funcionario = funcionario(FuncionarioId.novo(), "Joao", CPF_VALIDO, "joao@test.com");
        repository.salvar(funcionario);

        assertThat(repository.buscarPorCpf(new Cpf(CPF_VALIDO))).contains(funcionario);
        assertThat(repository.buscarPorCpf(new Cpf(CPF_VALIDO_2))).isEmpty();
    }

    @Test
    void deveBuscarPorEmail() {
        Funcionario funcionario = funcionario(FuncionarioId.novo(), "Joao", CPF_VALIDO, "joao@test.com");
        repository.salvar(funcionario);

        assertThat(repository.buscarPorEmail(new Email("joao@test.com"))).contains(funcionario);
        assertThat(repository.buscarPorEmail(new Email("outro@test.com"))).isEmpty();
    }

    @Test
    void deveListarTodos() {
        Funcionario primeiro = funcionario(FuncionarioId.novo(), "Joao", CPF_VALIDO, "joao@test.com");
        Funcionario segundo = funcionario(FuncionarioId.novo(), "Maria", CPF_VALIDO_2, "maria@test.com");
        repository.salvar(primeiro);
        repository.salvar(segundo);

        assertThat(repository.listarTodos()).containsExactlyInAnyOrder(primeiro, segundo);
    }

    @Test
    void listaRetornadaNaoDeveAlterarORepositorio() {
        repository.salvar(funcionario(FuncionarioId.novo(), "Joao", CPF_VALIDO, "joao@test.com"));

        List<Funcionario> lista = repository.listarTodos();
        lista.clear();

        assertThat(repository.listarTodos()).hasSize(1);
    }

    @Test
    void deveRemoverFuncionarioExistente() {
        Funcionario funcionario = funcionario(FuncionarioId.novo(), "Joao", CPF_VALIDO, "joao@test.com");
        repository.salvar(funcionario);

        assertThat(repository.remover(funcionario.getId())).isTrue();
        assertThat(repository.buscarPorId(funcionario.getId())).isEmpty();
    }

    @Test
    void deveRetornarFalsoAoRemoverFuncionarioInexistente() {
        assertThat(repository.remover(FuncionarioId.novo())).isFalse();
    }

    @Test
    void deveSuportarSalvamentosConcorrentes() {
        int quantidade = 1_000;

        IntStream.range(0, quantidade).parallel().forEach(i ->
                repository.salvar(funcionario(FuncionarioId.novo(), "Funcionario " + i,
                        CPF_VALIDO, "funcionario" + i + "@test.com")));

        assertThat(repository.listarTodos()).hasSize(quantidade);
    }
}
