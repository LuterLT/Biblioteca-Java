package ui;

import dao.CopiaDAO;
import dao.LivroDAO;
import modelo.Copia;
import modelo.Livro;
import ui.Refreshable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCopias extends JPanel implements Refreshable {
    private CopiaDAO dao = new CopiaDAO();
    private LivroDAO livroDAO = new LivroDAO();
    private JTable tabela;
    private DefaultTableModel modelo;
    private JComboBox<Livro> cbxLivro;
    private JTextField txtLocalizacao;
    private JCheckBox chkDisponivel;
    private Copia selecionado = null;

    public TelaCopias() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel pnlTabela = new JPanel(new BorderLayout());
        modelo = new DefaultTableModel(new String[]{"ID", "Livro", "Disponível", "Localização"}, 0);
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> selecionarLinha());
        JScrollPane scroll = new JScrollPane(tabela);
        pnlTabela.add(scroll, BorderLayout.CENTER);
        add(pnlTabela, BorderLayout.CENTER);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Formulário"));

        pnlForm.add(new JLabel("Livro:"));
        cbxLivro = new JComboBox<>();
        carregarLivros();
        pnlForm.add(cbxLivro);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("Localização:"));
        txtLocalizacao = new JTextField(15);
        pnlForm.add(txtLocalizacao);
        pnlForm.add(Box.createVerticalStrut(5));

        chkDisponivel = new JCheckBox("Disponível");
        pnlForm.add(chkDisponivel);
        pnlForm.add(Box.createVerticalStrut(10));

        JPanel pnlBotoes = new JPanel();
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizar());
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluir());
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limpar());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnAtualizar);
        pnlBotoes.add(btnExcluir);
        pnlBotoes.add(btnLimpar);
        pnlForm.add(pnlBotoes);
        pnlForm.add(Box.createVerticalGlue());

        add(pnlForm, BorderLayout.EAST);
        recarregarTabela();
    }

    private void carregarLivros() {
        try {
            cbxLivro.removeAllItems();
            List<Livro> lista = livroDAO.listarTodos();
            for (Livro l : lista) {
                cbxLivro.addItem(l);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recarregarTabela() {
        try {
            modelo.setRowCount(0);
            List<Copia> lista = dao.listarTodos();
            for (Copia c : lista) {
                modelo.addRow(new Object[]{c.getId(), c.getLivro() != null ? c.getLivro().getTitulo() : "?", c.isDisponivel() ? "Sim" : "Não", c.getLocalizacao()});
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
                    cbxLivro.setSelectedItem(selecionado.getLivro());
                    txtLocalizacao.setText(selecionado.getLocalizacao());
                    chkDisponivel.setSelected(selecionado.isDisponivel());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void salvar() {
        try {
            if (cbxLivro.getSelectedItem() == null || txtLocalizacao.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Sempre cria uma nova cópia
            Livro l = (Livro) cbxLivro.getSelectedItem();
            Copia c = new Copia(0, l, chkDisponivel.isSelected(), txtLocalizacao.getText());
            dao.inserir(c);
            JOptionPane.showMessageDialog(this, "Cópia adicionada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            recarregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma cópia para atualizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (cbxLivro.getSelectedItem() == null || txtLocalizacao.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Livro l = (Livro) cbxLivro.getSelectedItem();
            selecionado.setLivro(l);
            selecionado.setLocalizacao(txtLocalizacao.getText());
            selecionado.setDisponivel(chkDisponivel.isSelected());
            dao.atualizar(selecionado);
            JOptionPane.showMessageDialog(this, "Cópia atualizada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            recarregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma cópia!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Excluir?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                dao.remover(selecionado.getId());
                JOptionPane.showMessageDialog(this, "Cópia excluída!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
                recarregarTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpar() {
        selecionado = null;
        txtLocalizacao.setText("");
        chkDisponivel.setSelected(false);
        cbxLivro.setSelectedIndex(-1); // nenhum livro selecionado
        tabela.clearSelection();
    }

    @Override
    public void refreshData() {
        carregarLivros();
        recarregarTabela();
        limpar();
    }
}
