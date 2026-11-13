package br.com.brasmoto.gestao_funcionarios.funcionario.application.service;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.api.FuncionarioRequest;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.api.FuncionarioResponse;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Cpf;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Email;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Endereco;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Funcionario;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioId;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioNaoEncontradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioRepository;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Salario;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Telefone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioApplicationServiceTest {

    static final String CPF_VALIDO = "52998224725";

    @Mock
    private FuncionarioRepository repository;

    @InjectMocks
    private FuncionarioApplicationService service;

    private FuncionarioRequest request() {
        return new FuncionarioRequest(
                "Joao Silva",
                Designacao.DESENVOLVEDOR,
                new BigDecimal("5000.00"),
                "11999999999",
                "Rua X, 100",
                "joao@test.com",
                CPF_VALIDO
        );
    }

    private Funcionario funcionario() {
        return new Funcionario(
                new FuncionarioId(UUID.randomUUID()),
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
    void deveCriarFuncionarioESalvar() {
        when(repository.salvar(any(Funcionario.class))).thenAnswer(inv -> inv.getArgument(0));

        FuncionarioResponse response = service.criar(request());

        ArgumentCaptor<Funcionario> captor = ArgumentCaptor.forClass(Funcionario.class);
        verify(repository).salvar(captor.capture());
        Funcionario salvo = captor.getValue();

        assertThat(salvo.getNome()).isEqualTo("Joao Silva");
        assertThat(salvo.getDesignacao()).isEqualTo(Designacao.DESENVOLVEDOR);
        assertThat(salvo.getEmail().value()).isEqualTo("joao@test.com");
        assertThat(salvo.getCpf().value()).isEqualTo(CPF_VALIDO);
        assertThat(response.getId()).isEqualTo(salvo.getId().value());
    }

    @Test
    void deveBuscarFuncionarioPorId() {
        Funcionario funcionario = funcionario();
        when(repository.buscarPorId(funcionario.getId())).thenReturn(Optional.of(funcionario));

        FuncionarioResponse response = service.buscarPorId(funcionario.getId().value());

        assertThat(response.getId()).isEqualTo(funcionario.getId().value());
        assertThat(response.getNome()).isEqualTo("Joao Silva");
        assertThat(response.getEmail()).isEqualTo("joao@test.com");
        assertThat(response.getCpf()).isEqualTo(CPF_VALIDO);
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(id))
                .isInstanceOf(FuncionarioNaoEncontradoException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void deveListarFuncionarios() {
        when(repository.listarTodos()).thenReturn(List.of(funcionario()));

        List<FuncionarioResponse> response = service.listar();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getNome()).isEqualTo("Joao Silva");
    }

    @Test
    void deveAtualizarFuncionario() {
        Funcionario funcionario = funcionario();
        when(repository.buscarPorId(funcionario.getId())).thenReturn(Optional.of(funcionario));
        when(repository.salvar(any(Funcionario.class))).thenAnswer(inv -> inv.getArgument(0));

        FuncionarioRequest request = new FuncionarioRequest(
                "Joao Atualizado",
                Designacao.GERENTE,
                new BigDecimal("9000.00"),
                "11888888888",
                "Rua Z, 200",
                "joao.atualizado@test.com",
                "11144477735"
        );

        FuncionarioResponse response = service.atualizar(funcionario.getId().value(), request);

        assertThat(response.getNome()).isEqualTo("Joao Atualizado");
        assertThat(response.getDesignacao()).isEqualTo(Designacao.GERENTE);
        assertThat(response.getSalario()).isEqualByComparingTo("9000.00");
        assertThat(response.getEmail()).isEqualTo("joao.atualizado@test.com");
        assertThat(response.getCpf()).isEqualTo("11144477735");
        verify(repository).salvar(funcionario);
    }

    @Test
    void deveLancarExcecaoAoAtualizarIdInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(id, request()))
                .isInstanceOf(FuncionarioNaoEncontradoException.class);
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveRemoverFuncionarioExistente() {
        UUID id = UUID.randomUUID();
        when(repository.remover(any())).thenReturn(true);

        service.remover(id);

        verify(repository).remover(new FuncionarioId(id));
    }

    @Test
    void deveLancarExcecaoAoRemoverIdInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.remover(any())).thenReturn(false);

        assertThatThrownBy(() -> service.remover(id))
                .isInstanceOf(FuncionarioNaoEncontradoException.class);
    }
}