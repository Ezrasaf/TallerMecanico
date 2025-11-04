package vista;

import javax.swing.*;

public class ClienteView {
    private JPanel rootPanel;
    private JTable tablaClientes;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtEdad;
    private JTextField txtTelefono;
    private JTextField txtDni;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnEliminar;

    public JPanel getRootPanel() { return rootPanel; }
    public JTable getTablaClientes() { return tablaClientes; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnEliminar() { return btnEliminar; }

    // Getters corregidos
    public String getNombre() { return txtNombre.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public int getEdad() {
        try { return Integer.parseInt(txtEdad.getText()); }
        catch (NumberFormatException e) { return 0; }
    }
    public int getTelefono() {
        try { return Integer.parseInt(txtTelefono.getText()); }
        catch (NumberFormatException e) { return 0; }
    }

    public int getDni() {
        try { return Integer.parseInt(txtDni.getText()); }
        catch (NumberFormatException e) { return 0; }
    }

    public void limpiarFormulario() {
        txtNombre.setText("");
        txtEmail.setText("");
        txtEdad.setText("");
        txtTelefono.setText("");
        txtDni.setText("");
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(rootPanel, mensaje);
    }

}
