package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Designacao/cargo do funcionario", example = "DESENVOLVEDOR")
public enum Designacao {
    DESENVOLVEDOR,
    ANALISTA,
    GERENTE,
    ESTAGIARIO,
    SUPORTE
}
