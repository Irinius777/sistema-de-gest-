package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository {
    Funcionario salvar(Funcionario funcionario);
    Optional<Funcionario> buscarPorId(FuncionarioId id);
    Optional<Funcionario> buscarPorCpf(Cpf cpf);
    Optional<Funcionario> buscarPorEmail(Email email);
    List<Funcionario> listarTodos();
    boolean remover(FuncionarioId id);
}
