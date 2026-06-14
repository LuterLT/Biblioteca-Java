package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoBD {
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    // Classe utilitária para gerenciar a conexão com o banco SQLite.
    // O arquivo 'biblioteca.db' será criado automaticamente no diretório do projeto.
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // se o driver não estiver presente, lançamos SQLException para o chamador
            throw new SQLException("Driver SQLite não encontrado: " + e.getMessage());
        }
        Connection conn = DriverManager.getConnection(URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void init() throws SQLException {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            // Cria tabelas necessárias se não existirem
            st.execute("CREATE TABLE IF NOT EXISTS leitor (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, cpf TEXT, email TEXT, matricula TEXT, bloqueado INTEGER)");
            st.execute("CREATE TABLE IF NOT EXISTS funcionario (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, cpf TEXT, email TEXT, cargo TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS livro (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, autor TEXT, isbn TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS copia (id INTEGER PRIMARY KEY AUTOINCREMENT, livro_id INTEGER, disponivel INTEGER, localizacao TEXT, FOREIGN KEY(livro_id) REFERENCES livro(id) ON DELETE CASCADE)");
            st.execute("CREATE TABLE IF NOT EXISTS emprestimo (id INTEGER PRIMARY KEY AUTOINCREMENT, copia_id INTEGER, leitor_id INTEGER, funcionario_id INTEGER, data_emprestimo TEXT, data_devolucao TEXT, multa REAL, FOREIGN KEY(copia_id) REFERENCES copia(id), FOREIGN KEY(leitor_id) REFERENCES leitor(id), FOREIGN KEY(funcionario_id) REFERENCES funcionario(id))");
            st.execute("CREATE TABLE IF NOT EXISTS reserva (id INTEGER PRIMARY KEY AUTOINCREMENT, leitor_id INTEGER, livro_id INTEGER, data_reserva TEXT, status TEXT, FOREIGN KEY(leitor_id) REFERENCES leitor(id), FOREIGN KEY(livro_id) REFERENCES livro(id))");

            // Se já existiam tabelas antigas, acrescenta a coluna email quando ela estiver faltando.
            adicionarColunaSeNaoExistir(conn, "leitor", "email", "TEXT");
            adicionarColunaSeNaoExistir(conn, "funcionario", "email", "TEXT");
        }
    }

    private static void adicionarColunaSeNaoExistir(Connection conn, String tabela, String coluna, String tipo) throws SQLException {
        String sql = "PRAGMA table_info(" + tabela + ")";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            boolean existe = false;
            while (rs.next()) {
                if (coluna.equalsIgnoreCase(rs.getString("name"))) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                try (Statement st2 = conn.createStatement()) {
                    st2.execute("ALTER TABLE " + tabela + " ADD COLUMN " + coluna + " " + tipo);
                }
            }
        }
    }
}
