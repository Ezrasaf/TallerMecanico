package vista.historial;

import dominio.orden.OrdenDeTrabajo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class HistorialOrdenesEmpleadoView extends JDialog {

    private JPanel panelRoot;
    private JTable tablaHistorial;
    private JButton btnCerrar;
    private JPanel panelBotones;
    private DefaultTableModel model;

    public HistorialOrdenesEmpleadoView(JFrame owner) {
        super(owner, "Historial de órdenes", true);
        setContentPane(panelRoot);
        setSize(700, 300);
        setLocationRelativeTo(owner);

        model = new DefaultTableModel(
                new Object[]{"N°", "Fecha", "Estado", "Diagnóstico", "Prioridad", "Horas"}, 0
        );
        tablaHistorial.setModel(model);

        btnCerrar.addActionListener(e -> dispose());
    }

    public void cargarOrdenes(List<OrdenDeTrabajo> ordenes) {
        model.setRowCount(0);
        for (OrdenDeTrabajo o : ordenes) {
            model.addRow(new Object[]{
                    o.getNumeroOrden(),
                    o.getFechaIngreso(),
                    o.getEstado(),
                    o.getDiagnostico(),
                    o.getPrioridad(),
                    o.getHorasTrabajadas()
            });
        }
    }
}
