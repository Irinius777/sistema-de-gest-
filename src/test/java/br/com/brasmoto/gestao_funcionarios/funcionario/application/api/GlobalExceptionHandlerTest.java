package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import br.com.brasmoto.gestao_funcionarios.funcionario.domain.CpfJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.EmailJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveConverterFuncionarioNaoEncontradoEm404() {
        UUID id = UUID.randomUUID();

        ProblemDetail problema = handler.handleNotFound(new FuncionarioNaoEncontradoException(id));

        assertThat(problema.getStatus()).isEqualTo(404);
        assertThat(problema.getDetail()).contains(id.toString());
    }

    @Test
    void deveConverterCpfDuplicadoEm409() {
        ProblemDetail problema = handler.handleConflict(new CpfJaCadastradoException("52998224725"));

        assertThat(problema.getStatus()).isEqualTo(409);
        assertThat(problema.getDetail()).contains("52998224725");
    }

    @Test
    void deveConverterEmailDuplicadoEm409() {
        ProblemDetail problema = handler.handleConflict(new EmailJaCadastradoException("joao@test.com"));

        assertThat(problema.getStatus()).isEqualTo(409);
        assertThat(problema.getDetail()).contains("joao@test.com");
    }

    @Test
    void deveConverterIllegalArgumentExceptionEm400() {
        ProblemDetail problema = handler.handleIllegalArgument(
                new IllegalArgumentException("email invalido: x"));

        assertThat(problema.getStatus()).isEqualTo(400);
        assertThat(problema.getDetail()).isEqualTo("email invalido: x");
    }
}
