package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioRequest;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@Tag(name = "Funcionarios", description = "Gestao de funcionarios")
@RequestMapping("/v1/funcionarios")
public interface FuncionarioAPI {

    @Operation(summary = "Criar funcionario",
            description = "Cria um novo funcionario e retorna o cabecalho Location com a URI do recurso")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Funcionario criado",
                    headers = @Header(name = "Location", description = "URI do recurso criado",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "CPF ou email ja cadastrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    ResponseEntity<FuncionarioResponse> criar(@Valid @RequestBody FuncionarioRequest request);

    @Operation(summary = "Buscar funcionario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Funcionario encontrado"),
            @ApiResponse(responseCode = "400", description = "ID invalido",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Funcionario nao encontrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    FuncionarioResponse buscarPorId(@PathVariable UUID id);

    @Operation(summary = "Listar todos os funcionarios",
            description = "Retorna os funcionarios em ordem alfabetica de nome")
    @ApiResponse(responseCode = "200", description = "Lista de funcionarios")
    @GetMapping
    List<FuncionarioResponse> listar();

    @Operation(summary = "Atualizar funcionario",
            description = "Substitui todos os dados do funcionario informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Funcionario atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Funcionario nao encontrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "CPF ou email ja cadastrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    FuncionarioResponse atualizar(@PathVariable UUID id,
                                  @Valid @RequestBody FuncionarioRequest request);

    @Operation(summary = "Remover funcionario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Funcionario removido"),
            @ApiResponse(responseCode = "400", description = "ID invalido",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Funcionario nao encontrado",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void remover(@PathVariable UUID id);
}
