package Datos.archivo;

import dominio.orden.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class RepoOrdenesArchivo {
    private final Path file;

    public RepoOrdenesArchivo(String filename) {
        this.file = Paths.get(filename);
        try {
            Path parent = this.file.getParent();
            if (parent != null && Files.notExists(parent)) Files.createDirectories(parent);
            if (Files.notExists(this.file)) Files.createFile(this.file);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el archivo: " + filename, e);
        }
    }

    public synchronized void guardar(OrdenDeTrabajo orden) {
        List<OrdenDeTrabajo> ordenes = listar();
        boolean reemplazada = false;

        for (int i = 0; i < ordenes.size(); i++) {
            if (ordenes.get(i).getNumeroOrden() == orden.getNumeroOrden()) {
                ordenes.set(i, orden);
                reemplazada = true;
                break;
            }
        }

        if (!reemplazada) ordenes.add(orden);
        actualizar(ordenes);
    }

    public synchronized void eliminar(int numeroOrden) {
        List<OrdenDeTrabajo> ordenes = listar().stream()
                .filter(o -> o.getNumeroOrden() != numeroOrden)
                .collect(Collectors.toList());
        actualizar(ordenes);
    }

    private synchronized void actualizar(List<OrdenDeTrabajo> ordenes) {
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            for (OrdenDeTrabajo o : ordenes) {
                bw.write(format(o));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error actualizando CSV de órdenes", e);
        }
    }

    public List<OrdenDeTrabajo> listar() {
        if (!Files.exists(file)) return new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            List<OrdenDeTrabajo> ordenes = br.lines()
                    .map(this::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            System.out.println("Ordenes leídas del archivo: " + ordenes.size());
            return ordenes;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String format(OrdenDeTrabajo o) {
        return String.join(";",
                String.valueOf(o.getNumeroOrden()),
                o.getFechaIngreso().toString(),
                o.getEstado().name(),
                o.getDiagnostico(),
                o.getPrioridad().name(),
                String.valueOf(o.getLegajoEmpleado()),
                String.valueOf(o.getHorasTrabajadas())
        );
    }

    private Optional<OrdenDeTrabajo> parse(String line) {
        try {
            String[] p = line.split(";");
            if (p.length < 7) return Optional.empty();

            int nro = Integer.parseInt(p[0]);
            LocalDate fecha = LocalDate.parse(p[1]);

            // 👇 Esta parte es la clave: asegura que el enum se lea bien
            EstadoOT estado;
            try {
                estado = EstadoOT.valueOf(p[2].toUpperCase());
            } catch (Exception e) {
                // fallback si el texto es "Abierta" o similar
                estado = EstadoOT.ABIERTA;
            }

            String diag = p[3];
            Prioridad pr = Prioridad.valueOf(p[4].toUpperCase());
            int legajo = Integer.parseInt(p[5]);
            float horas = Float.parseFloat(p[6]);

            return Optional.of(new OrdenDeTrabajo(nro, fecha, estado, diag, pr, legajo, horas));
        } catch (Exception e) {
            System.err.println("Error parseando línea: " + line);
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
