package Datos;

import java.util.List;
import dominio.orden.OrdenDeTrabajo;

public interface RepositorioOrdenes {
	void guardar(OrdenDeTrabajo o);

	List<OrdenDeTrabajo> listar();
    int proximoNumero();

    int contarOrdenesActivasPorEmpleado(int legajoEmpleado);
}
