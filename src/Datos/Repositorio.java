package Datos;

import java.util.List;

/**
 * Repositorio genérico con operaciones CRUD básicas.
 * @param <T>  Tipo de entidad (por ejemplo Cliente, Vehiculo, Empleado, etc.)
 * @param <ID> Tipo del identificador (por ejemplo Integer, String, etc.)
 */
public interface Repositorio<T, ID> {
    void guardar(T entidad);
    List<T> listar();
    void eliminar(ID id);
    void actualizar(List<T> entidades);
}
