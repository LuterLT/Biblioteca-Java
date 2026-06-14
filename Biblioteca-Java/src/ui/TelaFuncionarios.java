package ui;

import dao.FuncionarioDAO;
import modelo.Funcionario;
import exceptions.DadosInvalidosException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.List;

public class TelaFuncionarios extends JPanel implements Refreshable {
    private FuncionarioDAO dao = new FuncionarioDAO();
    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField txtNome, txtEmail, txtCargo;
    private JFormattedTextField txtCpf;
    private Funcionario selecionado = null;

    public TelaFuncionarios() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel pnlTabela = new JPanel(new BorderLayout());
        modelo = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "E-mail", "Cargo"}, 0);
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> selecionarLinha());
        JScrollPane scroll = new JScrollPane(tabela);
        pnlTabela.add(scroll, BorderLayout.CENTER);
        add(pnlTabela, BorderLayout.CENTER);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Formulário"));

        pnlForm.add(new JLabel("Nome:"));
        txtNome = new JTextField(15);
        pnlForm.add(txtNome);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("CPF:"));
        try {
            // Cria uma máscara para o CPF no formato 000.000.000-00
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(mascaraCpf);
            txtCpf.setColumns(15);
        } catch (ParseException e) {
            txtCpf = new JFormattedTextField();
            txtCpf.setColumns(15);
        }
        pnlForm.add(txtCpf);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("E-mail:"));
        txtEmail = new JTextField(15);
        pnlForm.add(txtEmail);
        pnlForm.add(Box.createVerticalStrut(5));

        pnlForm.add(new JLabel("Cargo:"));
        txtCargo = new JTextField(15);
        pnlForm.add(txtCargo);
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
            List<Funcionario> lista = dao.listarTodos();
            for (Funcionario f : lista) {
                modelo.addRow(new Object[]{f.getId(), f.getNome(), f.getCpf(), f.getEmail(), f.getCargo()});
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
                    txtNome.setText(selecionado.getNome());
                    txtCpf.setText(selecionado.getCpf());
                    txtEmail.setText(selecionado.getEmail());
                    txtCargo.setText(selecionado.getCargo());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void salvar() {
        try {
            if (txtNome.getText().isEmpty() || txtCpf.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCargo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Sempre cria um novo funcionário
            Funcionario f = new Funcionario(0, txtNome.getText(), txtCpf.getText(), txtCargo.getText(), txtEmail.getText());
            f.validar();
            dao.inserir(f);
            JOptionPane.showMessageDialog(this, "Funcionário adicionado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            recarregarTabela();
        } catch (DadosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, "Dados inválidos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para atualizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (txtNome.getText().isEmpty() || txtCpf.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCargo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            selecionado.setNome(txtNome.getText());
            selecionado.setCpf(txtCpf.getText());
            selecionado.setEmail(txtEmail.getText());
            selecionado.setCargo(txtCargo.getText());
            selecionado.validar();
            dao.atualizar(selecionado);
            JOptionPane.showMessageDialog(this, "Funcionário atualizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
            recarregarTabela();
        } catch (DadosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, "Dados inválidos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refreshData() {
        recarregarTabela();
    }

    private void excluir() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Excluir?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                dao.remover(selecionado.getId());
                JOptionPane.showMessageDialog(this, "Funcionário excluído!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
                recarregarTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpar() {
        selecionado = null;
        txtNome.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtCargo.setText("");
        tabela.clearSelection();
    }
}
