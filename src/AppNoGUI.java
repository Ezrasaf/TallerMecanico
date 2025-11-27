import dominio.cliente.Cliente;
import dominio.empleado.Empleado;
import dominio.empleado.Mecanico;
import dominio.empleado.Nivel;
import dominio.empleado.Recepcionista;
import dominio.orden.OrdenDeTrabajo;
import dominio.orden.Prioridad;
import dominio.orden.EstadoOT;
import dominio.orden.ItemRepuesto;
import dominio.orden.LineaServicio;
import dominio.vehiculo.Vehiculo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class AppNoGUI {
	public static void main(String[] args) {
		System.out.println("=== SISTEMA DE GESTIÓN DE TALLER MECÁNICO ===\n");
		// --- Crear Clientes ---
		Cliente cliente1 = new Cliente("Juan Pérez", 20, 1133587972, "juan@example.com", 45819761);
		Cliente cliente2 = new Cliente("María Gómez", 29, 1199988877, "maria@example.com", 45819762);

		System.out.println("Clientes registrados:");
		System.out.println("- " + cliente1.getNombre() + " (" + cliente1.getEmail() + ")");
		System.out.println("- " + cliente2.getNombre() + " (" + cliente2.getEmail() + ")\n");

		// --- Crear Vehículos ---
		Vehiculo auto1 = new Vehiculo("ABC123", "Toyota", "Corolla", 2018, cliente1.getDni());
		Vehiculo auto2 = new Vehiculo("XYZ789", "Ford", "Fiesta", 2020, cliente2.getDni());

		cliente1.agregarVehiculo(auto2);
		cliente2.agregarVehiculo(auto1);

		System.out.println("Clientes y sus vehículos:");
		System.out.println("- " + cliente1.getNombre() + " tiene los vehículos:");
		for (Vehiculo v : cliente1.getVehiculos()) {
			System.out.println("  * " + v.getMarca() + " " + v.getModelo() + " (" + v.getPatente() + ")");
		}

		System.out.println("- " + cliente2.getNombre() + " tiene los vehículos:");
		for (Vehiculo v : cliente2.getVehiculos()) {
			System.out.println("  * " + v.getMarca() + " " + v.getModelo() + " (" + v.getPatente() + ")");
		}

		System.out.println("Vehículos registrados:");
		System.out.println("- " + auto1.getMarca() + " " + auto1.getModelo() + " (" + auto1.getPatente() + ")");
		System.out.println("- " + auto2.getMarca() + " " + auto2.getModelo() + " (" + auto2.getPatente() + ")\n");

		// Crear Nivel
		Nivel nivelMecanico = Nivel.SEMI;

		// --- Crear Empleados ---
		Empleado mecanico = new Mecanico(116778899,"Juan Perez", "Especialista en motores", 40, nivelMecanico);
		Empleado recepcionista = new Recepcionista(123,"Martina Ortiz", 400);

		System.out.println("Legajos del Empleado del taller:");
		System.out.println(
				"- Mecánico Legajo: " + mecanico.getLegajo() + " (Nivel: " + ((Mecanico) mecanico).getNivel() + ")");
		System.out.println("- Recepcionista Legajo: " + recepcionista.getLegajo());
		System.out.println();

		// --- Crear Repuestos ---
		ItemRepuesto aceite = new ItemRepuesto("ae111", "Aceite sintético 5W30", 350, 5000);
		ItemRepuesto filtro = new ItemRepuesto("fi222", "Filtro de aceite", 120, 3000);
		ItemRepuesto bujias = new ItemRepuesto("bu333", "Juego de bujías", 60, 8000);
		List<ItemRepuesto> repuestosDisponibles = Arrays.asList(aceite, filtro, bujias);

		System.out.println("\nRepuestos disponibles:");
		for (ItemRepuesto repuesto : repuestosDisponibles) {
			System.out.println("- " + repuesto.getDescripcion() + " ($" + repuesto.getPrecioUnitario() + ")");
		}

		// --- Crear Servicios ---
		LineaServicio cambioAceite = new LineaServicio("Cambio de aceite", 3, 500);
		LineaServicio revisionGeneral = new LineaServicio("Revisión general", 1, 1000);

		// --- Crear una Orden de Trabajo ---
		EstadoOT estado = EstadoOT.EN_PROCESO;
		Prioridad prioridad = Prioridad.BAJA;
		OrdenDeTrabajo orden1 = new OrdenDeTrabajo(
				1,
				LocalDate.now(),
				estado,
				"Cambio de aceite y revisión",
				prioridad,
				mecanico.getLegajo(),
				5);

		try {
			orden1.agregarRepuesto(filtro);
			orden1.agregarRepuesto(aceite);
		} catch (Exception e) {
			System.out.println("⚠️ Error al agregar repuesto: " + e.getMessage());
		}

		orden1.agregarServicio(cambioAceite);
		orden1.agregarServicio(revisionGeneral);

		System.out.println("\nOrden de Trabajo creada con ID: " + orden1.getNumeroOrden());

		// --- Mostrar resumen de la Orden ---
		System.out.println("ORDEN DE TRABAJO #" + orden1.getNumeroOrden());
		System.out.println("Cliente: " + cliente1.getNombre());
		System.out.println("Vehículo: " + auto1.getMarca() + " " + auto1.getModelo());
		System.out.println("Empleado asignado: " + mecanico.getLegajo());
		System.out.println("\nRepuestos utilizados:");
		for (ItemRepuesto r : orden1.getRepuestos()) {
			System.out.println("  - " + r.getDescripcion() + " ($" + r.getPrecioUnitario() + ")");
		}

		System.out.println("\nServicios realizados:");
		for (LineaServicio s : orden1.getServicios()) {
			System.out.println("  - " + s.getDescripcion() + " ($" + s.getTarifaHora() + ")");
		}

		System.out.println("\nCosto total de la orden: $" + orden1.calcularCostoTotalFormateado());
		System.out.println("\n=== FIN DE LA DEMOSTRACIÓN ===");
	}
}
