package vista;

import javax.swing.*;

public class VehiculoView {
    private JPanel rootPanel;
    private JTable tablaVehiculos;
    private JTextField txtPatente;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtAnio;
    private JTextField txtCliente;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnEliminar;


    public JPanel getRootPanel() { return rootPanel; }
    public JTable getTablaVehiculos() { return tablaVehiculos; }

    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnEliminar() { return btnEliminar; }

    public String getPatente() { return txtPatente.getText(); }
    public String getMarca() { return txtMarca.getText(); }
    public String getModelo() { return txtModelo.getText(); }
    public int getAnio() {
        try { return Integer.parseInt(txtAnio.getText()); }
        catch (NumberFormatException e) { return 0; }
    }
    public String getCliente() { return txtCliente.getText(); }

    public void limpiarFormulario() {
        txtPatente.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        txtCliente.setText("");
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(rootPanel, mensaje);
    }
}
