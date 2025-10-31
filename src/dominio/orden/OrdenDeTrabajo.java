package dominio.orden;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dominio.empleado.Mecanico;

public class OrdenDeTrabajo implements Facturable {
	private int numeroOrden;
	private LocalDate fechaIngreso;
	private EstadoOT estado;
	private String diagnostico;
	private Prioridad prioridad;
	private Mecanico asignadoA;
	private List<ItemRepuesto> repuestos;
	private List<LineaServicio> servicios;
	private float horasTrabajadas;

	public OrdenDeTrabajo(int numeroOrden, LocalDate fechaIngreso, EstadoOT estado, String diagnostico,
			Prioridad prioridad, Mecanico asignadoA, float horasTrabajadas) {
		this.numeroOrden = numeroOrden;
		this.fechaIngreso = fechaIngreso;
		this.estado = estado;
		this.diagnostico = diagnostico;
		this.prioridad = prioridad;
		this.asignadoA = asignadoA;
		this.repuestos = new ArrayList<>();
		this.servicios = new ArrayList<>();
		this.horasTrabajadas = horasTrabajadas;
	}

	public int getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public EstadoOT getEstado() {
		return estado;
	}

	public void setEstado(EstadoOT estado) {
		this.estado = estado;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public Prioridad getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(Prioridad prioridad) {
		this.prioridad = prioridad;
	}

	public Mecanico getAsignadoA() {
		return asignadoA;
	}

	public void setAsignadoA(Mecanico asignadoA) {
		this.asignadoA = asignadoA;
	}

	@Override
	public double calcularCostoTotal() {
		double total = 0;

		for (ItemRepuesto itemRepuesto : repuestos) {
			total += itemRepuesto.subTotal();
		}
		for (LineaServicio lineaServicio : servicios) {
			lineaServicio
					.setTarifaHora(this.asignadoA.calcularTarifaHora(this.horasTrabajadas, asignadoA.getSalario()));
			total += lineaServicio.subTotal();
		}

		return total;

	}

	public void agregarRepuesto(ItemRepuesto itemRepuesto) {
		this.repuestos.add(itemRepuesto);
	}

	public void agregarServicio(LineaServicio lineaServicio) {
		this.servicios.add(lineaServicio);
	}

}
