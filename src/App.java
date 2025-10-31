import java.sql.Date;
import java.time.LocalDate;

import dominio.cliente.Cliente;
import dominio.empleado.Mecanico;
import dominio.orden.EstadoOT;
import dominio.orden.ItemRepuesto;
import dominio.orden.LineaServicio;
import dominio.orden.OrdenDeTrabajo;
import dominio.orden.Prioridad;
import dominio.vehiculo.Vehiculo;

public class App {
	public static void main(String[] args) throws Exception {
		Vehiculo vehiculo = new Vehiculo("Toyota", "Corolla", "ABC123", 2020, "1HGBH41JXMN109186");
		Cliente cliente = new Cliente("Santiago Hanine", 21, 1133587072, 1, "santihanine@gmail.com");
		cliente.agregarVehiculo(vehiculo);
		cliente.notificar("¡Bienvenido " + cliente.getNombre() + "! Su vehículo " + vehiculo.getMarca() + " "
				+ vehiculo.getModelo() + " ha sido registrado exitosamente.");

		EstadoOT estado = EstadoOT.EN_PROCESO;
		Prioridad prioridad = Prioridad.ALTA;

		Mecanico mecanico = new Mecanico(01, "Camionetas", 30);
		OrdenDeTrabajo orden = new OrdenDeTrabajo(1, LocalDate.now(), estado, "El diagnostico prueba", prioridad,
				mecanico, 3);
		vehiculo.agregarOrdenDeTrabajo(orden);

		orden.agregarRepuesto(new ItemRepuesto("0001", "Frenos de disco", 150, 2));
		orden.agregarServicio(new LineaServicio("Cambio de aceite", 3, 1));
		orden.calcularCostoTotal();

		System.out.println("Costo total de la orden: " + orden.calcularCostoTotal());
	}
}
