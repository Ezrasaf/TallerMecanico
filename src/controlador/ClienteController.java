package controlador;

import vista.ClienteView;
import dominio.cliente.Cliente;
import dominio.vehiculo.Vehiculo;
import Datos.RepositorioClientes;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClienteController {
    private final ClienteView view;
    private final RepositorioClientes repo;
    private final DefaultTableModel model;
    private List<Cliente> clientes; // ← guardamos la lista en memoria

    public ClienteController(ClienteView view, RepositorioClientes repo) {
        this.view = view;
        this.repo = repo;
        this.model = new DefaultTableModel(new Object[]{"Nombre", "Email", "Edad", "Teléfono", "DNI"}, 0);
        view.getTablaClientes().setModel(model);
        inicializar();
        cargarClientes();
    }

    private void inicializar() {
        view.getBtnGuardar().addActionListener(e -> guardarCliente());
        view.getBtnCancelar().addActionListener(e -> view.limpiarFormulario());
    }

    private void cargarClientes() {
        clientes = repo.listar();
        model.setRowCount(0);

        for (Cliente c : clientes) {
            String vehiculosTexto = c.getVehiculos().isEmpty()
                    ? "Sin vehículos"
                    : String.join(", ", c.getVehiculos().stream().map(Vehiculo::toString).toList());

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

        repo.guardar(c);
        clientes.add(c); // ← lo agregamos a la lista en memoria también
        model.addRow(new Object[]{c.getNombre(), c.getEmail(), c.getEdad(), c.getTelefono(), c.getDni()});
        view.mostrarMensaje("Cliente guardado correctamente.");
        view.limpiarFormulario();
    }

    public Cliente buscarClientePorDni(int dni) {
        for (Cliente c : clientes) {
            if (c.getDni() == dni) { // comparar Strings correctamente
                return c;
            }
        }
        return null;
    }

    public void guardarVehiculo(Vehiculo v, int dni) {
        Cliente cliente = buscarClientePorDni(dni);

        if (cliente == null) {
            view.mostrarMensaje("No se encontró un cliente con ese DNI.");
            return;
        }

        cliente.agregarVehiculo(v);
        repo.actualizar(clientes);
        cargarClientes();

        view.mostrarMensaje("Vehículo agregado correctamente al cliente " + cliente.getNombre());
    }
}
