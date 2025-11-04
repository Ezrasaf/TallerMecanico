package persistencia;

import java.util.List;
import java.util.Optional;

import dominio.orden.OrdenDeTrabajo;

public interface RepositorioOrdenes {
    void guardar(OrdenDeTrabajo o);
    Optional<OrdenDeTrabajo> buscarPorId(int numeroOrden);
    List<OrdenDeTrabajo> listar();
}
