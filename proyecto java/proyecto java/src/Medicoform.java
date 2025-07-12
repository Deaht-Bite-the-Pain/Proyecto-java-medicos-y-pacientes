import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class Medicoform extends JFrame {


    private JTextField idField = new JTextField(10);
    private JTextField nombreField = new JTextField(20);
    private JButton btnAgregar = new JButton("Agregar Médico");

    public Medicoform() {
        super("Gestión de Médicos");
        setLayout(new GridBagLayout());
        setSize(400, 200);
        setLocationRelativeTo(null);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);

        c.gridx=0; c.gridy=0; add(new JLabel("ID Médico:"), c);
        c.gridx=1;           add(idField, c);
        c.gridx=0; c.gridy=1; add(new JLabel("Nombre:"), c);
        c.gridx=1;           add(nombreField, c);
        c.gridx=1; c.gridy=2; add(btnAgregar, c);

        btnAgregar.addActionListener(e -> agregarMedico());
        setVisible(true);
    }

    private void agregarMedico() {
        String idText = idField.getText().trim();
        String nombre = nombreField.getText().trim();
        if (!idText.matches("\\d+")) {
            mostrarError("El ID debe ser numérico."); return;
        }
        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacío."); return;
        }
        try (Connection conn = Conexion.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO medicos(id,nombre) VALUES(?,?)"
            );
            ps.setInt(1, Integer.parseInt(idText));
            ps.setString(2, nombre);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Médico agregado.");
            idField.setText("");
            nombreField.setText("");
        } catch (SQLException ex) {
            mostrarError("Error BD: " + ex.getMessage());
        }
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }




}
