package br.com.brasmoto.gestao_funcionarios.funcionario.application.service;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioRequest;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioResponse;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Cpf;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.CpfJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Email;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.EmailJaCadastradoException;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncionarioApplicationServiceTest {

    static final String CPF_VALIDO = "52998224725";
    static final String CPF_VALIDO_2 = "11144477735";

    @Mock
    private FuncionarioRepository repository;

    @InjectMocks
    private FuncionarioApplicationService service;

    private FuncionarioRequest request() {
        return request("Joao Silva", Designacao.DESENVOLVEDOR, "5000.00",
                "11999999999", "Rua X, 100", "joao@test.com", CPF_VALIDO);
    }

    private FuncionarioRequest request(String nome, Designacao designacao, String salario,
                                       String telefone, String endereco, String email, String cpf) {
        return new FuncionarioRequest(nome, designacao, new BigDecimal(salario),
                telefone, endereco, email, cpf);
    }

    private Funcionario funcionario() {
        return funcionario(FuncionarioId.novo(), "Joao Silva", Designacao.DESENVOLVEDOR,
                "5000.00", "11999999999", "Rua X, 100", "joao@test.com", CPF_VALIDO);
    }

    private Funcionario funcionario(FuncionarioId id, String nome, Designacao designacao,
                                    String salario, String telefone, String endereco,
                                    String email, String cpf) {
        return new Funcionario(
                id,
                nome,
                designacao,
                new Salario(new BigDecimal(salario)),
                new Telefone(telefone),
                new Endereco(endereco),
                new Email(email),
                new Cpf(cpf)
        );
    }

    private void semDuplicidade() {
        when(repository.buscarPorCpf(any())).thenReturn(Optional.empty());
        when(repository.buscarPorEmail(any())).thenReturn(Optional.empty());
    }

    @Test
    void deveCriarFuncionarioESalvar() {
        semDuplicidade();
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
    void deveNormalizarDadosAoCriar() {
        semDuplicidade();
        when(repository.salvar(any(Funcionario.class))).thenAnswer(inv -> inv.getArgument(0));

        FuncionarioResponse response = service.criar(request(
                "  Maria  ", Designacao.ANALISTA, "3500.00",
                "(11) 98888-7777", "  Rua Y, 20  ", "  MARIA@TEST.COM  ", "529.982.247-25"));

        assertThat(response.getNome()).isEqualTo("Maria");
        assertThat(response.getTelefone()).isEqualTo("11988887777");
        assertThat(response.getEndereco()).isEqualTo("Rua Y, 20");
        assertThat(response.getEmail()).isEqualTo("maria@test.com");
        assertThat(response.getCpf()).isEqualTo("52998224725");
    }

    @Test
    void deveLancarExcecaoAoCriarComCpfDuplicado() {
        Funcionario existente = funcionario();
        when(repository.buscarPorCpf(any())).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.criar(request()))
                .isInstanceOf(CpfJaCadastradoException.class)
                .hasMessageContaining(CPF_VALIDO);
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoAoCriarComEmailDuplicado() {
        Funcionario existente = funcionario();
        when(repository.buscarPorCpf(any())).thenReturn(Optional.empty());
        when(repository.buscarPorEmail(any())).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.criar(request()))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessageContaining("joao@test.com");
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoQuandoDadosInvalidosAntesDeSalvar() {
        FuncionarioRequest request = request("Joao", Designacao.ANALISTA, "1000.00",
                "11999999999", "Rua X", "email-invalido", CPF_VALIDO);

        assertThatThrownBy(() -> service.criar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email invalido");
        verifyNoInteractions(repository);
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
    void deveListarFuncionariosEmOrdemAlfabetica() {
        Funcionario zeca = funcionario(FuncionarioId.novo(), "Zeca", Designacao.ANALISTA,
                "3000.00", "11999999999", "Rua Z", "zeca@test.com", CPF_VALIDO);
        Funcionario ana = funcionario(FuncionarioId.novo(), "Ana", Designacao.GERENTE,
                "9000.00", "11888888888", "Rua A", "ana@test.com", CPF_VALIDO_2);
        when(repository.listarTodos()).thenReturn(List.of(zeca, ana));

        List<FuncionarioResponse> response = service.listar();

        assertThat(response).extracting(FuncionarioResponse::getNome)
                .containsExactly("Ana", "Zeca");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverFuncionarios() {
        when(repository.listarTodos()).thenReturn(List.of());

        assertThat(service.listar()).isEmpty();
    }

    @Test
    void deveAtualizarFuncionario() {
        Funcionario funcionario = funcionario();
        semDuplicidade();
        when(repository.buscarPorId(funcionario.getId())).thenReturn(Optional.of(funcionario));
        when(repository.salvar(any(Funcionario.class))).thenAnswer(inv -> inv.getArgument(0));

        FuncionarioResponse response = service.atualizar(funcionario.getId().value(),
                request("Joao Atualizado", Designacao.GERENTE, "9000.00",
                        "11888888888", "Rua Z, 200", "joao.atualizado@test.com", CPF_VALIDO_2));

        assertThat(response.getId()).isEqualTo(funcionario.getId().value());
        assertThat(response.getNome()).isEqualTo("Joao Atualizado");
        assertThat(response.getDesignacao()).isEqualTo(Designacao.GERENTE);
        assertThat(response.getSalario()).isEqualByComparingTo("9000.00");
        assertThat(response.getEmail()).isEqualTo("joao.atualizado@test.com");
        assertThat(response.getCpf()).isEqualTo(CPF_VALIDO_2);

        ArgumentCaptor<Funcionario> captor = ArgumentCaptor.forClass(Funcionario.class);
        verify(repository).salvar(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(funcionario.getId());
        assertThat(captor.getValue().getNome()).isEqualTo("Joao Atualizado");

        assertThat(funcionario.getNome()).isEqualTo("Joao Silva");
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
    void deveLancarExcecaoAoAtualizarParaCpfDuplicado() {
        Funcionario existente = funcionario();
        Funcionario alvo = funcionario(FuncionarioId.novo(), "Maria", Designacao.ANALISTA,
                "4000.00", "11999999999", "Rua Y", "maria@test.com", CPF_VALIDO_2);

        when(repository.buscarPorId(alvo.getId())).thenReturn(Optional.of(alvo));
        when(repository.buscarPorCpf(any())).thenReturn(Optional.of(existente));

        FuncionarioRequest request = request("Maria", Designacao.ANALISTA, "4000.00",
                "11999999999", "Rua Y", "maria@test.com", CPF_VALIDO);

        assertThatThrownBy(() -> service.atualizar(alvo.getId().value(), request))
                .isInstanceOf(CpfJaCadastradoException.class);
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizarParaEmailDuplicado() {
        Funcionario existente = funcionario();
        Funcionario alvo = funcionario(FuncionarioId.novo(), "Maria", Designacao.ANALISTA,
                "4000.00", "11999999999", "Rua Y", "maria@test.com", CPF_VALIDO_2);

        when(repository.buscarPorId(alvo.getId())).thenReturn(Optional.of(alvo));
        when(repository.buscarPorCpf(any())).thenReturn(Optional.empty());
        when(repository.buscarPorEmail(any())).thenReturn(Optional.of(existente));

        FuncionarioRequest request = request("Maria", Designacao.ANALISTA, "4000.00",
                "11999999999", "Rua Y", "joao@test.com", CPF_VALIDO_2);

        assertThatThrownBy(() -> service.atualizar(alvo.getId().value(), request))
                .isInstanceOf(EmailJaCadastradoException.class);
        verify(repository, never()).salvar(any());
    }

    @Test
    void devePermitirAtualizarMesmoFuncionarioComMesmoCpfEEmail() {
        Funcionario funcionario = funcionario();
        when(repository.buscarPorId(funcionario.getId())).thenReturn(Optional.of(funcionario));
        when(repository.buscarPorCpf(any())).thenReturn(Optional.of(funcionario));
        when(repository.buscarPorEmail(any())).thenReturn(Optional.of(funcionario));
        when(repository.salvar(any(Funcionario.class))).thenAnswer(inv -> inv.getArgument(0));

        FuncionarioResponse response = service.atualizar(funcionario.getId().value(), request());

        assertThat(response.getCpf()).isEqualTo(CPF_VALIDO);
        verify(repository).salvar(any(Funcionario.class));
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
