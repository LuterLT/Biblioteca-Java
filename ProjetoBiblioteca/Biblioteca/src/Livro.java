// CLASSE: Livro
public class Livro {

    // Gerador automático de código identificador (ISBN simulado)
    private static int contadorIsbn = 0;

    // Atributos privados (Encapsulamento)
    private int isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private int anoPublicado;
    private boolean disponivel;

    // Construtor Padrão (Sem parâmetros)
    public Livro() {
        contadorIsbn++;
        this.isbn = contadorIsbn;
        this.titulo = "";
        this.autor = "";
        this.categoria = "";
        this.anoPublicado = 0;
        this.disponivel = true;
    }

    // Construtor Customizado (Com validações de dados)
    public Livro(String titulo, String autor, String categoria, int anoPublicado)
            throws DadosInvalidosException {

        // Validações com estilo modificado
        if (titulo == null || titulo.isBlank()) {
            throw new DadosInvalidosException("O preenchimento do título do livro é obrigatório.");
        }

        if (autor == null || autor.isBlank()) {
            throw new DadosInvalidosException("O nome do autor do livro deve ser informado.");
        }

        // Checagem de ano com limite aceitável de forma simplificada
        if (anoPublicado < 1500 || anoPublicado > 2026) {
            throw new DadosInvalidosException("Ano de publicação fora dos limites aceitáveis: " + anoPublicado);
        }

        contadorIsbn++;
        this.isbn = contadorIsbn;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.anoPublicado = anoPublicado;
        this.disponivel = true;
    }

    // ===== GETTERS E SETTERS =====

    public int getIsbn() {
        return this.isbn;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) throws DadosInvalidosException {
        if (titulo == null || titulo.strip().isEmpty()) {
            throw new DadosInvalidosException("Título inválido: campo em branco.");
        }
        this.titulo = titulo;
    }

    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) throws DadosInvalidosException {
        if (autor == null || autor.strip().isEmpty()) {
            throw new DadosInvalidosException("Autor inválido: campo em branco.");
        }
        this.autor = autor;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getAnoPublicado() {
        return this.anoPublicado;
    }

    public void setAnoPublicado(int anoPublicado) throws DadosInvalidosException {
        if (anoPublicado < 1500 || anoPublicado > 2026) {
            throw new DadosInvalidosException("Não foi possível alterar. Ano inválido.");
        }
        this.anoPublicado = anoPublicado;
    }

    public boolean isDisponivel() {
        return this.disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    // ===== MÉTODOS DE REGRA DE NEGÓCIO (ESSENCIAIS PARA O EMPRÉSTIMO) =====

    // Altera o status quando o livro sai da biblioteca
    public void emprestar() {
        this.disponivel = false;
    }

    // Altera o status quando o livro retorna para a biblioteca
    public void devolver() {
        this.disponivel = true;
    }

    // ===== FORMATAÇÃO DE TEXTO =====

    @Override
    public String toString() {
        return "Obra: " + this.titulo + " [Reg: " + this.isbn + "] | Autor: " + this.autor + 
               " | Gênero: " + this.categoria + " (" + this.anoPublicado + ") | Status: " + 
               (this.disponivel ? "Disponível para retirada" : "Emprestado");
    }
}