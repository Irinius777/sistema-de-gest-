package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FuncionarioController implements FuncionarioAPI {

    private final FuncionarioService service;

    @Override
    public FuncionarioResponse criar(FuncionarioRequest request) {
        return service.criar(request);
    }

    @Override
    public FuncionarioResponse buscarPorId(UUID id) {
        return service.buscarPorId(id);
    }

    @Override
    public List<FuncionarioResponse> listar() {
        return service.listar();
    }

    @Override
    public FuncionarioResponse atualizar(UUID id, FuncionarioRequest request) {
        return service.atualizar(id, request);
    }

    @Override
    public void remover(UUID id) {
        service.remover(id);
    }
}