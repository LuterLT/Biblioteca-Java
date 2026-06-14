package ui;

import dao.LeitorDAO;
import modelo.Leitor;
import exceptions.DadosInvalidosException;
import ui.Refreshable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.List;

public class TelaLeitores extends JPanel implements Refreshable {
    private LeitorDAO dao = new LeitorDAO();
    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField txtNome, txtEmail;
    private JFormattedTextField txtCpf, txtMatricula;
    private JCheckBox chkBloqueado;
    private Leitor selecionado = null;

    public TelaLeitores() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Painel superior - tabela
        JPanel pnlTabela = new JPanel(new BorderLayout());
        modelo = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "E-mail", "Matrícula", "Bloqueado"}, 0);
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> selecionarLinha());
        JScrollPane scroll = new JScrollPane(tabela);
        pnlTabela.add(scroll, BorderLayout.CENTER);
        add(pnlTabela, BorderLayout.CENTER);

        // Painel direito - formulário
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Formulário"));

        pnlForm.add(new JLabel("Nome:"));
        txtNome = new JTextField(15);
        pnlForm.add(txtNome);

        pnlForm.add(Box.createVerticalStrut(5));
        pnlForm.add(new JLabel("CPF: "));
        try {
            // Cria uma máscara para o CPF no formato 000.000.000-00
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_'); // mostra _ nos espaços vazios
            txtCpf = new JFormattedTextField(mascaraCpf);
            txtCpf.setColumns(15);
        } catch (ParseException e) {
            txtCpf = new JFormattedTextField();
            txtCpf.setColumns(15);
        }
        pnlForm.add(txtCpf);

        pnlForm.add(Box.createVerticalStrut(5));
        pnlForm.add(new JLabel("Matrícula:"));
        try {
            // Cria uma máscara para matrícula no formato MAT000 (MAT fixo + 3 dígitos)
            MaskFormatter mascaraMatricula = new MaskFormatter("MAT###");
            mascaraMatricula.setPlaceholderCharacter('_');
            txtMatricula = new JFormattedTextField(mascaraMatricula);
            txtMatricula.setColumns(15);
        } catch (ParseException e) {
            txtMatricula = new JFormattedTextField();
            txtMatricula.setColumns(15);
        }
        pnlForm.add(txtMatricula);

        pnlForm.add(Box.createVerticalStrut(5));
        pnlForm.add(new JLabel("E-mail:"));
        txtEmail = new JTextField(15);
        pnlForm.add(txtEmail);

        pnlForm.add(Box.createVerticalStrut(5));
        chkBloqueado = new JCheckBox("Bloqueado");
        pnlForm.add(chkBloqueado);

        pnlForm.add(Box.createVerticalStrut(10));

        // Botões
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
            List<Leitor> lista = dao.listarTodos();
            for (Leitor l : lista) {
                modelo.addRow(new Object[]{l.getId(), l.getNome(), l.getCpf(), l.getEmail(), l.getMatricula(), l.isBloqueado() ? "Sim" : "Não"});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar leitores: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
                    txtMatricula.setText(selecionado.getMatricula());
                    chkBloqueado.setSelected(selecionado.isBloqueado());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar leitor: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void salvar() {
        try {
            String nome = txtNome.getText().trim();
            String cpf = txtCpf.getText().trim();
            String email = txtEmail.getText().trim();
            String matricula = txtMatricula.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || matricula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Sempre cria um novo leitor (Atualizar serve para editar)
            Leitor l = new Leitor(0, nome, cpf, matricula, email);
            l.validar();
            dao.inserir(l);
            JOptionPane.showMessageDialog(this, "Leitor adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Selecione um leitor para atualizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String nome = txtNome.getText().trim();
            String cpf = txtCpf.getText().trim();
            String email = txtEmail.getText().trim();
            String matricula = txtMatricula.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || matricula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            selecionado.setNome(nome);
            selecionado.setCpf(cpf);
            selecionado.setEmail(email);
            selecionado.setMatricula(matricula);
            selecionado.setBloqueado(chkBloqueado.isSelected());
            selecionado.validar();
            dao.atualizar(selecionado);
            JOptionPane.showMessageDialog(this, "Leitor atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Selecione um leitor para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                dao.remover(selecionado.getId());
                JOptionPane.showMessageDialog(this, "Leitor excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
                recarregarTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpar() {
        selecionado = null;
        txtNome.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtMatricula.setText("");
        chkBloqueado.setSelected(false);
        tabela.clearSelection();
    }
}
