package dominio.orden;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dominio.empleado.Empleado;
import dominio.excepciones.RepuestoDuplicadoException;

public class OrdenDeTrabajo implements Facturable {
	public static final double IVA = 0.21;
	private int numeroOrden;
	private LocalDate fechaIngreso;
	private EstadoOT estado;
	private String diagnostico;
	private Prioridad prioridad;
	private Empleado asignadoA;
	private List<ItemRepuesto> repuestos;
	private List<LineaServicio> servicios;
	private float horasTrabajadas;

	public OrdenDeTrabajo(int numeroOrden, LocalDate fechaIngreso, EstadoOT estado, String diagnostico,
			Prioridad prioridad, Empleado asignadoA, float horasTrabajadas) {
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

	public OrdenDeTrabajo(int nro, LocalDate fecha, EstadoOT estado2, String diag, Prioridad pr, Object object,
			Object object2, double horas) {
		// TODO Auto-generated constructor stub
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

	public Empleado getAsignadoA() {
		return asignadoA;
	}

	public void setAsignadoA(Empleado asignadoA) {
		this.asignadoA = asignadoA;
	}

	@Override
    public double calcularCostoTotal() {
        double rep = repuestos.stream().mapToDouble(ItemRepuesto::calcularCostoTotal).sum();
        double serv = servicios.stream().mapToDouble(LineaServicio::calcularCostoTotal).sum();
        double neto = rep + serv;
        return neto * (1 + IVA);
	}
	
	public void agregarRepuesto(ItemRepuesto r) throws RepuestoDuplicadoException {
	    boolean existe = repuestos.stream().anyMatch(x -> x.getCodigo().equals(r.getCodigo()));
	    if (existe) throw new RepuestoDuplicadoException("Repuesto ya agregado: " + r.getCodigo());
	    repuestos.add(r);
	}

	public void agregarServicio(LineaServicio lineaServicio) {
		this.servicios.add(lineaServicio);
	}
	public List<ItemRepuesto> getRepuestos() {
	    return repuestos;
	}

	public float getHorasTrabajadas() {
		// TODO Auto-generated method stub
		return horasTrabajadas;
	}


}
