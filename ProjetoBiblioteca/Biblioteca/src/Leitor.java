//CLASSE: Leitor

public class Leitor extends Pessoa { //usando herança
    // Atributos específicos de Leitor
    private String matricula;
    private boolean bloqueado;

    public Leitor() {
        super(); // chama o construtor da superclasse Pessoa
        this.matricula = "";
        this.bloqueado = false;
    }
    //exceptions
    public Leitor(int id, String nome, String cpf, String email, String matricula)
            throws DadosInvalidosException {
        super(id, nome, cpf, email); // chama o construtor da superclasse
        if (matricula == null || matricula.trim().isEmpty())
            throw new DadosInvalidosException("Matrícula do leitor não pode ser vazia.");
        this.matricula = matricula;
        this.bloqueado = false;
    }

    // ===== GETTERS E SETTERS =====
    public String getMatricula() { return matricula;}

    public void setMatricula(String matricula) throws DadosInvalidosException {
        if (matricula == null || matricula.trim().isEmpty())
            throw new DadosInvalidosException("Matrícula não pode ser vazia.");
        this.matricula = matricula;
    }

    public boolean isBloqueado() { return bloqueado; }

    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }

    // ===== POLIMORFISMO: implementação específica de exibirDados() =====
    @Override
    public void exibirDados() {
        System.out.println("===== LEITOR =====");
        System.out.println(getDadosBasicos());
        System.out.println("Matrícula: " + matricula);
        System.out.println("Situação: " + (bloqueado ? "BLOQUEADO" : "Ativo"));

    }

    @Override
    public boolean validar() {
        return getNome() != null && !getNome().isEmpty()
            && matricula != null && !matricula.isEmpty()
            && getCpf() != null && getCpf().length() == 11;
    }
}
