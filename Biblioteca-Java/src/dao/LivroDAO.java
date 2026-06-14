package dao;

import modelo.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO implements DAO<Livro> {
    @Override
    public Livro inserir(Livro l) throws Exception {
        String sql = "INSERT INTO livro(titulo,autor,isbn) VALUES (?,?,?)";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) l.setId(rs.getInt(1)); }
        }
        return l;
    }

    @Override
    public void atualizar(Livro l) throws Exception {
        String sql = "UPDATE livro SET titulo=?,autor=?,isbn=? WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setInt(4, l.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void remover(int id) throws Exception {
        String sql = "DELETE FROM livro WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Livro buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM livro WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) {
                Livro l = new Livro();
                l.setId(rs.getInt("id"));
                l.setTitulo(rs.getString("titulo"));
                l.setAutor(rs.getString("autor"));
                l.setIsbn(rs.getString("isbn"));
                return l;
            }}
        }
        return null;
    }

    @Override
    public List<Livro> listarTodos() throws Exception {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM livro";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Livro l = new Livro();
                l.setId(rs.getInt("id"));
                l.setTitulo(rs.getString("titulo"));
                l.setAutor(rs.getString("autor"));
                l.setIsbn(rs.getString("isbn"));
                lista.add(l);
            }
        }
        return lista;
    }
}
