package dominio.empleado;

import dominio.excepciones.HorasInvalidasException;

public abstract class Empleado {
    private final int legajo;
    private String nombre;

    public Empleado(int legajo, String nombre) {
        this.legajo = legajo;
        this.nombre = nombre;
    }

    public int getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    /** Tarifa pura por hora del empleado */
    public abstract double tarifaHora();

    /** Costo por X horas */
    public double costoPor(double horas) throws HorasInvalidasException {
        if (horas <= 0) throw new HorasInvalidasException("Las horas deben ser > 0");
        return horas * tarifaHora();
    }
}
