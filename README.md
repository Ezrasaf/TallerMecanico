# 🛠️ Sistema de Gestión para Taller Mecánico  
### Proyecto Académico – Paradigma Orientado a Objetos (UADE)

Este proyecto implementa un **sistema integral para la gestión operativa de un taller mecánico**, incluyendo manejo de clientes, vehículos, órdenes de trabajo, servicios, repuestos e historial por empleado.

Diseñado en **Java**, con arquitectura en capas, **principios SOLID**, **patrones GRASP**, persistencia por archivos y una interfaz gráfica en **Swing**.

---

## 📸 Vista General del Proyecto

> *(Reemplazá `img/dashboard.png` por el nombre real de tu captura)*

![Dashboard](img/dashboard.png)

---

# 📦 Objetivos del Sistema

- Digitalizar las operaciones del taller  
- Registrar clientes y sus vehículos  
- Crear y administrar órdenes de trabajo  
- Controlar servicios y repuestos utilizados  
- Asignar mecánicos y validar su disponibilidad  
- Registrar historial por empleado  
- Mantener persistencia en archivos de forma segura  

---

# 🧩 Arquitectura del Sistema

El diseño cumple con POO, SOLID y GRASP, y está organizado en **3 capas principales**:

---

## **1️⃣ Capa de Dominio (Modelo)**

Contiene las entidades fundamentales del negocio:

- `Cliente`
- `Empleado` (subclases: `Mecanico`, `Recepcionista`)
- `Vehiculo`
- `OrdenDeTrabajo`
- `LineaServicio`
- `ItemRepuesto`
- `Historial` *(mejora incorporada)*

> Ejemplo UML (del informe):  
![Diagrama UML](img/uml_taller.png)

> *Reemplazá esta imagen por tu captura real.*

---

## **2️⃣ Capa de Datos (Persistencia – DAO)**

Persistencia basada en archivos CSV/TXT:

- `RepoClientesArchivo`
- `RepoVehiculosArchivo`
- `RepoRepuestosArchivo`
- `RepoServiciosArchivo`
- `RepoOrdenesArchivo`
- `RepoEmpleadosArchivo`

Mejoras implementadas:

- Manejo seguro de archivos con `try-with-resources`  
- `proximoNumero()` para autogeneración de órdenes  
- Búsqueda por legajo y filtrado por mecánico  
- Validación de datos y formato estandarizado  

---

## **3️⃣ Capa de Presentación (Swing UI)**

Ventanas principales:

- Dashboard  
- Gestión de Clientes  
- Gestión de Vehículos  
- Gestión de Órdenes  
- Asignación de Mecánicos  
- Ventana de **Historial por Empleado**

Ejemplo visual:

![UI Example](img/orden_trabajo.png)

---

# ⚙️ Funcionalidades Principales

### ✔ Gestión de Clientes y Vehículos  
- ABM completo  
- Relación 1:N cliente → vehículos  

### ✔ Gestión de Órdenes de Trabajo  
- Número autoincremental  
- Estado, prioridad, diagnóstico  
- Servicios y repuestos incluidos  
- Cálculo automático del costo total  

### ✔ Control de Disponibilidad del Mecánico  
Cada mecánico puede tener **máximo 2 órdenes activas**.

### ✔ Historial por Empleado  
Se puede consultar:  
- Fecha  
- Cliente  
- Vehículo  
- Tareas realizadas  
- Tiempo empleado  

### ✔ Persistencia Completa  
Todo queda almacenado y puede recuperarse al iniciar el sistema.

---

# 🧠 Principios de Diseño Aplicados

## **🔹 SOLID**

| Principio | Aplicación |
|----------|------------|
| SRP | Repositorios separados por entidad, controladores independientes |
| OCP | Nuevas persistencias o vistas sin modificar código existente |
| LSP | `Mecanico` y `Recepcionista` sustituyen a `Empleado` sin romper nada |
| ISP | Interfaces pequeñas (ej. `Facturable`) |
| DIP | Controladores dependen de interfaces, no de implementaciones |

---

## **🔹 GRASP**

- **Controller:** controladores por entidad → menos acoplamiento  
- **Information Expert:** Orden calcula su total, Mecánico sabe si está disponible  
- **Creator:** OrdenController crea órdenes y asignaciones  
- **Low Coupling / High Cohesion:** vistas separadas de la lógica  
- **Indirection:** capa controladores gestiona la comunicación  

---

# 🔥 Mejoras Implementadas (Segunda Entrega)

- ✔ Nombre agregado a empleados  
- ✔ Archivo normalizado para empleados  
- ✔ Combo de selección con nombres completos  
- ✔ Historial completo por mecánico  
- ✔ Validación de órdenes activas  
- ✔ Reestructuración de controladores  
- ✔ Nuevos métodos en repositorios  
- ✔ UML actualizado con nuevas clases  

---

# 🗂️ Estructura del Proyecto

/src
/dominio
Cliente.java
Empleado.java
Mecanico.java
...
/persistencia
RepoClientesArchivo.java
RepoOrdenesArchivo.java
...
/controladores
ClienteController.java
OrdenController.java
/vista
DashboardView.java
OrdenView.java
...
/data
clientes.csv
empleados.csv
ordenes.csv
vehiculos.csv

# ▶️ Cómo Ejecutarlo

1. Clonar el repositorio  
2. Abrir el proyecto en **IntelliJ IDEA / Eclipse**  
3. Verificar que la carpeta `/data` existe  
4. Ejecutar la clase `App.java`  
5. Navegar desde el Dashboard  

---

# 🧪 Pruebas y Validaciones

- Pruebas unitarias incluidas en `/Pruebas_unitarias`  
- Validación de disponibilidad de mecánicos  
- Persistencia asegurada ante cierres imprevistos  
- Validaciones de entradas en formularios Swing  

---

# 📘 Tecnologías Utilizadas

- **Java 17**  
- **Swing**  
- **CSV/TXT Persistencia**  
- **UML + Buenas Prácticas de Diseño**  
- **Git & GitHub**  

---

# 👤 Autor

**Ezrasaf**  
Estudiante de Ingeniería en Informática (UADE)  
Intereses: Desarrollo Backend, Ingeniería de Datos, Automatización, IA.

---

# ⭐ Valor para Portfolio

Este proyecto demuestra:

- Programación orientada a objetos sólida  
- Arquitectura por capas  
- Dominio de patrones SOLID y GRASP  
- Construcción de una aplicación real con reglas de negocio  
- Uso profesional de Swing  
- Persistencia y manejo de datos  
- UML aplicado a un caso real  
