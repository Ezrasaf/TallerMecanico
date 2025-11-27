package dominio.empleado;

public class Mecanico extends Empleado {
    private String especialidad;
    private double salarioMensual;
    private Nivel nivel;

    public Mecanico(int legajo,String nombre, String especialidad, double salarioMensual, Nivel nivel) {
        super(legajo, nombre);
        this.especialidad = especialidad;
        this.salarioMensual = salarioMensual;
        this.nivel = nivel;
    }

    @Override
    public double tarifaHora() {
        double base = salarioMensual / 160.0; // simple: 160 hs/mes
        return base * nivel.factor();
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public Nivel  getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
