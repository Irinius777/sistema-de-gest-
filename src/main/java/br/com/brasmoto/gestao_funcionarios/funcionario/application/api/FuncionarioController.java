package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioRequest;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.dto.FuncionarioResponse;
import br.com.brasmoto.gestao_funcionarios.funcionario.application.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FuncionarioController implements FuncionarioAPI {

    private final FuncionarioService service;

    @Override
    public ResponseEntity<FuncionarioResponse> criar(FuncionarioRequest request) {
        var response = service.criar(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
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
