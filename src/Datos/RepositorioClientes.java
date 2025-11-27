package Datos;

import java.util.List;
import dominio.cliente.Cliente;

public interface RepositorioClientes {
	void guardar(Cliente c);

	List<Cliente> listar();

	void actualizar(List<Cliente> clientes);

	void eliminar(int dni);

}
