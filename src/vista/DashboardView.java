package vista;

import javax.swing.*;

public class DashboardView {
    private JPanel rootPanel;
    private JPanel headerPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JButton btnClientes;
    private JButton btnVehiculos;
    private JButton btnOrdenes;
    private JButton btnEmpleados;
    private JLabel lblTitulo;
    public JPanel getRootPanel() { return rootPanel; }
    public JPanel getContentPanel() { return contentPanel; }

    public JButton getBtnClientes() { return btnClientes; }
    public JButton getBtnVehiculos() { return btnVehiculos; }
    public JButton getBtnOrdenes() { return btnOrdenes; }
    public JButton getBtnEmpleados() { return btnEmpleados; }

    public DashboardView() {
        lblTitulo.setText("Taller Mecánico - Panel Principal");
    }
}
