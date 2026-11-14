package br.com.brasmoto.gestao_funcionarios.funcionario.application.service;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioRequest;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioResponse;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Cpf;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.CpfJaCadastradoException;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.Email;
import br.com.brasmoto.gestao_funcionarios.funcionario.domain.EmailJaCadastradoException;
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

import java.util.Comparator;
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
        var salario = new Salario(request.getSalario());
        var telefone = new Telefone(request.getTelefone());
        var endereco = new Endereco(request.getEndereco());
        var email = new Email(request.getEmail());
        var cpf = new Cpf(request.getCpf());

        validarUnicidade(email, cpf, null);

        var funcionario = new Funcionario(
                FuncionarioId.novo(),
                request.getNome(),
                request.getDesignacao(),
                salario,
                telefone,
                endereco,
                email,
                cpf
        );
        repository.salvar(funcionario);
        log.info("[finaliza] FuncionarioApplicationService - criar");
        return FuncionarioResponse.from(funcionario);
    }

    @Override
    public FuncionarioResponse buscarPorId(UUID id) {
        log.info("[inicia] FuncionarioApplicationService - buscarPorId");
        var funcionario = buscarFuncionario(id);
        log.info("[finaliza] FuncionarioApplicationService - buscarPorId");
        return FuncionarioResponse.from(funcionario);
    }

    @Override
    public List<FuncionarioResponse> listar() {
        log.info("[inicia] FuncionarioApplicationService - listar");
        var lista = repository.listarTodos().stream()
                .sorted(Comparator.comparing(Funcionario::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(FuncionarioResponse::from)
                .toList();
        log.info("[finaliza] FuncionarioApplicationService - listar");
        return lista;
    }

    @Override
    public FuncionarioResponse atualizar(UUID id, FuncionarioRequest request) {
        log.info("[inicia] FuncionarioApplicationService - atualizar");
        var funcionario = buscarFuncionario(id);

        var salario = new Salario(request.getSalario());
        var telefone = new Telefone(request.getTelefone());
        var endereco = new Endereco(request.getEndereco());
        var email = new Email(request.getEmail());
        var cpf = new Cpf(request.getCpf());

        validarUnicidade(email, cpf, funcionario.getId());

        var atualizado = funcionario.atualizar(
                request.getNome(),
                request.getDesignacao(),
                salario,
                telefone,
                endereco,
                email,
                cpf
        );
        repository.salvar(atualizado);
        log.info("[finaliza] FuncionarioApplicationService - atualizar");
        return FuncionarioResponse.from(atualizado);
    }

    @Override
    public void remover(UUID id) {
        log.info("[inicia] FuncionarioApplicationService - remover");
        if (!repository.remover(new FuncionarioId(id))) {
            throw new FuncionarioNaoEncontradoException(id);
        }
        log.info("[finaliza] FuncionarioApplicationService - remover");
    }

    private Funcionario buscarFuncionario(UUID id) {
        return repository.buscarPorId(new FuncionarioId(id))
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));
    }

    private void validarUnicidade(Email email, Cpf cpf, FuncionarioId idAtual) {
        repository.buscarPorCpf(cpf)
                .filter(outro -> !outro.getId().equals(idAtual))
                .ifPresent(outro -> {
                    throw new CpfJaCadastradoException(cpf.value());
                });

        repository.buscarPorEmail(email)
                .filter(outro -> !outro.getId().equals(idAtual))
                .ifPresent(outro -> {
                    throw new EmailJaCadastradoException(email.value());
                });
    }
}
