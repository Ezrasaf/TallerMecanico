package controlador;

import vista.EmpleadoView;
import dominio.empleado.*;
import Datos.archivo.RepoEmpleadosArchivo;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EmpleadoController {
    private final EmpleadoView view;
    private final RepoEmpleadosArchivo repo;
    private final DefaultTableModel model;

    public EmpleadoController(EmpleadoView view, RepoEmpleadosArchivo repo) {
        this.view = view;
        this.repo = repo;
        this.model = new DefaultTableModel(
                new Object[]{"Legajo", "Tipo", "Especialidad / Cargo", "Nivel", "Tarifa"}, 0
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
    }

    /** Carga los empleados desde el CSV */
    private void cargarEmpleados() {
        List<Empleado> empleados = repo.listar();
        model.setRowCount(0); // limpia la tabla antes de recargar

        for (Empleado e : empleados) {
            if (e instanceof Mecanico m) {
                model.addRow(new Object[]{
                        m.getLegajo(),
                        "Mecanico",
                        m.getEspecialidad(),
                        m.getNivel().name(),
                        String.format("%.2f", m.tarifaHora())
                });
            } else if (e instanceof Recepcionista r) {
                model.addRow(new Object[]{
                        r.getLegajo(),
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

        if (legajo <= 0 || tarifa <= 0) {
            view.mostrarMensaje("Complete todos los campos correctamente (legajo y tarifa válidos).");
            return;
        }

        Empleado empleado;

        if (tipo.equalsIgnoreCase("Mecanico")) {
            String especialidad = view.getEspecialidad();
            String nivelStr = view.getNivel();
            Nivel nivel;
            try {
                nivel = Nivel.valueOf(nivelStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                nivel = Nivel.JUNIOR;
            }
            empleado = new Mecanico(legajo, especialidad, tarifa, nivel);
        } else {
            empleado = new Recepcionista(legajo, tarifa);
        }

        repo.guardar(empleado);
        cargarEmpleados();
        view.mostrarMensaje("Empleado guardado correctamente.");
        view.limpiarFormulario();
    }

    /** Elimina un empleado seleccionado */
    private void eliminarEmpleado() {
        int fila = view.getTablaEmpleados().getSelectedRow();
        if (fila >= 0) {
            int legajo = Integer.parseInt(model.getValueAt(fila, 0).toString());
            repo.eliminar(legajo);
            cargarEmpleados();
            view.mostrarMensaje("Empleado eliminado correctamente.");
        } else {
            view.mostrarMensaje("Seleccione un empleado para eliminar.");
        }
    }
}
