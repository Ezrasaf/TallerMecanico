package vista;

import javax.swing.*;

public class EmpleadoView {
    private JPanel rootPanel;
    private JTable tablaEmpleados;
    private JTextField txtLegajo;
    private JTextField txtNombre;
    private JComboBox<String> comboTipo;
    private JTextField txtEspecialidad;
    private JComboBox<String> comboNivel;
    private JTextField txtTarifa;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnCancelar;
    private JLabel lblEspecialidad;
    private JLabel lblNivel;
    private JButton btnHistorial;

    public EmpleadoView() {
        // 🔹 Este listener se ejecuta cada vez que cambia el tipo de empleado
        comboTipo.addActionListener(e -> actualizarVisibilidadCampos());
        actualizarVisibilidadCampos();
    }

    /** Muestra/oculta campos según el tipo */
    private void actualizarVisibilidadCampos() {
        String tipo = (String) comboTipo.getSelectedItem();
        boolean esMecanico = "Mecanico".equalsIgnoreCase(tipo);

        txtEspecialidad.setVisible(esMecanico);
        comboNivel.setVisible(esMecanico);

        lblEspecialidad.setVisible(esMecanico);
        lblNivel.setVisible(esMecanico);

        rootPanel.revalidate();
        rootPanel.repaint();
    }

    public JPanel getRootPanel() { return rootPanel; }
    public JTable getTablaEmpleados() { return tablaEmpleados; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnHistorial() {return btnHistorial;}

    public int getLegajo() {
        try { return Integer.parseInt(txtLegajo.getText()); }
        catch (NumberFormatException e) { return 0; }
    }

    public String getTipo() { return (String) comboTipo.getSelectedItem(); }
    public String getNombre() { return txtNombre.getText(); }
    public String getEspecialidad() { return txtEspecialidad.getText(); }
    public String getNivel() { return (String) comboNivel.getSelectedItem(); }
    public double getTarifa() {
        try { return Double.parseDouble(txtTarifa.getText()); }
        catch (NumberFormatException e) { return 0.0; }
    }
    public void setNombre(String nombre) {
        txtNombre.setText(nombre);
    }

    public int getLegajoSeleccionado() {
        int fila = tablaEmpleados.getSelectedRow();
        if (fila == -1) {
            throw new IllegalStateException("Debe seleccionar un empleado.");
        }
        Object val = tablaEmpleados.getValueAt(fila, 0); // col 0 = legajo
        return Integer.parseInt(val.toString());
    }


    public void limpiarFormulario() {
        txtLegajo.setText("");
        txtNombre.setText("");
        txtEspecialidad.setText("");
        txtTarifa.setText("");
        comboTipo.setSelectedIndex(0);
        comboNivel.setSelectedIndex(0);
        actualizarVisibilidadCampos(); // 🔹 revalida visibilidad
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(rootPanel, mensaje);
    }
}
