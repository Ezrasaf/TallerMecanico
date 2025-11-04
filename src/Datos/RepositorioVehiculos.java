package Datos;

import dominio.vehiculo.Vehiculo;

public interface RepositorioVehiculos extends Repositorio<Vehiculo, String> {
    void eliminarPorCliente(int dni);
}