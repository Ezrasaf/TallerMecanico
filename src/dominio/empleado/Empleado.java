package dominio.empleado;

public abstract class Empleado {
	private int legajo;

	public Empleado(int legajo) {
		this.legajo = legajo;
	}

	public int getLegajo() {
		return legajo;
	}

	abstract public double calcularTarifaHora(float horas, double tarifaHora);

}
