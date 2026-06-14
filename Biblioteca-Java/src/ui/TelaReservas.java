package ui;

import dao.ReservaDAO;
import dao.LeitorDAO;
import dao.LivroDAO;
import modelo.Reserva;
import modelo.Leitor;
import modelo.Livro;
import ui.Refreshable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TelaReservas extends JPanel implements Refreshable {
    private ReservaDAO dao = new ReservaDAO();
    private LeitorDAO leitorDAO = new LeitorDAO();
    private LivroDAO livroDAO = new LivroDAO();
    private JTable tabela;
    private DefaultTableModel modelo;
    private JComboBox<Leitor> cbxLeitor;
    private JComboBox<Livro> cbxLivro;
    private JComboBox<String> cbxStatus;
    private JLabel lblData;
    private Reserva selecionado = null;

    public TelaReservas() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel pnlTabela = new JPanel(new BorderLayout());
        modelo = new DefaultTableModel(new String[]{"ID", "Leitor", "Livro", "Data Reserva", "Status"}, 0);
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> selecionarLinha());
        JScrollPane scroll = new JScrollPane(tabela);
        pnlTabela.add(scroll, BorderLayout.CENTER);
        add(pnlTabela, BorderLayout.CENTER);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Formulário"));

        pnlForm.add(new JLabel("Leitor:"));
        cbxLeitor = new JComboBox<>();
        carregarLeitores();
        pnlForm.add(cbxLeitor);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("Livro:"));
        cbxLivro = new JComboBox<>();
        carregarLivros();
        pnlForm.add(cbxLivro);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("Status:"));
        cbxStatus = new JComboBox<>(new String[]{"ATIVA", "CANCELADA", "ATENDIDA"});
        pnlForm.add(cbxStatus);
        pnlForm.add(Box.createVerticalStrut(5));

        lblData = new JLabel("Data: -");
        pnlForm.add(lblData);
        pnlForm.add(Box.createVerticalStrut(10));

        JPanel pnlBotoes = new JPanel();
        JButton btnSalvar = new JButton("Reservar");
        btnSalvar.addActionListener(e -> salvar());
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizar());
        JButton btnCancelar = new JButton("Cancelar Reserva");
        btnCancelar.addActionListener(e -> cancelar());
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limpar());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnAtualizar);
        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnLimpar);
        pnlForm.add(pnlBotoes);
        pnlForm.add(Box.createVerticalGlue());

        add(pnlForm, BorderLayout.EAST);
        recarregarTabela();
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

    private void carregarLivros() {
        try {
            cbxLivro.removeAllItems();
            List<Livro> lista = livroDAO.listarTodos();
            for (Livro l : lista) {
                cbxLivro.addItem(l);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recarregarTabela() {
        try {
            modelo.setRowCount(0);
            List<Reserva> lista = dao.listarTodos();
            for (Reserva r : lista) {
                modelo.addRow(new Object[]{
                    r.getId(),
                    r.getLeitor() != null ? r.getLeitor().getNome() : "?",
                    r.getLivro() != null ? r.getLivro().getTitulo() : "?",
                    r.getDataReserva(),
                    r.getStatus()
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
                    cbxLeitor.setSelectedItem(selecionado.getLeitor());
                    cbxLivro.setSelectedItem(selecionado.getLivro());
                    cbxStatus.setSelectedItem(selecionado.getStatus());
                    lblData.setText("Data: " + selecionado.getDataReserva());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void salvar() {
        try {
            if (cbxLeitor.getSelectedItem() == null || cbxLivro.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione leitor e livro!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Leitor l = (Leitor) cbxLeitor.getSelectedItem();
            Livro liv = (Livro) cbxLivro.getSelectedItem();

            // Sempre cria uma nova reserva
            Reserva r = new Reserva(0, l, liv, LocalDate.now(), "ATIVA");
            dao.inserir(r);
            JOptionPane.showMessageDialog(this, "Reserva criada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            recarregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva para atualizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (cbxLeitor.getSelectedItem() == null || cbxLivro.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione leitor e livro!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            selecionado.setLeitor((Leitor) cbxLeitor.getSelectedItem());
            selecionado.setLivro((Livro) cbxLivro.getSelectedItem());
            selecionado.setStatus((String) cbxStatus.getSelectedItem());
            dao.atualizar(selecionado);
            JOptionPane.showMessageDialog(this, "Reserva atualizada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            recarregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        // Cancela a reserva selecionada (muda status para CANCELADA)
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva para cancelar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            selecionado.setStatus("CANCELADA");
            dao.atualizar(selecionado);
            JOptionPane.showMessageDialog(this, "Reserva cancelada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            recarregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpar() {
        selecionado = null;
        cbxLeitor.setSelectedIndex(-1);  // nenhum leitor selecionado
        cbxLivro.setSelectedIndex(-1);   // nenhum livro selecionado
        cbxStatus.setSelectedIndex(0);   // status volta para ATIVA (padrão)
        lblData.setText("Data: -");
        tabela.clearSelection();
    }

    @Override
    public void refreshData() {
        carregarLeitores();
        carregarLivros();
        recarregarTabela();
        limpar();
    }
}
