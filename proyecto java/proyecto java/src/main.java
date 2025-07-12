import javax.swing.*;
import java.awt.*;


public class main extends JFrame {

    public main() {
        super("Sistema de Clínicas");
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton btnMed = new JButton("Médicos");
        JButton btnPac = new JButton("Pacientes");
        JButton btnCit = new JButton("Citas");

        btnMed.addActionListener(e -> new Medicoform());
        btnPac.addActionListener(e -> new PacienteForm());
        btnCit.addActionListener(e -> new CitaForm());

        add(btnMed);
        add(btnPac);
        add(btnCit);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(main::new);
    }
}
