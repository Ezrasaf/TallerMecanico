package dominio.vehiculo;

import java.util.ArrayList;
import java.util.List;

import dominio.orden.OrdenDeTrabajo;

public class Vehiculo {
    private String marca;
    private String modelo;
    private String patente;
    private int anio;
    private List<OrdenDeTrabajo> ordenesDeTrabajo;
    private int dniCliente;

    public Vehiculo(String marca, String modelo, String patente, int anio) {
        this.marca = marca;
        this.modelo = modelo;
        this.patente = patente;
        this.anio = anio;
        this.ordenesDeTrabajo = new ArrayList<>();
    }

    public Vehiculo(String marca, String modelo, String patente, int anio, int dniCliente) {
        this.marca = marca;
        this.modelo = modelo;
        this.patente = patente;
        this.anio = anio;
        this.ordenesDeTrabajo = new ArrayList<>();
        this.dniCliente = dniCliente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPatente() {
        return patente;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public List<OrdenDeTrabajo> getOrdenesDeTrabajo() {
        return ordenesDeTrabajo;
    }

    public void agregarOrdenDeTrabajo(OrdenDeTrabajo orden) {
        this.ordenesDeTrabajo.add(orden);
    }

    public void eliminarOrdenDeTrabajo(OrdenDeTrabajo orden) {
        this.ordenesDeTrabajo.remove(orden);
    }

    public int getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(int dniCliente) {
        this.dniCliente = dniCliente;
    }
}
