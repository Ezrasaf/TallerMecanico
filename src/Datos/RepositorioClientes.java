package Datos;

import java.util.List;
import java.util.Optional;
import dominio.cliente.Cliente;

public interface RepositorioClientes {
    void guardar(Cliente c);
    Optional<Cliente> buscarPorId(String dni);
    List<Cliente> listar();
    void actualizar(List<Cliente> clientes);
}
