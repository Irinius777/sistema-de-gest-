package br.com.brasmoto.gestao_funcionarios.funcionario.application.service;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.api.FuncionarioRequest;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.api.FuncionarioResponse;

import java.util.List;
import java.util.UUID;

public interface FuncionarioService {
    FuncionarioResponse criar(FuncionarioRequest request);
    FuncionarioResponse buscarPorId(UUID id);
    List<FuncionarioResponse> listar();
    FuncionarioResponse atualizar(UUID id, FuncionarioRequest request);
    void remover(UUID id);
}