//CLASSE: Funcionario

public class Funcionario extends Pessoa { //puxando herança
    // Atributos específicos de Funcionario
    private String matricula;
    private String cargo;
    private double salario;

    public Funcionario() {
        super();
        this.matricula = "";
        this.cargo = "";
        this.salario = 0.0;
    }

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

//Get e Set           
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

    //Tentativa de fazer (corrgigi com o GPT)
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
