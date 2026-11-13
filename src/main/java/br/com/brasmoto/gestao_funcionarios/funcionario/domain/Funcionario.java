package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;

public class Funcionario {

    private final FuncionarioId id;
    private String nome;
    private Designacao designacao;
    private Salario salario;
    private Telefone telefone;
    private Endereco endereco;
    private Email email;
    private Cpf cpf;

    public Funcionario(FuncionarioId id, String nome, Designacao designacao,
                       Salario salario, Telefone telefone, Endereco endereco,
                       Email email, Cpf cpf) {
        this.id = Objects.requireNonNull(id, "id nao pode ser nulo");
        this.nome = validarNome(nome);
        this.designacao = Objects.requireNonNull(designacao, "designacao nao pode ser nula");
        this.salario = Objects.requireNonNull(salario, "salario nao pode ser nulo");
        this.telefone = Objects.requireNonNull(telefone, "telefone nao pode ser nulo");
        this.endereco = Objects.requireNonNull(endereco, "endereco nao pode ser nulo");
        this.email = Objects.requireNonNull(email, "email nao pode ser nulo");
        this.cpf = Objects.requireNonNull(cpf, "cpf nao pode ser nulo");
    }

    public void atualizar(String nome, Designacao designacao, Salario salario,
                          Telefone telefone, Endereco endereco, Email email, Cpf cpf) {
        this.nome = validarNome(nome);
        this.designacao = Objects.requireNonNull(designacao, "designacao nao pode ser nula");
        this.salario = Objects.requireNonNull(salario, "salario nao pode ser nulo");
        this.telefone = Objects.requireNonNull(telefone, "telefone nao pode ser nulo");
        this.endereco = Objects.requireNonNull(endereco, "endereco nao pode ser nulo");
        this.email = Objects.requireNonNull(email, "email nao pode ser nulo");
        this.cpf = Objects.requireNonNull(cpf, "cpf nao pode ser nulo");
    }

    private String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome nao pode ser vazio");
        }
        return nome.trim();
    }

    public FuncionarioId getId() { return id; }
    public String getNome() { return nome; }
    public Designacao getDesignacao() { return designacao; }
    public Salario getSalario() { return salario; }
    public Telefone getTelefone() { return telefone; }
    public Endereco getEndereco() { return endereco; }
    public Email getEmail() { return email; }
    public Cpf getCpf() { return cpf; }
}