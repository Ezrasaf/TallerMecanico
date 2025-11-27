package controlador;

import Datos.archivo.RepoEmpleadosArchivo;
import dominio.empleado.Empleado;
import dominio.empleado.Mecanico;
import dominio.empleado.Nivel;
import dominio.empleado.Recepcionista;
import dominio.orden.OrdenDeTrabajo;
import vista.EmpleadoView;
import vista.historial.HistorialOrdenesEmpleadoView;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EmpleadoController {

    private final EmpleadoView view;
    private final RepoEmpleadosArchivo repo;
    private final DefaultTableModel model;
    // usamos el OrdenController para llegar al repo de órdenes
    private final OrdenController ordenController;

    public EmpleadoController(EmpleadoView view,
                              RepoEmpleadosArchivo repo,
                              OrdenController ordenController) {
        this.view = view;
        this.repo = repo;
        this.ordenController = ordenController;

        this.model = new DefaultTableModel(
                new Object[]{"Legajo", "Nombre", "Tipo", "Especialidad / Cargo", "Nivel", "Tarifa"}, 0
        );
        view.getTablaEmpleados().setModel(model);

        inicializar();
        cargarEmpleados();
    }

    /** Configura los listeners de los botones */
    private void inicializar() {
        view.getBtnGuardar().addActionListener(e -> guardarEmpleado());
        view.getBtnEliminar().addActionListener(e -> eliminarEmpleado());
        view.getBtnCancelar().addActionListener(e -> view.limpiarFormulario());
        view.getBtnHistorial().addActionListener(e -> verHistorialEmpleado());
    }

    /** Carga los empleados desde el CSV */
    private void cargarEmpleados() {
        List<Empleado> empleados = repo.listar();

        // 🔹 ORDENAR POR LEGAJO ASCENDENTE
        empleados.sort((a, b) -> Integer.compare(a.getLegajo(), b.getLegajo()));

        model.setRowCount(0); // limpia la tabla antes de recargar

        for (Empleado e : empleados) {
            if (e instanceof Mecanico m) {
                model.addRow(new Object[]{
                        m.getLegajo(),
                        m.getNombre(),
                        "Mecanico",
                        m.getEspecialidad(),
                        m.getNivel().name(),
                        String.format("%.2f", m.tarifaHora())
                });
            } else if (e instanceof Recepcionista r) {
                model.addRow(new Object[]{
                        r.getLegajo(),
                        r.getNombre(),
                        "Recepcionista",
                        "-",
                        "-",
                        String.format("%.2f", r.tarifaHora())
                });
            }
        }
    }

    /** Guarda un empleado nuevo o actualiza uno existente */
    private void guardarEmpleado() {
        int legajo = view.getLegajo();
        String tipo = view.getTipo();
        double tarifa = view.getTarifa();
        String nombre = view.getNombre();

        if (legajo <= 0 || tarifa <= 0 || nombre == null || nombre.isBlank()) {
            view.mostrarMensaje("Complete todos los campos correctamente (legajo, nombre y tarifa válidos).");
            return;
        }

        Empleado empleado;
        if ("Mecanico".equalsIgnoreCase(tipo)) {
            String especialidad = view.getEspecialidad();
            String nivelStr = view.getNivel();
            Nivel nivel;
            try {
                nivel = Nivel.valueOf(nivelStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                nivel = Nivel.JUNIOR;
            }
            empleado = new Mecanico(legajo, nombre, especialidad, tarifa, nivel);
        } else {
            empleado = new Recepcionista(legajo, nombre, tarifa);
        }

        repo.guardar(empleado);
        cargarEmpleados();
        view.mostrarMensaje("Empleado guardado correctamente.");
        view.limpiarFormulario();

        // refrescamos el combo de empleados en la pantalla de Órdenes
        if (ordenController != null) {
            ordenController.cargarEmpleadosEnCombo();
        }
    }

    /** Elimina un empleado seleccionado */
    private void eliminarEmpleado() {
        int fila = view.getTablaEmpleados().getSelectedRow();
        if (fila >= 0) {
            int legajo = Integer.parseInt(model.getValueAt(fila, 0).toString());
            repo.eliminar(legajo);
            cargarEmpleados();
            view.mostrarMensaje("Empleado eliminado correctamente.");

            if (ordenController != null) {
                ordenController.cargarEmpleadosEnCombo();
            }
        } else {
            view.mostrarMensaje("Seleccione un empleado para eliminar.");
        }
    }

    /** Abre el historial de órdenes del empleado seleccionado */
    private void verHistorialEmpleado() {
        try {
            int legajo = view.getLegajoSeleccionado(); // lo sacamos de la tabla de empleados

            // le pedimos al OrdenController la lista de órdenes de ese empleado
            List<OrdenDeTrabajo> ordenes = ordenController.listarOrdenesPorEmpleado(legajo);

            HistorialOrdenesEmpleadoView dialog =
                    new HistorialOrdenesEmpleadoView(null);
            dialog.cargarOrdenes(ordenes);
            dialog.setVisible(true);

        } catch (IllegalStateException ex) {
            // por ejemplo, si no hay fila seleccionada
            view.mostrarMensaje(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarMensaje("Error al mostrar el historial: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
