package dominio.general;

public abstract class Persona {
	private int id;
	private String nombre;
	private int edad;
	private int telefono;

	public Persona(String nombre, int edad, int telefono, int id) {
		this.nombre = nombre;
		this.edad = edad;
		this.telefono = telefono;
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public int getEdad() {
		return edad;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
