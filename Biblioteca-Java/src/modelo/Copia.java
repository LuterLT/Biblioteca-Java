package modelo;

public class Copia {
    private int id;
    private Livro livro;
    private boolean disponivel;
    private String localizacao;

    public Copia() {}

    public Copia(int id, Livro livro, boolean disponivel, String localizacao) {
        this.id = id;
        this.livro = livro;
        this.disponivel = disponivel;
        this.localizacao = localizacao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    @Override
    public String toString() {
        return "Cópia " + id + " - " + (livro != null ? livro.getTitulo() : "Sem livro") + " (" + (disponivel ? "Disponível" : "Indisponível") + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Copia)) return false;
        Copia copia = (Copia) o;
        return id == copia.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
