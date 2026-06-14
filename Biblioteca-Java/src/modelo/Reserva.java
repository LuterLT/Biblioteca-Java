package modelo;

import java.time.LocalDate;

public class Reserva {
    private int id;
    private Leitor leitor;
    private Livro livro;
    private LocalDate dataReserva;
    private String status; // ATIVA/CANCELADA/ATENDIDA

    public Reserva() {}

    public Reserva(int id, Leitor leitor, Livro livro, LocalDate dataReserva, String status) {
        this.id = id;
        this.leitor = leitor;
        this.livro = livro;
        this.dataReserva = dataReserva;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Leitor getLeitor() { return leitor; }
    public void setLeitor(Leitor leitor) { this.leitor = leitor; }
    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }
    public LocalDate getDataReserva() { return dataReserva; }
    public void setDataReserva(LocalDate dataReserva) { this.dataReserva = dataReserva; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Reserva " + id + " - Livro: " + (livro!=null?livro.getTitulo():"?") + " - Leitor: " + (leitor!=null?leitor.getNome():"?");
    }
}
