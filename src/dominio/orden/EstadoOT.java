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

    //  Método auxiliar
    public static EstadoOT fromString(String texto) {
        for (EstadoOT e : EstadoOT.values()) {
            if (e.getEstado().equalsIgnoreCase(texto.trim())) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + texto);
    }
}
