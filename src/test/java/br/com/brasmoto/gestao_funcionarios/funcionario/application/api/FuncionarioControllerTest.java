package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.service.FuncionarioService;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    void deveCriarFuncionario() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.criar(any())).thenReturn(new FuncionarioResponse(
                id, "Joao Silva", Designacao.DESENVOLVEDOR,
                new BigDecimal("5000.00"), "11999999999", "Rua X, 100",
                "joao@test.com", "52998224725"));

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isCreated())
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
                .andExpect(jsonPath("$.errors").isArray());
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
    void deveBuscarFuncionario() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.buscarPorId(id)).thenReturn(new FuncionarioResponse(
                id, "Joao Silva", Designacao.DESENVOLVEDOR,
                new BigDecimal("5000.00"), "11999999999", "Rua X, 100",
                "joao@test.com", "52998224725"));

        mockMvc.perform(get("/v1/funcionarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
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
    void deveRemoverFuncionario() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/v1/funcionarios/{id}", id))
                .andExpect(status().isNoContent());
    }
}