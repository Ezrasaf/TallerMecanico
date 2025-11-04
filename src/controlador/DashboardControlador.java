package controlador;

import Datos.RepositorioClientes;
import Datos.RepositorioVehiculos;
import Datos.archivo.RepoClientesArchivo;
import Datos.archivo.RepoVehiculosArchivo;
import vista.ClienteView;
import vista.DashboardView;
import vista.VehiculoView;

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

        ClienteView clienteView = new ClienteView();
        RepositorioClientes repoClientes = new RepoClientesArchivo("clientes.csv");
        new ClienteController(clienteView, repoClientes);

        VehiculoView vehiculoView = new VehiculoView();
        RepositorioVehiculos repoVehiculos = new RepoVehiculosArchivo("vehiculos.csv");
        new VehiculoController(vehiculoView, repoVehiculos);


        // Por ahora agregamos paneles placeholder hasta crear las vistas reales
        modulos.put("clientes", clienteView.getRootPanel());
        modulos.put("vehiculos", vehiculoView.getRootPanel());
        modulos.put("ordenes", crearPlaceholder("Órdenes de Trabajo"));
        modulos.put("empleados", crearPlaceholder("Gestión de Empleados"));
        modulos.put("facturacion", crearPlaceholder("Facturación y Pagos"));

        // Añadimos los paneles al contentPanel
        modulos.forEach((nombre, panel) -> view.getContentPanel().add(panel, nombre));

        // Asociamos los eventos a los botones del sidebar
        view.getBtnClientes().addActionListener(e -> mostrarModulo("clientes"));
        view.getBtnVehiculos().addActionListener(e -> mostrarModulo("vehiculos"));
        view.getBtnOrdenes().addActionListener(e -> mostrarModulo("ordenes"));
        view.getBtnEmpleados().addActionListener(e -> mostrarModulo("empleados"));
        view.getBtnFacturacion().addActionListener(e -> mostrarModulo("facturacion"));

        // Mostramos la vista por defecto
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
