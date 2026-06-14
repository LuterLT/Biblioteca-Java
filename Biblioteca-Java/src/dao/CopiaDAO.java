package dao;

import modelo.Copia;
import modelo.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CopiaDAO implements DAO<Copia> {
    private LivroDAO livroDAO = new LivroDAO();

    @Override
    public Copia inserir(Copia c) throws Exception {
        String sql = "INSERT INTO copia(livro_id,disponivel,localizacao) VALUES (?,?,?)";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getLivro().getId());
            ps.setInt(2, c.isDisponivel()?1:0);
            ps.setString(3, c.getLocalizacao());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) c.setId(rs.getInt(1)); }
        }
        return c;
    }

    @Override
    public void atualizar(Copia c) throws Exception {
        String sql = "UPDATE copia SET livro_id=?,disponivel=?,localizacao=? WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getLivro().getId());
            ps.setInt(2, c.isDisponivel()?1:0);
            ps.setString(3, c.getLocalizacao());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void remover(int id) throws Exception {
        String sql = "DELETE FROM copia WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Copia buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM copia WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) {
                Copia c = new Copia();
                c.setId(rs.getInt("id"));
                int lid = rs.getInt("livro_id");
                Livro l = livroDAO.buscarPorId(lid);
                c.setLivro(l);
                c.setDisponivel(rs.getInt("disponivel")!=0);
                c.setLocalizacao(rs.getString("localizacao"));
                return c;
            }}
        }
        return null;
    }

    @Override
    public List<Copia> listarTodos() throws Exception {
        List<Copia> lista = new ArrayList<>();
        String sql = "SELECT * FROM copia";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Copia c = new Copia();
                c.setId(rs.getInt("id"));
                int lid = rs.getInt("livro_id");
                Livro l = livroDAO.buscarPorId(lid);
                c.setLivro(l);
                c.setDisponivel(rs.getInt("disponivel")!=0);
                c.setLocalizacao(rs.getString("localizacao"));
                lista.add(c);
            }
        }
        return lista;
    }
}
