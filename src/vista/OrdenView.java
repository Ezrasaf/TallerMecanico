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
    private JComboBox<String> comboEmpleado;

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
    public String getEmpleado() {
        Object sel = comboEmpleado.getSelectedItem();
        return sel != null ? sel.toString() : "";
    }
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

    // Número de orden (para que el controlador lo pueda setear)
    public void setNumeroOrden(int numero) {
        txtNumero.setText(String.valueOf(numero));
    }

    public void prepararNumeroOrden(int numero) {
        setNumeroOrden(numero);
        txtNumero.setEditable(false); // lo maneja el sistema, no el usuario
    }
    public void setFechaHoy() {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtFecha.setText(hoy.format(fmt));
    }

    // --- Métodos auxiliares ---
    // --- Métodos auxiliares ---
    public void limpiarFormulario() {
        // OJO: no tocamos txtNumero, porque lo setea el controlador
        setFechaHoy();
        txtDiagnostico.setText("");
        txtHoras.setText("");
        comboEstado.setSelectedIndex(0);
        comboPrioridad.setSelectedIndex(0);
        if (comboEmpleado.getItemCount() > 0) {
            comboEmpleado.setSelectedIndex(0);
        }

        txtCodigoRepuesto.setText("");
        txtDescripcionRepuesto.setText("");
        txtCantidadRepuesto.setText("");
        txtPrecioUnitario.setText("");

        txtDescripcionServicio.setText("");
        txtHorasServicio.setText("");
        txtTarifaServicio.setText("");
    }



    // Cargar opciones en el combo
    public void setEmpleados(java.util.List<String> empleados) {
        comboEmpleado.removeAllItems();
        if (empleados == null) return;
        for (String e : empleados) {
            comboEmpleado.addItem(e);
        }
    }

    // Obtener el legajo del empleado seleccionado
    public int getLegajoEmpleadoSeleccionado() {
        Object sel = comboEmpleado.getSelectedItem();
        if (sel == null) {
            throw new IllegalStateException("Debe seleccionar un empleado.");
        }
        String texto = sel.toString();
        // Vamos a usar el formato "123 - Juan Pérez - Motores"
        String legajoStr = texto.split("-")[0].trim();
        return Integer.parseInt(legajoStr);
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(rootPanel, msg);
    }
}
