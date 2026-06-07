/**
 * CLASSE: Livro
 * 
 * Implementa as interfaces Emprestavel e Cadastravel.
 * Isso é um exemplo de POLIMORFISMO via interface: um Livro pode ser
 * tratado tanto como Emprestavel quanto como Cadastravel em diferentes
 * partes do código.
 * 
 * ENCAPSULAMENTO: todos os atributos são private, com getters e setters.
 * O atributo "disponivel" só muda pelos métodos emprestar() e devolver(),
 * garantindo que a lógica fique encapsulada na classe.
 */
public class Livro implements Emprestavel, Cadastravel {

    // ===== ATRIBUTOS PRIVADOS (Encapsulamento) =====
    private int isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private int anoPublicado;
    private boolean disponivel;

    // ===== CONSTRUTOR SEM PARÂMETROS =====
    public Livro() {
        this.isbn = 0;
        this.titulo = "";
        this.autor = "";
        this.categoria = "";
        this.anoPublicado = 0;
        this.disponivel = true;
    }

    // ===== CONSTRUTOR COM PARÂMETROS =====
    public Livro(int isbn, String titulo, String autor, String categoria,
                 int anoPublicado) throws DadosInvalidosException {
        if (titulo == null || titulo.trim().isEmpty())
            throw new DadosInvalidosException("Título do livro não pode ser vazio.");
        if (autor == null || autor.trim().isEmpty())
            throw new DadosInvalidosException("Autor do livro não pode ser vazio.");
        if (anoPublicado < 1000 || anoPublicado > 2100)
            throw new DadosInvalidosException("Ano de publicação inválido: " + anoPublicado);

        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.anoPublicado = anoPublicado;
        this.disponivel = true; // novo livro sempre começa disponível
    }

    // ===== GETTERS E SETTERS (Encapsulamento) =====
    public int getIsbn() { return isbn; }

    @Override
    public int getId() { return isbn; } // vem da interface Cadastravel

    @Override
    public String getTitulo() { return titulo; } // vem da interface Emprestavel

    public void setTitulo(String titulo) throws DadosInvalidosException {
        if (titulo == null || titulo.trim().isEmpty())
            throw new DadosInvalidosException("Título não pode ser vazio.");
        this.titulo = titulo;
    }

    public String getAutor() { return autor; }

    public void setAutor(String autor) throws DadosInvalidosException {
        if (autor == null || autor.trim().isEmpty())
            throw new DadosInvalidosException("Autor não pode ser vazio.");
        this.autor = autor;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getAnoPublicado() { return anoPublicado; }
    public void setAnoPublicado(int anoPublicado) { this.anoPublicado = anoPublicado; }

    // ===== IMPLEMENTAÇÃO DA INTERFACE Emprestavel =====

    @Override
    public boolean isDisponivel() { return disponivel; }

    @Override
    public void emprestar() {
        // A lógica de disponibilidade fica ENCAPSULADA aqui
        this.disponivel = false;
    }

    @Override
    public void devolver() {
        this.disponivel = true;
    }

    // ===== IMPLEMENTAÇÃO DA INTERFACE Cadastravel =====

    @Override
    public void exibirDados() {
        System.out.println("===== LIVRO =====");
        System.out.println("ISBN: " + isbn);
        System.out.println("Título: " + titulo);
        System.out.println("Autor: " + autor);
        System.out.println("Categoria: " + categoria);
        System.out.println("Ano: " + anoPublicado);
        System.out.println("Disponível: " + (disponivel ? "Sim" : "Não"));
    }

    @Override
    public boolean validar() {
        return titulo != null && !titulo.isEmpty()
            && autor != null && !autor.isEmpty()
            && anoPublicado > 999;
    }
}
