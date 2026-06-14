import dao.ConexaoBD;
import ui.TelaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Inicializa banco (cria arquivo e tabelas caso necessário)
        try {
            ConexaoBD.init();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro inicializando banco: " + ex.getMessage());
            return;
        }

        // Abre interface gráfica
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tp = new TelaPrincipal();
            tp.setVisible(true);
        });
    }
}
