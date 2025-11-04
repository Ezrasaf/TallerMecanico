package persistencia;

import java.util.List;
import java.util.Optional;

import dominio.vehiculo.Vehiculo;

public interface RepositorioVehiculos {
    void guardar(Vehiculo v);
    Optional<Vehiculo> buscarPorPatente(String patente);
    List<Vehiculo> listar();
}
