package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    public TelaPrincipal() {
        setTitle("Biblioteca-Java - Tela Principal");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Leitores", new TelaLeitores());
        tabs.addTab("Funcionários", new TelaFuncionarios());
        tabs.addTab("Livros", new TelaLivros());
        tabs.addTab("Cópias", new TelaCopias());
        tabs.addTab("Empréstimos", new TelaEmprestimos());
        tabs.addTab("Reservas", new TelaReservas());
        tabs.addChangeListener(e -> {
            Component comp = tabs.getSelectedComponent();
            if (comp instanceof Refreshable) {
                ((Refreshable) comp).refreshData();
            }
        });
        getContentPane().add(tabs, BorderLayout.CENTER);
    }
}
