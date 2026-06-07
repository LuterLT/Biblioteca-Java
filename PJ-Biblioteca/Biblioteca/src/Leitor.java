import java.util.ArrayList;
import java.util.List;

/**
 * CLASSE: Leitor
 * 
 * HERANÇA: estende Pessoa — herda id, nome, cpf, email e seus métodos.
 * Adiciona atributos específicos de quem vai emprestar livros.
 * 
 * COMPOSIÇÃO: Leitor "possui" uma lista de Emprestimos.
 * Se o Leitor for removido do sistema, seus empréstimos também somem.
 * Isso é COMPOSIÇÃO (ciclo de vida dependente).
 */
public class Leitor extends Pessoa {

    // Atributos específicos de Leitor
    private String matricula;
    private boolean bloqueado;

    // COMPOSIÇÃO: Leitor compõe seus próprios empréstimos
    private List<Emprestimo> historicoEmprestimos;

    // ===== CONSTRUTOR SEM PARÂMETROS =====
    public Leitor() {
        super(); // chama o construtor da superclasse Pessoa
        this.matricula = "";
        this.bloqueado = false;
        this.historicoEmprestimos = new ArrayList<>();
    }

    // ===== CONSTRUTOR COM PARÂMETROS =====
    public Leitor(int id, String nome, String cpf, String email, String matricula)
            throws DadosInvalidosException {
        super(id, nome, cpf, email); // chama o construtor da superclasse
        if (matricula == null || matricula.trim().isEmpty())
            throw new DadosInvalidosException("Matrícula do leitor não pode ser vazia.");
        this.matricula = matricula;
        this.bloqueado = false;
        this.historicoEmprestimos = new ArrayList<>();
    }

    // ===== GETTERS E SETTERS =====
    public String getMatricula() { return matricula; }

    public void setMatricula(String matricula) throws DadosInvalidosException {
        if (matricula == null || matricula.trim().isEmpty())
            throw new DadosInvalidosException("Matrícula não pode ser vazia.");
        this.matricula = matricula;
    }

    public boolean isBloqueado() { return bloqueado; }

    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }

    public List<Emprestimo> getHistoricoEmprestimos() { return historicoEmprestimos; }

    // Adiciona empréstimo ao histórico
    public void adicionarEmprestimo(Emprestimo emprestimo) {
        this.historicoEmprestimos.add(emprestimo);
    }

    // Conta empréstimos ativos (ainda não devolvidos)
    public int getEmprestimosAtivos() {
        int count = 0;
        for (Emprestimo e : historicoEmprestimos) {
            if (!e.isDevolvido()) count++;
        }
        return count;
    }

    // ===== POLIMORFISMO: implementação específica de exibirDados() =====
    @Override
    public void exibirDados() {
        System.out.println("===== LEITOR =====");
        System.out.println(getDadosBasicos());
        System.out.println("Matrícula: " + matricula);
        System.out.println("Situação: " + (bloqueado ? "BLOQUEADO" : "Ativo"));
        System.out.println("Empréstimos ativos: " + getEmprestimosAtivos());
    }

    @Override
    public boolean validar() {
        return getNome() != null && !getNome().isEmpty()
            && matricula != null && !matricula.isEmpty()
            && getCpf() != null && getCpf().length() == 11;
    }
}
