/**
 * CLASSE: Funcionario
 * 
 * HERANÇA: estende Pessoa — herda id, nome, cpf, email e seus métodos.
 * Adiciona atributos específicos de um funcionário da biblioteca.
 * 
 * POLIMORFISMO: implementa exibirDados() de forma diferente do Leitor,
 * mesmo sendo chamado pelo mesmo método da superclasse Pessoa.
 */
public class Funcionario extends Pessoa {

    // Atributos específicos de Funcionario
    private String matricula;
    private String cargo;
    private double salario;

    // ===== CONSTRUTOR SEM PARÂMETROS =====
    public Funcionario() {
        super();
        this.matricula = "";
        this.cargo = "";
        this.salario = 0.0;
    }

    // ===== CONSTRUTOR COM PARÂMETROS =====
    public Funcionario(int id, String nome, String cpf, String email, String matricula,
                       String cargo, double salario) throws DadosInvalidosException {
        super(id, nome, cpf, email);
        if (cargo == null || cargo.trim().isEmpty())
            throw new DadosInvalidosException("Cargo do funcionário não pode ser vazio.");
        if (salario < 0)
            throw new DadosInvalidosException("Salário não pode ser negativo.");
        this.matricula = matricula;
        this.cargo = cargo;
        this.salario = salario;
    }

    // ===== GETTERS E SETTERS =====
    public String getMatricula() { return matricula; }

    public void setMatricula(String matricula) throws DadosInvalidosException {
        if (matricula == null || matricula.trim().isEmpty())
            throw new DadosInvalidosException("Matrícula não pode ser vazia.");
        this.matricula = matricula;
    }

    public String getCargo() { return cargo; }

    public void setCargo(String cargo) throws DadosInvalidosException {
        if (cargo == null || cargo.trim().isEmpty())
            throw new DadosInvalidosException("Cargo não pode ser vazio.");
        this.cargo = cargo;
    }

    public double getSalario() { return salario; }

    public void setSalario(double salario) throws DadosInvalidosException {
        if (salario < 0) throw new DadosInvalidosException("Salário não pode ser negativo.");
        this.salario = salario;
    }

    // ===== POLIMORFISMO: implementação específica de exibirDados() =====
    @Override
    public void exibirDados() {
        System.out.println("===== FUNCIONÁRIO =====");
        System.out.println(getDadosBasicos());
        System.out.println("Matrícula: " + matricula);
        System.out.println("Cargo: " + cargo);
        System.out.printf("Salário: R$ %.2f%n", salario);
    }

    @Override
    public boolean validar() {
        return getNome() != null && !getNome().isEmpty()
            && cargo != null && !cargo.isEmpty()
            && salario >= 0;
    }
}
