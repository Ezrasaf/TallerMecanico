package controlador;

import vista.VehiculoView;
import dominio.vehiculo.Vehiculo;
import Datos.RepositorioVehiculos;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VehiculoController {
    private final VehiculoView view;
    private final RepositorioVehiculos repo;
    private final DefaultTableModel model;

    public VehiculoController(VehiculoView view, RepositorioVehiculos repo) {
        this.view = view;
        this.repo = repo;
        this.model = new DefaultTableModel(new Object[]{"Patente", "Marca", "Modelo", "Año", "Cliente"}, 0);
        view.getTablaVehiculos().setModel(model);
        inicializar();
        cargarVehiculos();
    }

    private void inicializar() {
        view.getBtnGuardar().addActionListener(e -> guardarVehiculo());
        view.getBtnCancelar().addActionListener(e -> view.limpiarFormulario());
    }

    private void cargarVehiculos() {
        List<Vehiculo> vehiculos = repo.listar();
        for (Vehiculo v : vehiculos) {
            model.addRow(new Object[]{v.getPatente(), v.getMarca(), v.getModelo(), v.getAnio(), v.getDniCliente()});
        }
    }

    private void guardarVehiculo() {
        System.out.println(view.getCliente());
        Vehiculo v = new Vehiculo(
                view.getPatente(), view.getMarca(), view.getModelo(),
                view.getAnio(), view.getCliente()
        );
        repo.guardar(v);
        model.addRow(new Object[]{v.getPatente(), v.getMarca(), v.getModelo(), v.getAnio(), v.getDniCliente()});
        view.mostrarMensaje("Vehículo guardado correctamente.");
        view.limpiarFormulario();


    }
}
