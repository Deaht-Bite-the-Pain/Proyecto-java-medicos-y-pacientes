import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.*;



class CitaForm extends JFrame {
    private JComboBox<String> medCombo = new JComboBox<>();
    private JComboBox<String> pacCombo = new JComboBox<>();
    private JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    private JButton btnAgregar = new JButton("Agendar Cita");
    private JButton btnActualizar = new JButton("Actualizar Datos");

    public CitaForm() {
        super("Gestión de Citas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(550, 350);
        setLocationRelativeTo(null);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);

        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        c.gridx=0; c.gridy=0; add(new JLabel("Médico:"), c);
        c.gridx=1;           add(medCombo, c);
        c.gridx=0; c.gridy=1; add(new JLabel("Paciente:"), c);
        c.gridx=1;           add(pacCombo, c);
        c.gridx=0; c.gridy=2; add(new JLabel("Fecha:"), c);
        c.gridx=1;           add(dateSpinner, c);
        c.gridx=0; c.gridy=3; add(btnAgregar, c);
        c.gridx=1;           add(btnActualizar, c);

        btnAgregar.addActionListener(e -> agendarCita());
        btnActualizar.addActionListener(e -> cargarDatos());

        cargarDatos();
        setVisible(true);
    }

    private void cargarDatos() {
        medCombo.removeAllItems();
        pacCombo.removeAllItems();
        try (Connection conn = Conexion.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, nombre FROM medicos");
            while (rs.next()) {
                String item = rs.getInt("id") + " - " + rs.getString("nombre");
                medCombo.addItem(item);
            }
            rs = st.executeQuery("SELECT dui, nombre FROM pacientes");
            while (rs.next()) {
                String item = rs.getString("dui") + " - " + rs.getString("nombre");
                pacCombo.addItem(item);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }

    private void agendarCita() {
        try {
            String medSel = (String) medCombo.getSelectedItem();
            String pacSel = (String) pacCombo.getSelectedItem();
            int idMed = Integer.parseInt(medSel.split(" - ")[0]);
            String dui = pacSel.split(" - ")[0];

            java.util.Date sel = (java.util.Date) dateSpinner.getValue();
            LocalDate fecha = sel.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (fecha.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "La fecha no puede ser anterior a hoy.");
                return;
            }
            PreparedStatement ps = Conexion.getConnection().prepareStatement(
                    "INSERT INTO citas(medico_id,paciente_dui,fecha) VALUES(?,?,?)"
            );
            ps.setInt(1, idMed);
            ps.setString(2, dui);
            ps.setDate(3, java.sql.Date.valueOf(fecha));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cita agendada:\n" + medSel + " con " + pacSel + " el " + fecha);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage());
        }
    }
}
