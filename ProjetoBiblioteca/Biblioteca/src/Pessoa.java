//  CLASSE ABSTRATA: Pessoa
//Feita para podermos usar Herança com o Leitor e Funcionario  
//  

public abstract class Pessoa implements Cadastravel {

    // Encapsulamento
    private int id; //Para ser codigo unico a cada cadastro, queria colocar aquele que vai aumentando automaticamente.
    private String nome;
    private String cpf;
    private String email;

    public Pessoa() {
        this.id = 0;
        this.nome = "";
        this.cpf = "";
        this.email = "";
    }

    //Copiei do trabalho do professor
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

    // GET E SET (Encapsulamento), caso queira alterar a vontade. 
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


    // Subclasses herdam este comportamento
    public String getDadosBasicos() {
        return "ID: " + id + " | Nome: " + nome + " | CPF: " + cpf + " | Email: " + email;
    }
}

