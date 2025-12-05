# 🛠️ Sistema de Gestión para Taller Mecánico  
### Proyecto Académico – Paradigma Orientado a Objetos (UADE)

Este proyecto implementa un **sistema integral para la gestión operativa de un taller mecánico**, incluyendo manejo de clientes, vehículos, órdenes de trabajo, servicios, repuestos e historial por empleado.

Diseñado en **Java**, con arquitectura en capas, **principios SOLID**, **patrones GRASP**, persistencia en **Base de Datos MySQL** y una interfaz gráfica en **Swing**.

Presentacion: https://gamma.app/docs/Sistema-de-Gestion-para-Taller-Mecanico-sk2prxrmf6xqv9k

---

## 📸 Vista General del Proyecto

![Dashboard](img/dashboard.png)

---

# 📦 Objetivos del Sistema

- Digitalizar las operaciones del taller  
- Registrar clientes y sus vehículos  
- Crear y administrar órdenes de trabajo  
- Controlar servicios y repuestos utilizados  
- Asignar mecánicos y validar su disponibilidad  
- Registrar historial por empleado  
- Mantener los datos en una **base de datos relacional** de forma segura  

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
- `Historial`

📌 **UML (vista general del sistema):**

![UML](img/uml_taller.png)

---

## **2️⃣ Capa de Datos (Persistencia – Repositorios + MySQL)**

Persistencia basada en **MySQL** utilizando **JDBC**:

- `RepoClientesBD`
- `RepoVehiculosBD`
- `RepoRepuestosBD`
- `RepoServiciosBD`
- `RepoOrdenesBD`
- `RepoEmpleadosBD`
- `ConexionDB` para administrar la conexión

Características:

- Creación automática de tablas (si no existen)  
- Consultas parametrizadas para listar, insertar, actualizar y eliminar  
- Capa de acceso a datos desacoplada de la lógica de negocio  
- Centralización de la configuración de conexión (URL, usuario, password)

---

## **3️⃣ Capa de Presentación (Swing UI)**

### 👤 **Gestión de Clientes**
![Clientes](img/clientes.png)

---

### 🚗 **Gestión de Vehículos**
![Vehículos](img/vehiculos.png)

---

### 🛠️ **Gestión de Empleados**
![Empleados](img/empleados.png)

---

### 📄 **Órdenes de Trabajo**
Incluye:
- Estado  
- Prioridad  
- Mecánico asignado  
- Fecha de ingreso  
- Diagnóstico  
- Horas trabajadas  
- Servicios y repuestos  

![Orden de Trabajo](img/orden_trabajo.png)

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

### ✔ Persistencia en BD  
Todos los datos se almacenan en **MySQL** y se recuperan al iniciar el sistema.

---

# 🧠 Principios de Diseño Aplicados

## **🔹 SOLID**

| Principio | Aplicación |
|----------|------------|
| SRP | Repositorios separados por entidad, controladores independientes |
| OCP | Nuevas vistas o lógicas sin modificar código existente |
| LSP | `Mecanico` y `Recepcionista` sustituyen a `Empleado` sin romper nada |
| ISP | Interfaces pequeñas (`Facturable`) |
| DIP | Controladores dependen de interfaces / abstracciones, no de implementaciones concretas |

---

## **🔹 GRASP**

- **Controller:** controladores por entidad → menos acoplamiento  
- **Information Expert:** Orden calcula su total; Mecánico sabe si está disponible  
- **Creator:** OrdenController crea órdenes y asignaciones  
- **Low Coupling / High Cohesion:** vistas separadas de la lógica  
- **Indirection:** capa controladores gestiona la comunicación entre UI y BD  

---

# 🔥 Mejoras Implementadas (Segunda Entrega)

- ✔ Migración del modelo a **Base de Datos MySQL**  
- ✔ Nombre agregado a empleados  
- ✔ Archivo/tabla normalizada para empleados  
- ✔ Combo de selección con nombres completos  
- ✔ Historial completo por mecánico  
- ✔ Validación de órdenes activas por empleado  
- ✔ Reestructuración de controladores  
- ✔ Nuevos métodos en repositorios basados en BD  
- ✔ UML actualizado con nuevas clases y componentes  

---

# 🗂️ Estructura del Proyecto

```text
/src
   /dominio
      Cliente.java
      Empleado.java
      Mecanico.java
      ...
   /datos
      ConexionDB.java
      RepoClientesBD.java
      RepoOrdenesBD.java
      RepoVehiculosBD.java
      ...
   /controlador
      ClienteController.java
      OrdenController.java
      DashboardControlador.java
   /vista
      DashboardView.java
      OrdenView.java
      ClienteView.java
      VehiculoView.java
      EmpleadoView.java



---

# ▶️ Cómo Ejecutarlo

1. Clonar el repositorio
2. Crear la base de datos en MySQL (por ejemplo):

CREATE DATABASE tallermecanico;

3. Ajustar en ConexionDB los parámetros de conexión:

URL      = "jdbc:mysql://localhost:3306/tallermecanico";
USUARIO  = "root";
PASSWORD = "tu_password";

4. Asegurarse de tener el MySQL Connector en el classpath
5.Abrir el proyecto en IntelliJ IDEA / Eclipse
6.Ejecutar la clase App.java
7.Navegar desde el Dashboard

---

# 🧪 Pruebas y Validaciones

- Pruebas unitarias incluidas en `/Pruebas_unitarias`  
- Validación de disponibilidad de mecánicos  
- Manejo de errores de conexión a BD 
- Validaciones de entradas en formularios Swing  

---

# 📘 Tecnologías Utilizadas

- **Java 17**  
- **Swing**  
- **MySQL + JDBC (MySQL Connector/J)**  
- **UML + Buenas Prácticas de Diseño**  
- **Git & GitHub**  

---

# 👤 Autor

**Ezra Safadie**  
Estudiante de Ingeniería en Informática (UADE)  
Intereses: Backend, Datos, Automatización, IA.

---

# ⭐ Valor para Portfolio

Este proyecto demuestra:

- Programación orientada a objetos sólida  
- Arquitectura por capas con acceso a datos real sobre BD
- Dominio de patrones SOLID y GRASP  
- Construcción de una aplicación real con reglas de negocio  
- Uso profesional de Swing  
- Persistencia e Integración Java + MySQL con JDBC 
- UML aplicado a un caso real  

