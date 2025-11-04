package vista;

import javax.swing.*;

public class OrdenView {
    private JPanel rootPanel;

    // === Datos generales ===
    private JTextField txtNumero;
    private JTextField txtFecha;
    private JComboBox<String> comboEstado;
    private JTextField txtDiagnostico;
    private JComboBox<String> comboPrioridad;
    private JTextField txtEmpleado;
    private JTextField txtHoras;

    // === Repuestos ===
    private JTable tablaRepuestos;
    private JTextField txtCodigoRepuesto;
    private JTextField txtDescripcionRepuesto;
    private JTextField txtCantidadRepuesto;
    private JTextField txtPrecioUnitario;
    private JButton btnAgregarRepuesto;
    private JButton btnEliminarRepuesto;

    // === Servicios ===
    private JTable tablaServicios;
    private JTextField txtDescripcionServicio;
    private JTextField txtHorasServicio;
    private JTextField txtTarifaServicio;
    private JButton btnAgregarServicio;
    private JButton btnEliminarServicio;

    // === Ordenes ===
    private JTable tablaOrdenes;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnCancelar;
    private JTabbedPane tabbedPane1;

    public JPanel getRootPanel() { return rootPanel; }

    // --- Getters de tablas ---
    public JTable getTablaOrdenes() { return tablaOrdenes; }
    public JTable getTablaRepuestos() { return tablaRepuestos; }
    public JTable getTablaServicios() { return tablaServicios; }

    // --- Botones ---
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnAgregarRepuesto() { return btnAgregarRepuesto; }
    public JButton getBtnEliminarRepuesto() { return btnEliminarRepuesto; }
    public JButton getBtnAgregarServicio() { return btnAgregarServicio; }
    public JButton getBtnEliminarServicio() { return btnEliminarServicio; }

    // --- Campos generales ---
    public int getNumeroOrden() { return Integer.parseInt(txtNumero.getText()); }
    public String getFecha() { return txtFecha.getText(); }
    public String getDiagnostico() { return txtDiagnostico.getText(); }
    public String getEstado() { return (String) comboEstado.getSelectedItem(); }
    public String getPrioridad() { return (String) comboPrioridad.getSelectedItem(); }
    public String getEmpleado() { return  txtEmpleado.getText(); }
    public float getHoras() { return Float.parseFloat(txtHoras.getText()); }

    // --- Campos de repuesto ---
    public String getCodigoRepuesto() { return txtCodigoRepuesto.getText(); }
    public String getDescripcionRepuesto() { return txtDescripcionRepuesto.getText(); }
    public int getCantidadRepuesto() { return Integer.parseInt(txtCantidadRepuesto.getText()); }
    public double getPrecioUnitario() { return Double.parseDouble(txtPrecioUnitario.getText()); }

    // --- Campos de servicio ---
    public String getDescripcionServicio() { return txtDescripcionServicio.getText(); }
    public double getHorasServicio() { return Double.parseDouble(txtHorasServicio.getText()); }
    public double getTarifaServicio() { return Double.parseDouble(txtTarifaServicio.getText()); }

    // --- Métodos auxiliares ---
    public void limpiarFormulario() {
        txtNumero.setText("");
        txtFecha.setText("");
        txtDiagnostico.setText("");
        txtHoras.setText("");
        comboEstado.setSelectedIndex(0);
        comboPrioridad.setSelectedIndex(0);
        txtEmpleado.setText("");

        txtCodigoRepuesto.setText("");
        txtDescripcionRepuesto.setText("");
        txtCantidadRepuesto.setText("");
        txtPrecioUnitario.setText("");

        txtDescripcionServicio.setText("");
        txtHorasServicio.setText("");
        txtTarifaServicio.setText("");
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(rootPanel, msg);
    }
}
