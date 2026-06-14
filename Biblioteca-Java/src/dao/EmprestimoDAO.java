package dao;

import modelo.Emprestimo;
import modelo.Copia;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO implements DAO<Emprestimo> {
    private CopiaDAO copiaDAO = new CopiaDAO();
    private LeitorDAO leitorDAO = new LeitorDAO();
    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @Override
    public Emprestimo inserir(Emprestimo e) throws Exception {
        String sql = "INSERT INTO emprestimo(copia_id,leitor_id,funcionario_id,data_emprestimo,data_devolucao,multa) VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getCopia().getId());
            ps.setInt(2, e.getLeitor().getId());
            ps.setInt(3, e.getFuncionario().getId());
            ps.setString(4, e.getDataEmprestimo().toString());
            ps.setString(5, null);
            ps.setDouble(6, 0.0);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) e.setId(rs.getInt(1)); }
        }
        // marcar copia indisponivel
        Copia c = e.getCopia();
        c.setDisponivel(false);
        copiaDAO.atualizar(c);
        return e;
    }

    @Override
    public void atualizar(Emprestimo e) throws Exception {
        String sql = "UPDATE emprestimo SET copia_id=?,leitor_id=?,funcionario_id=?,data_emprestimo=?,data_devolucao=?,multa=? WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, e.getCopia().getId());
            ps.setInt(2, e.getLeitor().getId());
            ps.setInt(3, e.getFuncionario().getId());
            ps.setString(4, e.getDataEmprestimo().toString());
            ps.setString(5, e.getDataDevolucao()!=null?e.getDataDevolucao().toString():null);
            ps.setDouble(6, e.getMulta());
            ps.setInt(7, e.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void remover(int id) throws Exception {
        String sql = "DELETE FROM emprestimo WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Emprestimo buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM emprestimo WHERE id=?";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) {
                Emprestimo e = new Emprestimo();
                e.setId(rs.getInt("id"));
                int cid = rs.getInt("copia_id");
                e.setCopia(copiaDAO.buscarPorId(cid));
                e.setLeitor(leitorDAO.buscarPorId(rs.getInt("leitor_id")));
                e.setFuncionario(funcionarioDAO.buscarPorId(rs.getInt("funcionario_id")));
                e.setDataEmprestimo(LocalDate.parse(rs.getString("data_emprestimo")));
                String dev = rs.getString("data_devolucao");
                if (dev != null) e.setDataDevolucao(LocalDate.parse(dev));
                e.setMulta(rs.getDouble("multa"));
                return e;
            }}
        }
        return null;
    }

    @Override
    public List<Emprestimo> listarTodos() throws Exception {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo";
        try (Connection conn = ConexaoBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Emprestimo e = new Emprestimo();
                e.setId(rs.getInt("id"));
                e.setCopia(copiaDAO.buscarPorId(rs.getInt("copia_id")));
                e.setLeitor(leitorDAO.buscarPorId(rs.getInt("leitor_id")));
                e.setFuncionario(funcionarioDAO.buscarPorId(rs.getInt("funcionario_id")));
                e.setDataEmprestimo(LocalDate.parse(rs.getString("data_emprestimo")));
                String dev = rs.getString("data_devolucao");
                if (dev != null) e.setDataDevolucao(LocalDate.parse(dev));
                e.setMulta(rs.getDouble("multa"));
                lista.add(e);
            }
        }
        return lista;
    }

    public void devolver(int emprestimoId, LocalDate dataDevolucao) throws Exception {
        Emprestimo e = buscarPorId(emprestimoId);
        if (e == null) return;
        e.setDataDevolucao(dataDevolucao);
        // calculo simples: multa de 1.0 por dia atrasado, prazo 14 dias
        long dias = java.time.temporal.ChronoUnit.DAYS.between(e.getDataEmprestimo(), dataDevolucao);
        double multa = 0.0;
        if (dias > 14) multa = (dias - 14) * 1.0;
        e.setMulta(multa);
        atualizar(e);
        // liberar copia
        Copia c = e.getCopia();
        c.setDisponivel(true);
        copiaDAO.atualizar(c);
    }
}
