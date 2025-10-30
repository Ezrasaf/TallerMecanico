package dominio.cliente;

import java.util.ArrayList;
import java.util.List;

import dominio.general.Persona;
import dominio.vehiculo.Vehiculo;

public class Cliente extends Persona implements Notificable {
	private String email;
	private List<Vehiculo> vehiculos;

	public Cliente(String nombre, int edad, int telefono, int id, String email) {
		super(nombre, edad, telefono, id);
		this.email = email;
		this.vehiculos = new ArrayList<>();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void notificar(String mensaje) {
		System.out.println(mensaje);
	}

	public List<Vehiculo> getVehiculos() {
		return vehiculos;
	}

	public void agregarVehiculo(Vehiculo vehiculo) {
		this.vehiculos.add(vehiculo);
	}
}
