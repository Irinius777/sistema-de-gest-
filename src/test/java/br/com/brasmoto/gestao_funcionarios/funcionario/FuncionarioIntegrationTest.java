package br.com.brasmoto.gestao_funcionarios.funcionario;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioRequest;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioResponse;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.service.FuncionarioService;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.CpfJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.EmailJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioNaoEncontradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FuncionarioIntegrationTest {

    private static final String CPF_VALIDO = "52998224725";
    private static final String CPF_VALIDO_2 = "11144477735";

    @Autowired
    private FuncionarioService service;

    @Autowired
    private FuncionarioRepository repository;

    @BeforeEach
    void limparRepositorio() {
        repository.listarTodos().forEach(funcionario -> repository.remover(funcionario.getId()));
    }

    private FuncionarioRequest request(String nome, String email, String cpf) {
        return new FuncionarioRequest(
                nome,
                Designacao.DESENVOLVEDOR,
                new BigDecimal("5000.00"),
                "11999999999",
                "Rua X, 100",
                email,
                cpf
        );
    }

    @Test
    void deveExecutarFluxoCompletoDeCrud() {
        FuncionarioResponse criado = service.criar(request("Joao Silva", "joao@test.com", CPF_VALIDO));

        assertThat(criado.getId()).isNotNull();
        assertThat(service.buscarPorId(criado.getId()).getNome()).isEqualTo("Joao Silva");
        assertThat(service.listar()).hasSize(1);

        FuncionarioResponse atualizado = service.atualizar(criado.getId(),
                request("Joao Atualizado", "joao.atualizado@test.com", CPF_VALIDO));

        assertThat(atualizado.getNome()).isEqualTo("Joao Atualizado");
        assertThat(service.buscarPorId(criado.getId()).getNome()).isEqualTo("Joao Atualizado");

        service.remover(criado.getId());

        assertThat(service.listar()).isEmpty();
        assertThatThrownBy(() -> service.buscarPorId(criado.getId()))
                .isInstanceOf(FuncionarioNaoEncontradoException.class);
    }

    @Test
    void deveRejeitarCpfDuplicado() {
        service.criar(request("Joao", "joao@test.com", CPF_VALIDO));

        assertThatThrownBy(() -> service.criar(request("Maria", "maria@test.com", CPF_VALIDO)))
                .isInstanceOf(CpfJaCadastradoException.class);

        assertThat(service.listar()).hasSize(1);
    }

    @Test
    void deveRejeitarEmailDuplicado() {
        service.criar(request("Joao", "joao@test.com", CPF_VALIDO));

        assertThatThrownBy(() -> service.criar(request("Maria", "joao@test.com", CPF_VALIDO_2)))
                .isInstanceOf(EmailJaCadastradoException.class);

        assertThat(service.listar()).hasSize(1);
    }
}
