package br.com.brasmoto.gestao_funcionarios.funcionario.domain;

import java.util.Objects;

public class Funcionario {

    private static final int NOME_MAX_LENGTH = 150;

    private final FuncionarioId id;
    private final String nome;
    private final Designacao designacao;
    private final Salario salario;
    private final Telefone telefone;
    private final Endereco endereco;
    private final Email email;
    private final Cpf cpf;

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

    public Funcionario atualizar(String nome, Designacao designacao, Salario salario,
                                 Telefone telefone, Endereco endereco, Email email, Cpf cpf) {
        return new Funcionario(id, nome, designacao, salario, telefone, endereco, email, cpf);
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome nao pode ser vazio");
        }
        String normalizado = nome.trim();
        if (normalizado.length() > NOME_MAX_LENGTH) {
            throw new IllegalArgumentException("nome deve ter no maximo " + NOME_MAX_LENGTH + " caracteres");
        }
        return normalizado;
    }

    public FuncionarioId getId() { return id; }
    public String getNome() { return nome; }
    public Designacao getDesignacao() { return designacao; }
    public Salario getSalario() { return salario; }
    public Telefone getTelefone() { return telefone; }
    public Endereco getEndereco() { return endereco; }
    public Email getEmail() { return email; }
    public Cpf getCpf() { return cpf; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Funcionario outro)) {
            return false;
        }
        return id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
