package dominio.orden;

public class ItemRepuesto {
	private String codigo;
	private String descripcion;
	private int cantidad;
	private double precioUnitario;

	public ItemRepuesto(String codigo, String descripcion, int cantidad, double precioUnitario) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	private double calcularSubTotal() {
		return this.cantidad * this.precioUnitario;
	}

	public double subTotal() {
		return calcularSubTotal();
	}
}
