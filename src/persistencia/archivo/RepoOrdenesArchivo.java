package persistencia.archivo;

import persistencia.RepositorioOrdenes;
import dominio.orden.OrdenDeTrabajo;
import dominio.empleado.Mecanico;
import dominio.empleado.Nivel;
import dominio.orden.EstadoOT;
import dominio.orden.Prioridad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio de Ordenes en CSV simple.
 * Formato: numeroOrden;fechaISO;estado;diagnostico;prioridad;asignadoLegajo;horasTrabajadas
 */
public class RepoOrdenesArchivo implements RepositorioOrdenes {
    private final Path file;

    public RepoOrdenesArchivo(String filename) {
        this.file = Paths.get(filename);
        try {
            Path parent = this.file.getParent();
            if (parent != null && Files.notExists(parent)) Files.createDirectories(parent);
            if (Files.notExists(this.file)) Files.createFile(this.file);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar archivo de ordenes: " + filename, e);
        }
    }

    @Override
    public synchronized void guardar(OrdenDeTrabajo o) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            lines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            lines = new ArrayList<>();
        }

        String key = String.valueOf(o.getNumeroOrden());
        String record = format(o);
        boolean replaced = false;
        for (int i = 0; i < lines.size(); i++) {
            String ln = lines.get(i);
            if (ln.startsWith(key + ";")) {
                lines.set(i, record);
                replaced = true;
                break;
            }
        }
        if (!replaced) lines.add(record);

        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo archivo de ordenes", e);
        }
    }

    @Override
    public Optional<OrdenDeTrabajo> buscarPorId(int numeroOrden) {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return br.lines()
                    .map(this::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(o -> o.getNumeroOrden() == numeroOrden)
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo de ordenes", e);
        }
    }

    @Override
    public List<OrdenDeTrabajo> listar() {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return br.lines()
                    .map(this::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo de ordenes", e);
        }
    }

    private String format(OrdenDeTrabajo o) {
        String asignadoLegajo = o.getAsignadoA() == null ? "" : String.valueOf(o.getAsignadoA().getLegajo());
        return String.join(";",
                String.valueOf(o.getNumeroOrden()),
                o.getFechaIngreso().toString(),
                o.getEstado().name(),
                escape(o.getDiagnostico() == null ? "" : o.getDiagnostico()),
                o.getPrioridad().name(),
                asignadoLegajo,
                String.valueOf(o.getHorasTrabajadas())
        );
    }

    private Optional<OrdenDeTrabajo> parse(String line) {
        if (line == null || line.trim().isEmpty()) return Optional.empty();
        String[] parts = line.split(";");
        if (parts.length < 7) return Optional.empty();
        try {
            int numero = Integer.parseInt(parts[0]);
            LocalDate fecha = LocalDate.parse(parts[1]);
            EstadoOT estado = EstadoOT.valueOf(parts[2]);
            String diagnostico = unescape(parts[3]);
            Prioridad prioridad = Prioridad.valueOf(parts[4]);
            String legajoStr = parts[5];
            int legajo = legajoStr == null || legajoStr.isEmpty() ? 0 : Integer.parseInt(legajoStr);
            float horas = Float.parseFloat(parts[6]);
            Mecanico mecanico = legajo == 0 ? new Mecanico(0, "", 0.0, Nivel.JUNIOR) : new Mecanico(legajo, "", 0.0, Nivel.JUNIOR);
            OrdenDeTrabajo o = new OrdenDeTrabajo(numero, fecha, estado, diagnostico, prioridad, mecanico, horas);
            return Optional.of(o);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace(";", "\\;");
    }

    private String unescape(String s) {
        return s.replace("\\;", ";").replace("\\\\", "\\");
    }
}
