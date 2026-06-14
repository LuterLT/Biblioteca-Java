package dao;

import modelo.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO implements DAO<Reserva> {
    private LeitorDAO leitorDAO = new LeitorDAO();
    private LivroDAO livroDAO = new LivroDAO();

    @Override
    public Reserva inserir(Reserva r) throws Exception {
        String sql = "INSERT INTO reserva(leitor_id,livro_id,data_reserva,status) VALUES (?,?,?,?)";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getLeitor().getId());
            ps.setInt(2, r.getLivro().getId());
            ps.setString(3, r.getDataReserva().toString());
            ps.setString(4, r.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) r.setId(rs.getInt(1)); }
        }
        return r;
    }

    @Override
    public void atualizar(Reserva r) throws Exception {
        String sql = "UPDATE reserva SET leitor_id=?,livro_id=?,data_reserva=?,status=? WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getLeitor().getId());
            ps.setInt(2, r.getLivro().getId());
            ps.setString(3, r.getDataReserva().toString());
            ps.setString(4, r.getStatus());
            ps.setInt(5, r.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void remover(int id) throws Exception {
        String sql = "DELETE FROM reserva WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Reserva buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM reserva WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("id"));
                r.setLeitor(leitorDAO.buscarPorId(rs.getInt("leitor_id")));
                r.setLivro(livroDAO.buscarPorId(rs.getInt("livro_id")));
                r.setDataReserva(LocalDate.parse(rs.getString("data_reserva")));
                r.setStatus(rs.getString("status"));
                return r;
            }}
        }
        return null;
    }

    @Override
    public List<Reserva> listarTodos() throws Exception {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reserva";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("id"));
                r.setLeitor(leitorDAO.buscarPorId(rs.getInt("leitor_id")));
                r.setLivro(livroDAO.buscarPorId(rs.getInt("livro_id")));
                r.setDataReserva(LocalDate.parse(rs.getString("data_reserva")));
                r.setStatus(rs.getString("status"));
                lista.add(r);
            }
        }
        return lista;
    }
}
