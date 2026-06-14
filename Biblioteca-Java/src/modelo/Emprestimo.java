package modelo;

import java.time.LocalDate;

public class Emprestimo {
    private int id;
    private Copia copia;
    private Leitor leitor;
    private Funcionario funcionario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private double multa;

    public Emprestimo() {}

    public Emprestimo(int id, Copia copia, Leitor leitor, Funcionario funcionario, LocalDate dataEmprestimo) {
        this.id = id;
        this.copia = copia;
        this.leitor = leitor;
        this.funcionario = funcionario;
        this.dataEmprestimo = dataEmprestimo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Copia getCopia() { return copia; }
    public void setCopia(Copia copia) { this.copia = copia; }
    public Leitor getLeitor() { return leitor; }
    public void setLeitor(Leitor leitor) { this.leitor = leitor; }
    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) { this.dataDevolucao = dataDevolucao; }
    public double getMulta() { return multa; }
    public void setMulta(double multa) { this.multa = multa; }

    @Override
    public String toString() {
        return "Emprestimo " + id + " - Copia: " + (copia!=null?copia.getId():"?") + " - Leitor: " + (leitor!=null?leitor.getNome():"?");
    }
}
