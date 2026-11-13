package br.com.brasmoto.gestao_funcionarios.funcionario.application.service;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.api.FuncionarioRequest;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.api.FuncionarioResponse;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Cpf;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Designacao;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Email;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Endereco;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Funcionario;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioId;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioNaoEncontradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.FuncionarioRepository;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Salario;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Telefone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuncionarioApplicationService implements FuncionarioService {

    private final FuncionarioRepository repository;

    @Override
    public FuncionarioResponse criar(FuncionarioRequest request) {
        log.info("[inicia] FuncionarioApplicationService - criar");
        var funcionario = new Funcionario(
                FuncionarioId.novo(),
                request.getNome(),
                request.getDesignacao(),
                new Salario(request.getSalario()),
                new Telefone(request.getTelefone()),
                new Endereco(request.getEndereco()),
                new Email(request.getEmail()),
                new Cpf(request.getCpf())
        );
        repository.salvar(funcionario);
        log.info("[finaliza] FuncionarioApplicationService - criar");
        return FuncionarioResponse.from(funcionario);
    }

    @Override
    public FuncionarioResponse buscarPorId(UUID id) {
        log.info("[inicia] FuncionarioApplicationService - buscarPorId");
        var funcionario = repository.buscarPorId(new FuncionarioId(id))
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));
        log.info("[finaliza] FuncionarioApplicationService - buscarPorId");
        return FuncionarioResponse.from(funcionario);
    }

    @Override
    public List<FuncionarioResponse> listar() {
        log.info("[inicia] FuncionarioApplicationService - listar");
        var lista = repository.listarTodos().stream()
                .map(FuncionarioResponse::from).toList();
        log.info("[finaliza] FuncionarioApplicationService - listar");
        return lista;
    }

    @Override
    public FuncionarioResponse atualizar(UUID id, FuncionarioRequest request) {
        log.info("[inicia] FuncionarioApplicationService - atualizar");
        var funcionario = repository.buscarPorId(new FuncionarioId(id))
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));
        funcionario.atualizar(
                request.getNome(),
                request.getDesignacao(),
                new Salario(request.getSalario()),
                new Telefone(request.getTelefone()),
                new Endereco(request.getEndereco()),
                new Email(request.getEmail()),
                new Cpf(request.getCpf())
        );
        repository.salvar(funcionario);
        log.info("[finaliza] FuncionarioApplicationService - atualizar");
        return FuncionarioResponse.from(funcionario);
    }

    @Override
    public void remover(UUID id) {
        log.info("[inicia] FuncionarioApplicationService - remover");
        if (!repository.remover(new FuncionarioId(id))) {
            throw new FuncionarioNaoEncontradoException(id);
        }
        log.info("[finaliza] FuncionarioApplicationService - remover");
    }
}