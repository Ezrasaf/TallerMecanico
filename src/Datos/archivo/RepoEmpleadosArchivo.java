package Datos.archivo;

import Datos.RepositorioEmpleados;
import dominio.empleado.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación en CSV/TXT para empleados.
 * Formato por línea:
 * legajo;tipo;tarifaHora;especialidad;nivel
 *
 * Donde tipo ∈ {MECANICO, RECEPCIONISTA}
 */
public class RepoEmpleadosArchivo implements RepositorioEmpleados {
    private final Path file;

    public RepoEmpleadosArchivo(String filename) {
        this.file = Paths.get(filename);
        try {
            Path parent = this.file.getParent();
            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(this.file)) {
                Files.createFile(this.file);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el archivo de empleados: " + filename, e);
        }
    }

    /** Guarda o actualiza un empleado por legajo */
    @Override
    public synchronized void guardar(Empleado e) {
        List<Empleado> empleados = listar();

        boolean reemplazado = false;
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getLegajo() == e.getLegajo()) {
                empleados.set(i, e);
                reemplazado = true;
                break;
            }
        }
        if (!reemplazado) empleados.add(e);

        actualizar(empleados);
    }

    /** Lista todos los empleados */
    @Override
    public List<Empleado> listar() {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return br.lines()
                    .map(this::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo de empleados", e);
        }
    }

    /** Elimina un empleado por legajo */
    @Override
    public synchronized void eliminar(int legajo) {
        List<Empleado> empleados = listar()
                .stream()
                .filter(emp -> emp.getLegajo() != legajo)
                .collect(Collectors.toList());
        actualizar(empleados);
    }

    /** Reescribe el archivo completo con la lista actualizada */
    @Override
    public synchronized void actualizar(List<Empleado> empleados) {
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Empleado e : empleados) {
                bw.write(format(e));
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error actualizando archivo de empleados", e);
        }
    }

    // ==========================
    // Conversión Empleado ↔ CSV
    // ==========================

    private String format(Empleado e) {
        if (e instanceof Mecanico m) {
            return String.join(";",
                    String.valueOf(m.getLegajo()),
                    "MECANICO",
                    String.valueOf(m.tarifaHora()),
                    escape(m.getEspecialidad()),
                    m.getNivel().name()
            );
        } else if (e instanceof Recepcionista r) {
            return String.join(";",
                    String.valueOf(r.getLegajo()),
                    "RECEPCIONISTA",
                    String.valueOf(r.tarifaHora()),
                    "",
                    ""
            );
        }
        return "";
    }

    private Empleado parse(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split(";");
        if (parts.length < 3) return null;

        try {
            int legajo = Integer.parseInt(parts[0]);
            String tipo = parts[1].trim();
            double tarifaHora = Double.parseDouble(parts[2]);

            if ("MECANICO".equalsIgnoreCase(tipo)) {
                String especialidad = parts.length > 3 ? unescape(parts[3]) : "";
                Nivel nivel = parts.length > 4 && !parts[4].isEmpty()
                        ? Nivel.valueOf(parts[4])
                        : Nivel.JUNIOR;
                // Convertimos tarifaHora * 160 para obtener salario mensual aprox.
                return new Mecanico(legajo, especialidad, tarifaHora * 160, nivel);
            } else if ("RECEPCIONISTA".equalsIgnoreCase(tipo)) {
                return new Recepcionista(legajo, tarifaHora);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace(";", "\\;");
    }

    private String unescape(String s) {
        return s == null ? "" : s.replace("\\;", ";").replace("\\\\", "\\");
    }
}
