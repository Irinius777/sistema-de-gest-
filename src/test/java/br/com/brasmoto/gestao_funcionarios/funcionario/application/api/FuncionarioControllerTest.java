package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioResponse;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.service.FuncionarioService;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.CpfJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.EmailJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FuncionarioController.class)
class FuncionarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FuncionarioService service;

    private final String jsonValido = """
            {
              "nome": "Joao Silva",
              "designacao": "DESENVOLVEDOR",
              "salario": 5000.00,
              "telefone": "11999999999",
              "endereco": "Rua X, 100",
              "email": "joao@test.com",
              "cpf": "52998224725"
            }
            """;

    private FuncionarioResponse response(UUID id) {
        return new FuncionarioResponse(
                id,
                "Joao Silva",
                Designacao.DESENVOLVEDOR,
                new BigDecimal("5000.00"),
                "11999999999",
                "Rua X, 100",
                "joao@test.com",
                "52998224725"
        );
    }

    @Test
    void deveCriarFuncionario() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.criar(any())).thenReturn(response(id));

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Joao Silva"))
                .andExpect(jsonPath("$.designacao").value("DESENVOLVEDOR"))
                .andExpect(jsonPath("$.email").value("joao@test.com"))
                .andExpect(jsonPath("$.cpf").value("52998224725"));
    }

    @Test
    void deveRetornar400QuandoCamposObrigatoriosFaltarem() throws Exception {
        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void deveRetornar400QuandoEmailForInvalido() throws Exception {
        String json = jsonValido.replace("joao@test.com", "email-invalido");

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void deveRetornar400QuandoCpfForInvalido() throws Exception {
        String json = jsonValido.replace("52998224725", "12345678901");

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("cpf: cpf invalido"));
    }

    @Test
    void deveRetornar400QuandoSalarioForZero() throws Exception {
        String json = jsonValido.replace("5000.00", "0");

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void deveRetornar400QuandoDesignacaoForInvalida() throws Exception {
        String json = jsonValido.replace("DESENVOLVEDOR", "INVALIDO");

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void deveRetornar400QuandoJsonForInvalido() throws Exception {
        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void deveRetornar400QuandoServicoLancarIllegalArgumentException() throws Exception {
        when(service.criar(any())).thenThrow(new IllegalArgumentException("email invalido"));

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("email invalido"));
    }

    @Test
    void deveRetornar409QuandoCpfDuplicado() throws Exception {
        when(service.criar(any())).thenThrow(new CpfJaCadastradoException("52998224725"));

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("CPF ja cadastrado: 52998224725"));
    }

    @Test
    void deveRetornar409QuandoEmailDuplicado() throws Exception {
        when(service.criar(any())).thenThrow(new EmailJaCadastradoException("joao@test.com"));

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Email ja cadastrado: joao@test.com"));
    }

    @Test
    void deveBuscarFuncionario() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.buscarPorId(id)).thenReturn(response(id));

        mockMvc.perform(get("/v1/funcionarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void deveListarFuncionarios() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.listar()).thenReturn(List.of(response(id)));

        mockMvc.perform(get("/v1/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id.toString()));
    }

    @Test
    void deveRetornar400QuandoIdForInvalido() throws Exception {
        mockMvc.perform(get("/v1/funcionarios/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void deveRetornar404ParaRecursoInexistente() throws Exception {
        mockMvc.perform(get("/nao-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Recurso nao encontrado"));
    }

    @Test
    void deveRetornar404QuandoFuncionarioNaoExistir() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.buscarPorId(id)).thenThrow(new FuncionarioNaoEncontradoException(id));

        mockMvc.perform(get("/v1/funcionarios/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void deveAtualizarFuncionario() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.atualizar(eq(id), any())).thenReturn(response(id));

        mockMvc.perform(put("/v1/funcionarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(service).atualizar(eq(id), any());
    }

    @Test
    void deveRetornar404AoAtualizarFuncionarioInexistente() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.atualizar(eq(id), any())).thenThrow(new FuncionarioNaoEncontradoException(id));

        mockMvc.perform(put("/v1/funcionarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRemoverFuncionario() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/v1/funcionarios/{id}", id))
                .andExpect(status().isNoContent());

        verify(service).remover(id);
    }

    @Test
    void deveRetornar404AoRemoverFuncionarioInexistente() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new FuncionarioNaoEncontradoException(id)).when(service).remover(id);

        mockMvc.perform(delete("/v1/funcionarios/{id}", id))
                .andExpect(status().isNotFound());
    }
}
