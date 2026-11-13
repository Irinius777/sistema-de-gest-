package br.com.brasmoto.gestao_funcionarios.funcionario.infrastructure;

import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Funcionario;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioId;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryFuncionarioRepository implements FuncionarioRepository {

    private final Map<FuncionarioId, Funcionario> store = new ConcurrentHashMap<>();

    @Override
    public Funcionario salvar(Funcionario funcionario) {
        store.put(funcionario.getId(), funcionario);
        return funcionario;
    }

    @Override
    public Optional<Funcionario> buscarPorId(FuncionarioId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Funcionario> listarTodos() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean remover(FuncionarioId id) {
        return store.remove(id) != null;
    }

    @Override
    public boolean existe(FuncionarioId id) {
        return store.containsKey(id);
    }
}