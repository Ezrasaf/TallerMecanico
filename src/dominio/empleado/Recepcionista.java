package dominio.empleado;

public class Recepcionista extends Empleado {
	private double tarifaHora;

	public Recepcionista(int legajo, double tarifaHora) {
		super(legajo);
		this.tarifaHora = tarifaHora;
	}

	@Override
	public double calcularTarifaHora(float horas, double tarifaHora) {
		return horas * this.tarifaHora;
	}

}
