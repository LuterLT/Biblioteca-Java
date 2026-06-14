package ui;

import dao.EmprestimoDAO;
import dao.CopiaDAO;
import dao.LeitorDAO;
import dao.FuncionarioDAO;
import modelo.Emprestimo;
import modelo.Copia;
import modelo.Leitor;
import modelo.Funcionario;
import ui.Refreshable;
import exceptions.LivroIndisponivelException;
import exceptions.LeitorBloqueadoException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TelaEmprestimos extends JPanel implements Refreshable {
    private EmprestimoDAO dao = new EmprestimoDAO();
    private CopiaDAO copiaDAO = new CopiaDAO();
    private LeitorDAO leitorDAO = new LeitorDAO();
    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private JTable tabela;
    private DefaultTableModel modelo;
    private JComboBox<Copia> cbxCopia;
    private JComboBox<Leitor> cbxLeitor;
    private JComboBox<Funcionario> cbxFuncionario;
    private JLabel lblData, lblDevolucao, lblMulta;
    private Emprestimo selecionado = null;

    public TelaEmprestimos() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel pnlTabela = new JPanel(new BorderLayout());
        modelo = new DefaultTableModel(new String[]{"ID", "Cópia", "Leitor", "Funcionário", "Empréstimo", "Devolução", "Multa"}, 0);
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> selecionarLinha());
        JScrollPane scroll = new JScrollPane(tabela);
        pnlTabela.add(scroll, BorderLayout.CENTER);
        add(pnlTabela, BorderLayout.CENTER);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Formulário"));

        pnlForm.add(new JLabel("Cópia:"));
        cbxCopia = new JComboBox<>();
        carregarCopias();
        pnlForm.add(cbxCopia);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("Leitor:"));
        cbxLeitor = new JComboBox<>();
        carregarLeitores();
        pnlForm.add(cbxLeitor);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("Funcionário:"));
        cbxFuncionario = new JComboBox<>();
        carregarFuncionarios();
        pnlForm.add(cbxFuncionario);
        pnlForm.add(Box.createVerticalStrut(5));

        lblData = new JLabel("Empréstimo: -");
        pnlForm.add(lblData);
        lblDevolucao = new JLabel("Devolução: -");
        pnlForm.add(lblDevolucao);
        lblMulta = new JLabel("Multa: -");
        pnlForm.add(lblMulta);
        pnlForm.add(Box.createVerticalStrut(10));

        JPanel pnlBotoes = new JPanel();
        JButton btnSalvar = new JButton("Emprestar");
        btnSalvar.addActionListener(e -> salvar());
        JButton btnDevolver = new JButton("Devolver");
        btnDevolver.addActionListener(e -> devolver());
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizar());
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limpar());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnDevolver);
        pnlBotoes.add(btnAtualizar);
        pnlBotoes.add(btnLimpar);
        pnlForm.add(pnlBotoes);
        pnlForm.add(Box.createVerticalGlue());

        add(pnlForm, BorderLayout.EAST);
        recarregarTabela();
    }

    private void carregarCopias() {
        try {
            cbxCopia.removeAllItems();
            List<Copia> lista = copiaDAO.listarTodos();
            for (Copia c : lista) {
                cbxCopia.addItem(c);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarLeitores() {
        try {
            cbxLeitor.removeAllItems();
            List<Leitor> lista = leitorDAO.listarTodos();
            for (Leitor l : lista) {
                cbxLeitor.addItem(l);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarFuncionarios() {
        try {
            cbxFuncionario.removeAllItems();
            List<Funcionario> lista = funcionarioDAO.listarTodos();
            for (Funcionario f : lista) {
                cbxFuncionario.addItem(f);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recarregarTabela() {
        try {
            modelo.setRowCount(0);
            List<Emprestimo> lista = dao.listarTodos();
            for (Emprestimo e : lista) {
                modelo.addRow(new Object[]{
                    e.getId(),
                    e.getCopia() != null ? e.getCopia().getId() : "?",
                    e.getLeitor() != null ? e.getLeitor().getNome() : "?",
                    e.getFuncionario() != null ? e.getFuncionario().getNome() : "?",
                    e.getDataEmprestimo(),
                    e.getDataDevolucao() != null ? e.getDataDevolucao() : "-",
                    String.format("R$ %.2f", e.getMulta())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selecionarLinha() {
        int row = tabela.getSelectedRow();
        if (row >= 0) {
            try {
                int id = (Integer) modelo.getValueAt(row, 0);
                selecionado = dao.buscarPorId(id);
                if (selecionado != null) {
                    cbxCopia.setSelectedItem(selecionado.getCopia());
                    cbxLeitor.setSelectedItem(selecionado.getLeitor());
                    cbxFuncionario.setSelectedItem(selecionado.getFuncionario());
                    lblData.setText("Empréstimo: " + selecionado.getDataEmprestimo());
                    lblDevolucao.setText("Devolução: " + (selecionado.getDataDevolucao() != null ? selecionado.getDataDevolucao() : "-"));
                    lblMulta.setText(String.format("Multa: R$ %.2f", selecionado.getMulta()));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    @Override
    public void refreshData() {
        carregarCopias();
        carregarLeitores();
        carregarFuncionarios();
        recarregarTabela();
        limpar();
    }

    private void salvar() {
        try {
            if (cbxCopia.getSelectedItem() == null || cbxLeitor.getSelectedItem() == null || cbxFuncionario.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione cópia, leitor e funcionário!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Copia c = (Copia) cbxCopia.getSelectedItem();
            Leitor l = (Leitor) cbxLeitor.getSelectedItem();
            Funcionario f = (Funcionario) cbxFuncionario.getSelectedItem();

            if (!c.isDisponivel()) {
                throw new LivroIndisponivelException("Cópia não está disponível!");
            }
            if (l.isBloqueado()) {
                throw new LeitorBloqueadoException("Leitor está bloqueado!");
            }

            Emprestimo emp = new Emprestimo(0, c, l, f, LocalDate.now());
            dao.inserir(emp);
            JOptionPane.showMessageDialog(this, "Empréstimo realizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            carregarCopias();
            recarregarTabela();
        } catch (LivroIndisponivelException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (LeitorBloqueadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolver() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selecionado.getDataDevolucao() != null) {
            JOptionPane.showMessageDialog(this, "Empréstimo já foi devolvido!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            dao.devolver(selecionado.getId(), LocalDate.now());
            selecionado = dao.buscarPorId(selecionado.getId());
            JOptionPane.showMessageDialog(this, String.format("Devolução realizada! Multa: R$ %.2f", selecionado.getMulta()), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            recarregarTabela();
            selecionarLinha();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Atualiza os dados dos combos (cópias, leitores, funcionários) com o que está no banco
    private void atualizar() {
        carregarCopias();
        carregarLeitores();
        carregarFuncionarios();
        recarregarTabela();
        limpar();
        JOptionPane.showMessageDialog(this, "Dados atualizados!", "Atualizar", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpar() {
        selecionado = null;
        cbxCopia.setSelectedIndex(-1);      // nenhuma cópia selecionada
        cbxLeitor.setSelectedIndex(-1);     // nenhum leitor selecionado
        cbxFuncionario.setSelectedIndex(-1); // nenhum funcionário selecionado
        lblData.setText("Empréstimo: -");
        lblDevolucao.setText("Devolução: -");
        lblMulta.setText("Multa: -");
        tabela.clearSelection();
    }
}
