package dao;

import modelo.Leitor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeitorDAO implements DAO<Leitor> {
    @Override
    public Leitor inserir(Leitor l) throws Exception {
        String sql = "INSERT INTO leitor(nome,cpf,email,matricula,bloqueado) VALUES (?,?,?,?,?)";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getNome());
            ps.setString(2, l.getCpf());
            ps.setString(3, l.getEmail());
            ps.setString(4, l.getMatricula());
            ps.setInt(5, l.isBloqueado()?1:0);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) l.setId(rs.getInt(1));
            }
        }
        return l;
    }

    @Override
    public void atualizar(Leitor l) throws Exception {
        String sql = "UPDATE leitor SET nome=?,cpf=?,email=?,matricula=?,bloqueado=? WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getNome());
            ps.setString(2, l.getCpf());
            ps.setString(3, l.getEmail());
            ps.setString(4, l.getMatricula());
            ps.setInt(5, l.isBloqueado()?1:0);
            ps.setInt(6, l.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void remover(int id) throws Exception {
        String sql = "DELETE FROM leitor WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Leitor buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM leitor WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Leitor l = new Leitor();
                    l.setId(rs.getInt("id"));
                    l.setNome(rs.getString("nome"));
                    l.setCpf(rs.getString("cpf"));
                    l.setEmail(rs.getString("email"));
                    l.setMatricula(rs.getString("matricula"));
                    l.setBloqueado(rs.getInt("bloqueado")!=0);
                    return l;
                }
            }
        }
        return null;
    }

    @Override
    public List<Leitor> listarTodos() throws Exception {
        List<Leitor> lista = new ArrayList<>();
        String sql = "SELECT * FROM leitor";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Leitor l = new Leitor();
                l.setId(rs.getInt("id"));
                l.setNome(rs.getString("nome"));
                l.setCpf(rs.getString("cpf"));
                l.setEmail(rs.getString("email"));
                l.setMatricula(rs.getString("matricula"));
                l.setBloqueado(rs.getInt("bloqueado")!=0);
                lista.add(l);
            }
        }
        return lista;
    }
}
