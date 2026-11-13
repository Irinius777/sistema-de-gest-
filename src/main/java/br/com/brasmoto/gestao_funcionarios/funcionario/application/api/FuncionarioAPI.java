package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Funcionarios", description = "Gestao de funcionarios")
@RestController
@RequestMapping("/v1/funcionarios")
public interface FuncionarioAPI {

    @Operation(summary = "Criar funcionario")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    FuncionarioResponse criar(@Valid @RequestBody FuncionarioRequest request);

    @Operation(summary = "Buscar funcionario por ID")
    @GetMapping("/{id}")
    FuncionarioResponse buscarPorId(@PathVariable UUID id);

    @Operation(summary = "Listar todos os funcionarios")
    @GetMapping
    List<FuncionarioResponse> listar();

    @Operation(summary = "Atualizar funcionario")
    @PutMapping("/{id}")
    FuncionarioResponse atualizar(@PathVariable UUID id,
                                 @Valid @RequestBody FuncionarioRequest request);

    @Operation(summary = "Remover funcionario")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void remover(@PathVariable UUID id);
}