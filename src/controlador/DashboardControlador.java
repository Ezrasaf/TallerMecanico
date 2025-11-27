package controlador;

import Datos.RepositorioClientes;
import Datos.RepositorioVehiculos;
import Datos.archivo.*;
import vista.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DashboardController
 * --------------------
 * Controlador principal del sistema.
 * Se encarga de manejar la navegación entre las diferentes secciones
 * del panel principal (Clientes, Vehículos, Órdenes, Empleados, Facturación).
 *
 * Aplica principios SOLID y GRASP:
 * - SRP: Solo controla el comportamiento del Dashboard.
 * - Controller (GRASP): Intermediario entre la vista y los subcontroladores.
 * - Low Coupling / High Cohesion: No contiene lógica de negocio.
 */
public class DashboardControlador {

    private final DashboardView view;
    private final Map<String, JPanel> modulos = new HashMap<>();
    private final CardLayout layout;

    // Controladores (si querés usarlos después)
    private ClienteController clienteController;
    private VehiculoController vehiculoController;
    private EmpleadoController empleadoController;
    private OrdenController ordenController;

    public DashboardControlador(DashboardView view) {
        this.view = view;
        this.layout = new CardLayout();
        inicializar();
    }

    /**
     * Inicializa los componentes y listeners del Dashboard.
     */
    private void inicializar() {
        // Configura el layout principal
        view.getContentPanel().setLayout(layout);

        // -----------------------------
        // Repositorios
        // -----------------------------
        RepositorioClientes repoClientes = new RepoClientesArchivo("data/clientes.csv");
        RepositorioVehiculos repoVehiculos = new RepoVehiculosArchivo("data/vehiculos.csv");
        RepoEmpleadosArchivo repoEmpleados = new RepoEmpleadosArchivo("data/empleados.csv");
        RepoOrdenesArchivo repoOrdenes = new RepoOrdenesArchivo("data/ordenes.csv");
        RepoRepuestosArchivo repoRepuestos = new RepoRepuestosArchivo("data/repuestos.csv");
        RepoServiciosArchivo repoServicios = new RepoServiciosArchivo("data/servicios.csv");

        // -----------------------------
        // Vistas
        // -----------------------------
        OrdenView ordenView = new OrdenView();
        ClienteView clienteView = new ClienteView();
        VehiculoView vehiculoView = new VehiculoView();
        EmpleadoView empleadoView = new EmpleadoView();

        // -----------------------------
        // Controladores
        // -----------------------------

        // ÓRDENES (va primero porque Empleados lo usa)
        ordenController = new OrdenController(
                ordenView,
                repoOrdenes,
                repoRepuestos,
                repoServicios,
                repoEmpleados
        );

        // CLIENTES
        clienteController = new ClienteController(
                clienteView,
                repoClientes,
                repoVehiculos
        );

        // VEHÍCULOS (recibe ClienteController para avisar recargas)
        vehiculoController = new VehiculoController(
                vehiculoView,
                repoVehiculos,
                clienteController
        );
        // ClienteController necesita VehiculoController para recargar al borrar cliente
        clienteController.setVehiculoController(vehiculoController);

        // EMPLEADOS (sincronizado con ÓRDENES para el combo)
        empleadoController = new EmpleadoController(
                empleadoView,
                repoEmpleados,
                ordenController
        );

        // -----------------------------
        // Registrar módulos en el CardLayout
        // -----------------------------
        modulos.put("clientes", clienteView.getRootPanel());
        modulos.put("vehiculos", vehiculoView.getRootPanel());
        modulos.put("ordenes", ordenView.getRootPanel());
        modulos.put("empleados", empleadoView.getRootPanel());
        modulos.put("facturacion", crearPlaceholder("Facturación y Pagos"));

        modulos.forEach((nombre, panel) -> view.getContentPanel().add(panel, nombre));

        // -----------------------------
        // Botones del sidebar
        // -----------------------------
        view.getBtnClientes().addActionListener(e -> mostrarModulo("clientes"));
        view.getBtnVehiculos().addActionListener(e -> mostrarModulo("vehiculos"));
        view.getBtnOrdenes().addActionListener(e -> mostrarModulo("ordenes"));
        view.getBtnEmpleados().addActionListener(e -> mostrarModulo("empleados"));
        // (si más adelante tenés btnFacturacion, acá sumás el listener)

        // Vista por defecto
        layout.show(view.getContentPanel(), "clientes");
    }

    /**
     * Cambia la vista visible en el panel central.
     */
    private void mostrarModulo(String nombre) {
        layout.show(view.getContentPanel(), nombre);
    }

    /**
     * Crea un panel de marcador de posición (placeholder) para módulos aún no implementados.
     */
    private JPanel crearPlaceholder(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(titulo, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
