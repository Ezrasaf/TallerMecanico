package controlador;

import vista.VehiculoView;
import dominio.vehiculo.Vehiculo;
import Datos.RepositorioVehiculos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VehiculoController {
    private final VehiculoView view;
    private final RepositorioVehiculos repo;
    private final DefaultTableModel model;
    private final ClienteController clienteController; // nuevo

    public VehiculoController(VehiculoView view, RepositorioVehiculos repo, ClienteController clienteController) {
        this.view = view;
        this.repo = repo;
        this.clienteController = clienteController;
        this.model = new DefaultTableModel(new Object[]{"Marca", "Modelo", "Patente", "Año", "Cliente (DNI)"}, 0);
        view.getTablaVehiculos().setModel(model);
        inicializar();
        cargarVehiculos();
    }

    private void inicializar() {
        view.getBtnGuardar().addActionListener(e -> guardarVehiculo());
        view.getBtnCancelar().addActionListener(e -> view.limpiarFormulario());
        view.getBtnEliminar().addActionListener(e -> eliminarVehiculo());
    }

    private void eliminarVehiculo() {
        int fila = view.getTablaVehiculos().getSelectedRow();
        if (fila >= 0) {
            String patente = (String) model.getValueAt(fila, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    view.getRootPanel(),
                    "¿Seguro que querés eliminar este vehículo?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) return;

            // eliminar del CSV
            repo.eliminar(patente);

            // recargar tabla
            cargarVehiculos();

            // actualizar tabla de clientes
            if (clienteController != null) {
                clienteController.recargarTabla();
            }

            view.mostrarMensaje("Vehículo eliminado correctamente.");
        } else {
            view.mostrarMensaje("Seleccione un vehículo para eliminar.");
        }
    }

    public void cargarVehiculos() {
        model.setRowCount(0);
        List<Vehiculo> vehiculos = repo.listar();
        for (Vehiculo v : vehiculos) {
            model.addRow(new Object[]{
                    v.getMarca(), v.getModelo(), v.getPatente(),v.getAnio(), v.getDniCliente()
            });
        }
    }

    private void guardarVehiculo() {
        int dniCliente;
        try {
            dniCliente = Integer.parseInt(view.getCliente());
        } catch (NumberFormatException e) {
            view.mostrarMensaje("El DNI del cliente debe ser un número.");
            return;
        }

        Vehiculo v = new Vehiculo(
                view.getMarca(),
                view.getModelo(),
                view.getPatente(),
                view.getAnio(),
                dniCliente
        );

        repo.guardar(v);
        model.addRow(new Object[]{v.getMarca(), v.getModelo(),v.getPatente(), v.getAnio(), v.getDniCliente()});
        view.mostrarMensaje("Vehículo guardado correctamente.");
        view.limpiarFormulario();

        // 🔁 notificar a ClienteController para que actualice la tabla
        if (clienteController != null) {
            clienteController.recargarTabla();
        }
    }

}
