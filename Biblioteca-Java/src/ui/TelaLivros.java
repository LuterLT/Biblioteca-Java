package ui;

import dao.LivroDAO;
import modelo.Livro;
import ui.Refreshable;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaLivros extends JPanel implements Refreshable {
    private LivroDAO dao = new LivroDAO();
    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField txtTitulo, txtAutor;
    private JFormattedTextField txtIsbn;
    private Livro selecionado = null;

    public TelaLivros() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel pnlTabela = new JPanel(new BorderLayout());
        modelo = new DefaultTableModel(new String[]{"ID", "Título", "Autor", "ISBN"}, 0);
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> selecionarLinha());
        JScrollPane scroll = new JScrollPane(tabela);
        pnlTabela.add(scroll, BorderLayout.CENTER);
        add(pnlTabela, BorderLayout.CENTER);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Formulário"));

        pnlForm.add(new JLabel("Título:"));
        txtTitulo = new JTextField(15);
        pnlForm.add(txtTitulo);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("Autor:"));
        txtAutor = new JTextField(15);
        pnlForm.add(txtAutor);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("ISBN:"));
        try {
            // Máscara ISBN-13: 000-00-00000-00-0
            MaskFormatter mascaraIsbn = new MaskFormatter("###-##-#####-##-#");
            mascaraIsbn.setPlaceholderCharacter('_');
            txtIsbn = new JFormattedTextField(mascaraIsbn);
            txtIsbn.setColumns(15);
        } catch (ParseException e) {
            txtIsbn = new JFormattedTextField();
            txtIsbn.setColumns(15);
        }
        pnlForm.add(txtIsbn);
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

    private void recarregarTabela() {
        try {
            modelo.setRowCount(0);
            List<Livro> lista = dao.listarTodos();
            for (Livro l : lista) {
                modelo.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn()});
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
                    txtTitulo.setText(selecionado.getTitulo());
                    txtAutor.setText(selecionado.getAutor());
                    txtIsbn.setText(selecionado.getIsbn());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void salvar() {
        try {
            if (txtTitulo.getText().isEmpty() || txtAutor.getText().isEmpty() || txtIsbn.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Livro l = new Livro(0, txtTitulo.getText(), txtAutor.getText(), txtIsbn.getText());
            dao.inserir(l);
            JOptionPane.showMessageDialog(this, "Livro adicionado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            recarregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para atualizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (txtTitulo.getText().isEmpty() || txtAutor.getText().isEmpty() || txtIsbn.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            selecionado.setTitulo(txtTitulo.getText());
            selecionado.setAutor(txtAutor.getText());
            selecionado.setIsbn(txtIsbn.getText());
            dao.atualizar(selecionado);
            JOptionPane.showMessageDialog(this, "Livro atualizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            recarregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um livro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Excluir?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                dao.remover(selecionado.getId());
                JOptionPane.showMessageDialog(this, "Livro excluído!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
                recarregarTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpar() {
        selecionado = null;
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        tabela.clearSelection();
    }

    @Override
    public void refreshData() {
        recarregarTabela();
    }
}
