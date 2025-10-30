package dominio.orden;

public enum Prioridad {
	ALTA("Alta"),
	MEDIA("Media"),
	BAJA("Baja"),
	URGENTE("Urgente");

	private final String prioridad;

	Prioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public String getPrioridad() {
		return prioridad;
	}
}