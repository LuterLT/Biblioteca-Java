/**
 * CLASSE ABSTRATA: Pessoa
 * 
 * Superclasse que contém atributos e comportamentos COMUNS a
 * Leitor e Funcionario. Isso é HERANÇA — evita repetição de código.
 * 
 * É "abstrata" porque não faz sentido criar um "Pessoa" genérico
 * no sistema — apenas Leitor ou Funcionario existem de verdade.
 * 
 * ENCAPSULAMENTO: todos os atributos são "private".
 * O acesso de fora é feito apenas pelos métodos get/set.
 */
public abstract class Pessoa implements Cadastravel {

    // ===== ATRIBUTOS PRIVADOS (Encapsulamento) =====
    private int id;
    private String nome;
    private String cpf;
    private String email;

    // ===== CONSTRUTOR SEM PARÂMETROS =====
    public Pessoa() {
        this.id = 0;
        this.nome = "";
        this.cpf = "";
        this.email = "";
    }

    // ===== CONSTRUTOR COM PARÂMETROS =====
    public Pessoa(int id, String nome, String cpf, String email) throws DadosInvalidosException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadosInvalidosException("Nome da pessoa não pode ser vazio.");
        }
        if (cpf == null || cpf.length() != 11) {
            throw new DadosInvalidosException("CPF deve ter 11 dígitos (sem pontuação).");
        }
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    // ===== GETTERS E SETTERS (Encapsulamento) =====
    @Override
    public int getId() { return id; }

    public void setId(int id) throws DadosInvalidosException {
        if (id <= 0) throw new DadosInvalidosException("ID deve ser maior que zero.");
        this.id = id;
    }

    public String getNome() { return nome; }

    public void setNome(String nome) throws DadosInvalidosException {
        if (nome == null || nome.trim().isEmpty())
            throw new DadosInvalidosException("Nome não pode ser vazio.");
        this.nome = nome;
    }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) throws DadosInvalidosException {
        if (cpf == null || cpf.length() != 11)
            throw new DadosInvalidosException("CPF deve ter 11 dígitos.");
        this.cpf = cpf;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    // ===== MÉTODO CONCRETO NA SUPERCLASSE =====
    // Subclasses herdam este comportamento
    public String getDadosBasicos() {
        return "ID: " + id + " | Nome: " + nome + " | CPF: " + cpf + " | Email: " + email;
    }

    // ===== MÉTODO ABSTRATO =====
    // Cada subclasse É OBRIGADA a implementar do seu jeito (Polimorfismo)
    @Override
    public abstract void exibirDados();

    @Override
    public abstract boolean validar();
}
