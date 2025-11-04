package Datos;

import java.util.List;
import java.util.Optional;

import dominio.orden.OrdenDeTrabajo;

public interface RepositorioOrdenes {
    void guardar(OrdenDeTrabajo o);
    List<OrdenDeTrabajo> listar();
}
