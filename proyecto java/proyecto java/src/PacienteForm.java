import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.regex.Pattern;

class PacienteForm extends JFrame {
    private JTextField duiField = new JTextField(9);
    private JTextField nombreField = new JTextField(20);
    private JTextField emailField = new JTextField(20);
    private JButton btnAgregar = new JButton("Agregar Paciente");

    public PacienteForm() {
        super("Gestión de Pacientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(450, 250);
        setLocationRelativeTo(null);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);

        c.gridx=0; c.gridy=0; add(new JLabel("DUI (9 dígitos):"), c);
        c.gridx=1;           add(duiField, c);
        c.gridx=0; c.gridy=1; add(new JLabel("Nombre:"), c);
        c.gridx=1;           add(nombreField, c);
        c.gridx=0; c.gridy=2; add(new JLabel("Email:"), c);
        c.gridx=1;           add(emailField, c);
        c.gridx=1; c.gridy=3; add(btnAgregar, c);

        btnAgregar.addActionListener(e -> agregarPaciente());
        setVisible(true);
    }

    private void agregarPaciente() {
        String dui = duiField.getText().trim();
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        if (!dui.matches("\\d{9}")) {
            mostrarError("El DUI debe tener 9 dígitos numéricos."); return;
        }
        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacío."); return;
        }
        if (!Pattern.matches(".+@.+\\..+", email)) {
            mostrarError("Email inválido."); return;
        }
        try (Connection conn = Conexion.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO pacientes(dui,nombre,email) VALUES(?,?,?)"
            );
            ps.setString(1, dui);
            ps.setString(2, nombre);
            ps.setString(3, email);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Paciente agregado.");
            duiField.setText("");
            nombreField.setText("");
            emailField.setText("");
        } catch (SQLException ex) {
            mostrarError("Error BD: " + ex.getMessage());
        }
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}