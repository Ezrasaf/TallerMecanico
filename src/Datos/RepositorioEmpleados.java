package Datos;

import dominio.empleado.Empleado;
import java.util.List;

public interface RepositorioEmpleados {
    void guardar(Empleado e);
    void eliminar(int legajo);
    List<Empleado> listar();
    void actualizar(List<Empleado> empleados);
}
