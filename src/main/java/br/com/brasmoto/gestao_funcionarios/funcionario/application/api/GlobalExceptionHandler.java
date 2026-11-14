package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import br.com.brasmoto.gestao_funcionarios.funcionario.domain.CpfJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.EmailJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioNaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FuncionarioNaoEncontradoException.class)
    public ProblemDetail handleNotFound(FuncionarioNaoEncontradoException ex) {
        log.warn("Funcionario nao encontrado: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({CpfJaCadastradoException.class, EmailJaCadastradoException.class})
    public ProblemDetail handleConflict(RuntimeException ex) {
        log.warn("Conflito de unicidade: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Um ou mais campos sao invalidos");
        pd.setTitle("Erro de validacao");

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        pd.setProperty("errors", errors);
        log.warn("Erro de validacao: {}", errors);
        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex) {
        log.warn("Requisicao com corpo invalido: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Corpo da requisicao invalido ou mal formatado");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Dados invalidos: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Parametro invalido: {}={}", ex.getName(), ex.getValue());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Parametro invalido: " + ex.getName());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResource(NoResourceFoundException ex) {
        log.warn("Recurso nao encontrado: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Recurso nao encontrado");
    }
}
