package Datos;

import java.util.List;

import dominio.vehiculo.Vehiculo;

public interface RepositorioVehiculos {
	void guardar(Vehiculo entidad);

	List<Vehiculo> listar();

	void eliminar(String id);

	void actualizar(List<Vehiculo> entidades);

	void eliminarPorCliente(int dni);
}