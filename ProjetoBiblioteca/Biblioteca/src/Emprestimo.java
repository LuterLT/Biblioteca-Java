import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
//Classe Emprestimo

public class Emprestimo {
    private static int contadorId = 1; // gerador automático de ID (peguei do google)

    private int id;
    private Leitor leitor;         // ASSOCIAÇÃO com Leitor
    private Livro livro;           // ASSOCIAÇÃO com Livro
    private Funcionario responsavel; // ASSOCIAÇÃO com Funcionario que registrou
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucaoReal;
    private boolean devolvido;

private static final int DIAS_PRAZO = 14; // prazo padrão em dias VER SE COLOCAMOS OU NAO
private static final double MULTA_POR_DIA = 0.50; // R$ 0,50 por dia de atraso VE SE COLOCAMOS OU NAO

    public Emprestimo() {
        this.id = contadorId++;
        this.devolvido = false;
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevistaDevolucao = LocalDate.now().plusDays(DIAS_PRAZO);
    }

    public Emprestimo(Leitor leitor, Livro livro, Funcionario responsavel)
            throws LivroIndisponivelException, LeitorBloqueadoException, DadosInvalidosException {

        // Exceções: validação antes de criar o empréstimo
        if (leitor == null) throw new DadosInvalidosException("Leitor não pode ser nulo.");
        if (livro == null)  throw new DadosInvalidosException("Livro não pode ser nulo.");

        if (!livro.isDisponivel())
            throw new LivroIndisponivelException(livro.getTitulo());
        if (leitor.isBloqueado())
            throw new LeitorBloqueadoException(leitor.getNome());

        this.id = contadorId++;
        this.leitor = leitor;
        this.livro = livro;
        this.responsavel = responsavel;
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevistaDevolucao = LocalDate.now().plusDays(DIAS_PRAZO);
        this.devolvido = false;

        // Marca o livro como indisponível (encapsulado no Livro)
        livro.emprestar();
}

    //Getters
    public int getId() { return id; }
    public Leitor getLeitor() { return leitor; }
    public Livro getLivro() { return livro; }
    public Funcionario getResponsavel() { return responsavel; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public boolean isDevolvido() { return devolvido; }

    //Registrar devolução =====
    public double registrarDevolucao() {
        this.dataDevolucaoReal = LocalDate.now();
        this.devolvido = true;
        livro.devolver(); // libera o livro

        // Verifica se está em atraso e calcula multa
        double multa = calcularMulta();
        if (multa > 0) {
            System.out.printf("  ⚠ Devolução em atraso! Multa: R$ %.2f%n", multa);
        }
        return multa;
    }

    // Calcular multa por atraso =====
    public double calcularMulta() {
        LocalDate referencia = (dataDevolucaoReal != null) ? dataDevolucaoReal : LocalDate.now();
        long diasAtraso = ChronoUnit.DAYS.between(dataPrevistaDevolucao, referencia);
        if (diasAtraso > 0) {
            return diasAtraso * MULTA_POR_DIA;
        }
        return 0.0;
    }

    //Verificar se está em atraso
    public boolean isEmAtraso() {
        if (devolvido) return false;
        return LocalDate.now().isAfter(dataPrevistaDevolucao);
    }

    public void exibirDados() {
        System.out.println("--- Empréstimo #" + id + " ---");
        System.out.println("Livro: " + (livro != null ? livro.getTitulo() : "N/A"));
        System.out.println("Leitor: " + (leitor != null ? leitor.getNome() : "N/A"));
        System.out.println("Responsável: " + (responsavel != null ? responsavel.getNome() : "N/A"));
        System.out.println("Data empréstimo: " + dataEmprestimo);
        System.out.println("Devolução prevista: " + dataPrevistaDevolucao);
        System.out.println("Devolvido: " + (devolvido ? "Sim (" + dataDevolucaoReal + ")" : "Não"));
        if (isEmAtraso()) System.out.printf("EM ATRASO! Multa atual: R$ %.2f%n", calcularMulta());
    }
}

