package dominio.orden;

public enum EstadoOT {
	ABIERTA("Abierto"),
	EN_PROCESO("En Proceso"),
	CERRADA("Cerrada"),
	ENTREGADA("Entregada");

	private final String estado;

	EstadoOT(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}
}
