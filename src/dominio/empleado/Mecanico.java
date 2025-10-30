package dominio.empleado;

public class Mecanico extends Empleado {
	private String especialidad;
	private double salario;

	public Mecanico(int legajo, String especialidad, double salario) {
		super(legajo);
		this.especialidad = especialidad;
		this.salario = salario;
	}

	@Override
	public double calcularTarifaHora(float horas, double salario) {
		if (horas == 0) {
			throw new IllegalArgumentException("Las horas no pueden ser cero.");
		}
		return salario / horas;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}

}
