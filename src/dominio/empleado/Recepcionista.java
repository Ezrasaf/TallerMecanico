package dominio.empleado;

public class Recepcionista extends Empleado {
    private double tarifaHora;

    public Recepcionista(int legajo,String nombre, double tarifaHora) {
        super(legajo, nombre);
        this.tarifaHora = tarifaHora;
    }

    @Override
    public double tarifaHora() {
        return tarifaHora;
    }
}
