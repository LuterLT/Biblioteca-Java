package modelo;

import exceptions.DadosInvalidosException;

public class Leitor extends Pessoa {
    private String matricula;
    private boolean bloqueado;

    public Leitor() {}

    public Leitor(int id, String nome, String cpf, String matricula) {
        this(id, nome, cpf, matricula, "");
    }

    public Leitor(int id, String nome, String cpf, String matricula, String email) {
        super(id, nome, cpf, email);
        this.matricula = matricula;
        this.bloqueado = false;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public boolean isBloqueado() { return bloqueado; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }

    @Override
    public void validar() throws DadosInvalidosException {
        if (nome == null || nome.isEmpty())
            throw new DadosInvalidosException("Nome não pode ser vazio");

        if (cpf == null || cpf.isEmpty())
            throw new DadosInvalidosException("CPF inválido");

        String cpfSomenteNumeros = cpf.replaceAll("[^0-9]", "");
        if (cpfSomenteNumeros.length() != 11)
            throw new DadosInvalidosException("CPF deve conter 11 dígitos");

        if (matricula == null || matricula.isEmpty())
            throw new DadosInvalidosException("Matrícula inválida");

        if (email == null || !email.contains("@"))
            throw new DadosInvalidosException("E-mail inválido: deve conter @");
    }

    @Override
    public String exibirDados() {
        return "Leitor - " + super.exibirDados() + " - Email: " + email + " - Matricula: " + matricula + (bloqueado?" (BLOQUEADO)":"");
    }

    @Override
    public String toString() {
        return nome + " (" + matricula + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leitor)) return false;
        Leitor leitor = (Leitor) o;
        return id == leitor.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
