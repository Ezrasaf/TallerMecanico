package controlador;

import vista.OrdenView;
import Datos.archivo.RepoOrdenesArchivo;
import Datos.archivo.RepoRepuestosArchivo;
import Datos.archivo.RepoServiciosArchivo;
import dominio.orden.*;
import Datos.archivo.RepoEmpleadosArchivo;
import dominio.empleado.Empleado;
import dominio.empleado.Mecanico;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class OrdenController {
    private final OrdenView view;
    private final RepoOrdenesArchivo repoOrdenes;
    private final RepoRepuestosArchivo repoRepuestos;
    private final RepoServiciosArchivo repoServicios;
    private final DefaultTableModel modelOrdenes;
    private final DefaultTableModel modelRepuestos;
    private final DefaultTableModel modelServicios;
    private final RepoEmpleadosArchivo repoEmpleados;  // 🔹 nuevo


    public OrdenController(
            OrdenView view,
            RepoOrdenesArchivo repoOrdenes,
            RepoRepuestosArchivo repoRepuestos,
            RepoServiciosArchivo repoServicios,
            RepoEmpleadosArchivo repoEmpleados
    ) {
        this.view = view;
        this.repoOrdenes = repoOrdenes;
        this.repoRepuestos = repoRepuestos;
        this.repoServicios = repoServicios;
        this.repoEmpleados = repoEmpleados;

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
        cargarEmpleadosEnCombo();
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
            // Número de orden
            int numero = view.getNumeroOrden();

            // Fecha en formato dd/MM/yyyy (la vista muestra ese formato)
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecha = LocalDate.parse(view.getFecha(), fmt);

            // Estado, diagnóstico, prioridad
            EstadoOT estado = EstadoOT.fromString(view.getEstado());
            String diag = view.getDiagnostico();
            Prioridad pr = Prioridad.valueOf(view.getPrioridad().toUpperCase());

            // 🔹 Empleado tomado desde el combo (no de un textfield)
            int legajo = view.getLegajoEmpleadoSeleccionado();

            // Horas trabajadas
            float horas = view.getHoras();

            // 🔹 Restricción: máximo 2 órdenes activas para este empleado
            int activas = repoOrdenes.contarOrdenesActivasPorEmpleado(legajo);
            if (activas >= 2) {
                view.mostrarMensaje("El empleado seleccionado ya tiene 2 órdenes activas. Elija otro empleado.");
                return;
            }

            // Crear la orden
            OrdenDeTrabajo o = new OrdenDeTrabajo(numero, fecha, estado, diag, pr, legajo, horas);

            // 🔹 REPUESTOS de la tabla
            for (int i = 0; i < modelRepuestos.getRowCount(); i++) {
                String codigo = modelRepuestos.getValueAt(i, 0).toString();
                String desc = modelRepuestos.getValueAt(i, 1).toString();
                int cant = Integer.parseInt(modelRepuestos.getValueAt(i, 2).toString());
                double precio = Double.parseDouble(modelRepuestos.getValueAt(i, 3).toString());
                o.agregarRepuesto(new ItemRepuesto(codigo, desc, cant, precio));
            }

            // 🔹 SERVICIOS de la tabla
            for (int i = 0; i < modelServicios.getRowCount(); i++) {
                String desc = modelServicios.getValueAt(i, 0).toString();
                double h = Double.parseDouble(modelServicios.getValueAt(i, 1).toString());
                double tarifa = Double.parseDouble(modelServicios.getValueAt(i, 2).toString());
                o.agregarServicio(new LineaServicio(desc, h, tarifa));
            }

            // Guardar
            repoOrdenes.guardar(o);

            cargarOrdenes(); // refresca tabla + número + fecha
            view.mostrarMensaje("Orden guardada correctamente.");
            view.limpiarFormulario(); // limpia campos pero NO el número

        } catch (DateTimeParseException e) {
            view.mostrarMensaje("La fecha debe tener formato dd/MM/yyyy.");
        } catch (NumberFormatException e) {
            view.mostrarMensaje("Verifique los valores numéricos (horas, tarifas, cantidades...).");
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

    private String buscarNombreEmpleado(int legajo) {
        Empleado emp = repoEmpleados.buscarPorLegajo(legajo);
        if (emp != null) {
            return emp.getNombre();   // o getApellido(), o getNombreCompleto()
        }
        return "Desconocido";
    }
    public List<OrdenDeTrabajo> listarOrdenesPorEmpleado(int legajo) {
        return repoOrdenes.listarPorEmpleado(legajo);
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
                    buscarNombreEmpleado(o.getLegajoEmpleado()),
                    o.getHorasTrabajadas()
            });
        }

        // Después de refrescar la tabla, calculamos el próximo número
        int siguiente = repoOrdenes.proximoNumero();
        view.prepararNumeroOrden(siguiente); //num incremetnal; id orden
        view.setFechaHoy();        // Fecha de hoy automática
        // Opcional: recargar también empleados por si cambió algo
        cargarEmpleadosEnCombo();
    }

    public void cargarEmpleadosEnCombo() {
        List<Empleado> empleados = repoEmpleados.listar();
        List<String> etiquetas = new java.util.ArrayList<>();

        for (Empleado e : empleados) {
            if (e instanceof Mecanico) {  // solo mecánicos para órdenes
                Mecanico m = (Mecanico) e;
                // Formato: "123 - Juan Pérez - Motores"
                String etiqueta = m.getLegajo() + " - " + m.getNombre() + " - " + m.getEspecialidad();
                etiquetas.add(etiqueta);
            }
        }
        view.setEmpleados(etiquetas);
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
