package controlador;

import vista.OrdenView;
import Datos.archivo.RepoOrdenesArchivo;
import Datos.archivo.RepoRepuestosArchivo;
import Datos.archivo.RepoServiciosArchivo;
import dominio.orden.*;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class OrdenController {
    private final OrdenView view;
    private final RepoOrdenesArchivo repoOrdenes;
    private final RepoRepuestosArchivo repoRepuestos;
    private final RepoServiciosArchivo repoServicios;
    private final DefaultTableModel modelOrdenes;
    private final DefaultTableModel modelRepuestos;
    private final DefaultTableModel modelServicios;

    public OrdenController(
            OrdenView view,
            RepoOrdenesArchivo repoOrdenes,
            RepoRepuestosArchivo repoRepuestos,
            RepoServiciosArchivo repoServicios
    ) {
        this.view = view;
        this.repoOrdenes = repoOrdenes;
        this.repoRepuestos = repoRepuestos;
        this.repoServicios = repoServicios;

        this.modelOrdenes = new DefaultTableModel(
                new Object[]{"N°", "Fecha", "Estado", "Diagnóstico", "Prioridad", "Legajo", "Horas"}, 0);
        this.modelRepuestos = new DefaultTableModel(
                new Object[]{"Código", "Descripción", "Cantidad", "Precio Unitario"}, 0);
        this.modelServicios = new DefaultTableModel(
                new Object[]{"Descripción", "Horas", "Tarifa/Hora"}, 0);

        view.getTablaOrdenes().setModel(modelOrdenes);
        view.getTablaRepuestos().setModel(modelRepuestos);
        view.getTablaServicios().setModel(modelServicios);

        inicializar();
        cargarCatalogos(); // precarga de CSVs
        cargarOrdenes();   // carga de órdenes
    }

    private void inicializar() {
        view.getBtnGuardar().addActionListener(e -> guardarOrden());
        view.getBtnEliminar().addActionListener(e -> eliminarOrden());
        view.getBtnCancelar().addActionListener(e -> view.limpiarFormulario());

        view.getBtnAgregarRepuesto().addActionListener(e -> agregarRepuesto());
        view.getBtnEliminarRepuesto().addActionListener(e -> eliminarRepuesto());
        view.getBtnAgregarServicio().addActionListener(e -> agregarServicio());
        view.getBtnEliminarServicio().addActionListener(e -> eliminarServicio());
    }

    // =======================
    // RE PUESTOS
    // =======================
    private void agregarRepuesto() {
        try {
            String codigo = view.getCodigoRepuesto();
            String desc = view.getDescripcionRepuesto();
            int cantidad = view.getCantidadRepuesto();
            double precio = view.getPrecioUnitario();

            if (codigo.isEmpty() || desc.isEmpty()) {
                view.mostrarMensaje("Complete todos los campos del repuesto.");
                return;
            }

            modelRepuestos.addRow(new Object[]{codigo, desc, cantidad, precio});
            view.mostrarMensaje("Repuesto agregado correctamente.");

            // Guardar en el CSV general de repuestos
            repoRepuestos.guardar(new ItemRepuesto(codigo, desc, cantidad, precio));
        } catch (Exception e) {
            view.mostrarMensaje("Datos de repuesto inválidos: " + e.getMessage());
        }
    }

    private void eliminarRepuesto() {
        int fila = view.getTablaRepuestos().getSelectedRow();
        if (fila >= 0) {
            String codigo = modelRepuestos.getValueAt(fila, 0).toString();
            modelRepuestos.removeRow(fila);
            repoRepuestos.eliminar(codigo);
            view.mostrarMensaje("Repuesto eliminado correctamente.");
        } else {
            view.mostrarMensaje("Seleccione un repuesto para eliminar.");
        }
    }

    // =======================
    // SE RVICIOS
    // =======================
    private void agregarServicio() {
        try {
            String desc = view.getDescripcionServicio();
            double horas = view.getHorasServicio();
            double tarifa = view.getTarifaServicio();

            if (desc.isEmpty()) {
                view.mostrarMensaje("Debe ingresar la descripción del servicio.");
                return;
            }

            modelServicios.addRow(new Object[]{desc, horas, tarifa});
            view.mostrarMensaje("Servicio agregado correctamente.");

            // Guardar en el CSV general de servicios
            repoServicios.guardar(new LineaServicio(desc, horas, tarifa));
        } catch (Exception e) {
            view.mostrarMensaje("Datos de servicio inválidos: " + e.getMessage());
        }
    }

    private void eliminarServicio() {
        int fila = view.getTablaServicios().getSelectedRow();
        if (fila >= 0) {
            String desc = modelServicios.getValueAt(fila, 0).toString();
            modelServicios.removeRow(fila);
            repoServicios.eliminar(desc);
            view.mostrarMensaje("Servicio eliminado correctamente.");
        } else {
            view.mostrarMensaje("Seleccione un servicio para eliminar.");
        }
    }

    // =======================
    // OR DENES
    // =======================
    private void guardarOrden() {
        try {
            int numero = view.getNumeroOrden();
            LocalDate fecha = LocalDate.parse(view.getFecha());
            EstadoOT estado = EstadoOT.fromString(view.getEstado());
            String diag = view.getDiagnostico();
            Prioridad pr = Prioridad.valueOf(view.getPrioridad().toUpperCase());
            int legajo = Integer.parseInt(view.getEmpleado());
            float horas = view.getHoras();

            OrdenDeTrabajo o = new OrdenDeTrabajo(numero, fecha, estado, diag, pr, legajo, horas);

            // Repuestos de la tabla
            for (int i = 0; i < modelRepuestos.getRowCount(); i++) {
                String codigo = modelRepuestos.getValueAt(i, 0).toString();
                String desc = modelRepuestos.getValueAt(i, 1).toString();
                int cant = Integer.parseInt(modelRepuestos.getValueAt(i, 2).toString());
                double precio = Double.parseDouble(modelRepuestos.getValueAt(i, 3).toString());
                o.agregarRepuesto(new ItemRepuesto(codigo, desc, cant, precio));
            }

            // Servicios de la tabla
            for (int i = 0; i < modelServicios.getRowCount(); i++) {
                String desc = modelServicios.getValueAt(i, 0).toString();
                double h = Double.parseDouble(modelServicios.getValueAt(i, 1).toString());
                double tarifa = Double.parseDouble(modelServicios.getValueAt(i, 2).toString());
                o.agregarServicio(new LineaServicio(desc, h, tarifa));
            }

            repoOrdenes.guardar(o);
            cargarOrdenes();
            view.mostrarMensaje("Orden guardada correctamente.");
            view.limpiarFormulario();
        } catch (Exception e) {
            view.mostrarMensaje("Error al guardar orden: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void eliminarOrden() {
        int fila = view.getTablaOrdenes().getSelectedRow();
        if (fila >= 0) {
            int numero = Integer.parseInt(modelOrdenes.getValueAt(fila, 0).toString());
            repoOrdenes.eliminar(numero);
            cargarOrdenes();
            view.mostrarMensaje("Orden eliminada correctamente.");
        } else {
            view.mostrarMensaje("Seleccione una orden para eliminar.");
        }
    }

    public void cargarOrdenes() {
        modelOrdenes.setRowCount(0);
        List<OrdenDeTrabajo> ordenes = repoOrdenes.listar();
        for (OrdenDeTrabajo o : ordenes) {
            modelOrdenes.addRow(new Object[]{
                    o.getNumeroOrden(),
                    o.getFechaIngreso(),
                    o.getEstado(),
                    o.getDiagnostico(),
                    o.getPrioridad(),
                    o.getLegajoEmpleado(),
                    o.getHorasTrabajadas()
            });
        }
    }

    // =======================
    // CATALOGOS (CSV base)
    // =======================
    private void cargarCatalogos() {
        modelRepuestos.setRowCount(0);
        for (ItemRepuesto r : repoRepuestos.listar()) {
            modelRepuestos.addRow(new Object[]{
                    r.getCodigo(), r.getDescripcion(), r.getCantidad(), r.getPrecioUnitario()
            });
        }

        modelServicios.setRowCount(0);
        for (LineaServicio s : repoServicios.listar()) {
            modelServicios.addRow(new Object[]{
                    s.getDescripcion(), s.getHoras(), s.getTarifaHora()
            });
        }
    }
}
