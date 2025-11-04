package controlador;

import vista.ClienteView;
import dominio.cliente.Cliente;
import dominio.vehiculo.Vehiculo;
import Datos.RepositorioClientes;
import Datos.RepositorioVehiculos;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteController {
    private final ClienteView view;
    private final RepositorioClientes repoClientes;
    private final RepositorioVehiculos repoVehiculos;
    private final DefaultTableModel model;
    private List<Cliente> clientes;
    private VehiculoController vehiculoController; // opcional, si querés refrescar la tabla de vehículos

    public ClienteController(ClienteView view, RepositorioClientes repoClientes, RepositorioVehiculos repoVehiculos) {
        this.view = view;
        this.repoClientes = repoClientes;
        this.repoVehiculos = repoVehiculos;
        this.model = new DefaultTableModel(
                new Object[]{"Nombre", "Email", "Edad", "Teléfono", "DNI", "Vehículos"}, 0
        );
        view.getTablaClientes().setModel(model);
        inicializar();
        cargarClientes();
    }

    public void setVehiculoController(VehiculoController vehiculoController) {
        this.vehiculoController = vehiculoController;
    }

    private void inicializar() {
        view.getBtnGuardar().addActionListener(e -> guardarCliente());
        view.getBtnCancelar().addActionListener(e -> view.limpiarFormulario());
        view.getBtnEliminar().addActionListener(e -> eliminarCliente()); // 🔹 manejamos también la eliminación
    }

    private void cargarClientes() {
        clientes = repoClientes.listar();
        List<Vehiculo> vehiculos = repoVehiculos.listar();
        model.setRowCount(0);

        for (Cliente c : clientes) {
            List<Vehiculo> vehiculosCliente = vehiculos.stream()
                    .filter(v -> v.getDniCliente() == c.getDni())
                    .collect(Collectors.toList());

            String vehiculosTexto = vehiculosCliente.isEmpty()
                    ? "Sin vehículos"
                    : vehiculosCliente.stream()
                    .map(v -> v.getMarca() + " " + v.getModelo() + " (" + v.getPatente() + ")")
                    .collect(Collectors.joining(", "));

            model.addRow(new Object[]{
                    c.getNombre(),
                    c.getEmail(),
                    c.getEdad(),
                    c.getTelefono(),
                    c.getDni(),
                    vehiculosTexto
            });
        }
    }

    private void guardarCliente() {
        Cliente c = new Cliente(
                view.getNombre(),
                view.getEdad(),
                view.getTelefono(),
                view.getEmail(),
                view.getDni()
        );

        repoClientes.guardar(c);
        clientes.add(c);
        cargarClientes();
        view.mostrarMensaje("Cliente guardado correctamente.");
        view.limpiarFormulario();
    }

    /** 🔹 Elimina el cliente y también sus vehículos asociados */
    private void eliminarCliente() {
        int fila = view.getTablaClientes().getSelectedRow();
        if (fila >= 0) {
            int dni = (int) model.getValueAt(fila, 4); // Columna DNI

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    view.getRootPanel(),
                    "¿Seguro que querés eliminar este cliente y todos sus vehículos?",
                    "Confirmar eliminación",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

            // 1️⃣ Eliminar cliente del archivo
            repoClientes.eliminar(dni);

            // 2️⃣ Eliminar todos los vehículos asociados
            repoVehiculos.eliminarPorCliente(dni);

            // 3️⃣ Actualizar la lista local y la vista
            clientes.removeIf(c -> c.getDni() == dni);
            cargarClientes();

            if (vehiculoController != null) {
                vehiculoController.cargarVehiculos();
            }

            view.mostrarMensaje("Cliente y sus vehículos fueron eliminados correctamente.");
        } else {
            view.mostrarMensaje("Seleccione un cliente para eliminar.");
        }
    }

    public void recargarTabla() {
        cargarClientes();
    }
}
