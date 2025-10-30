package dominio.orden;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dominio.empleado.Mecanico;

public class OrdenDeTrabajo implements Facturable {
	private int numeroOrden;
	private Date fechaIngreso;
	private EstadoOT estado;
	private String diagnostico;
	private Prioridad prioridad;
	private Mecanico asignadoA;
	private List<ItemRepuesto> repuestos;
	private List<LineaServicio> servicios;
	private float horasTrabajadas;

	public OrdenDeTrabajo(int numeroOrden, Date fechaIngreso, EstadoOT estado, String diagnostico,
			Prioridad prioridad, Mecanico asignadoA) {
		this.numeroOrden = numeroOrden;
		this.fechaIngreso = fechaIngreso;
		this.estado = estado;
		this.diagnostico = diagnostico;
		this.prioridad = prioridad;
		this.asignadoA = asignadoA;
		this.repuestos = new ArrayList<>();
		this.servicios = new ArrayList<>();
		this.horasTrabajadas = 0;
	}

	public int getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
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
			total += lineaServicio.subTotal();
		}

		total += asignadoA.calcularTarifaHora(this.horasTrabajadas, asignadoA.getSalario());

		return total;

	}

}
