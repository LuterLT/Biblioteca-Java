package modelo;

import exceptions.DadosInvalidosException;

public class Funcionario extends Pessoa {
    private String cargo;

    public Funcionario() {}

    public Funcionario(int id, String nome, String cpf, String cargo) {
        this(id, nome, cpf, cargo, "");
    }

    public Funcionario(int id, String nome, String cpf, String cargo, String email) {
        super(id, nome, cpf, email);
        this.cargo = cargo;
    }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    @Override
    public void validar() throws DadosInvalidosException {
        if (nome == null || nome.isEmpty())
            throw new DadosInvalidosException("Nome não pode ser vazio");

        if (cpf == null || cpf.isEmpty())
            throw new DadosInvalidosException("CPF inválido");

        String cpfSomenteNumeros = cpf.replaceAll("[^0-9]", "");
        if (cpfSomenteNumeros.length() != 11)
            throw new DadosInvalidosException("CPF deve conter 11 dígitos");

        if (cargo == null || cargo.isEmpty())
            throw new DadosInvalidosException("Cargo inválido");

        if (email == null || !email.contains("@"))
            throw new DadosInvalidosException("E-mail inválido: deve conter @");
    }

    @Override
    public String exibirDados() {
        return "Funcionario - " + super.exibirDados() + " - Email: " + email + " - Cargo: " + cargo;
    }

    @Override
    public String toString() {
        return nome + " (" + cargo + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Funcionario)) return false;
        Funcionario funcionario = (Funcionario) o;
        return id == funcionario.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
