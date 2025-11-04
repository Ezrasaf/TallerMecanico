package persistencia.archivo;

import persistencia.RepositorioVehiculos;
import dominio.vehiculo.Vehiculo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio de Vehículos en CSV simple.
 * Formato: patente;marca;modelo;anio;vin
 */
public class RepoVehiculosArchivo implements RepositorioVehiculos {
    private final Path file;

    public RepoVehiculosArchivo(String filename) {
        this.file = Paths.get(filename);
        try {
            Path parent = this.file.getParent();
            if (parent != null && Files.notExists(parent)) Files.createDirectories(parent);
            if (Files.notExists(this.file)) Files.createFile(this.file);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar archivo de vehiculos: " + filename, e);
        }
    }

    @Override
    public synchronized void guardar(Vehiculo v) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            lines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            lines = new ArrayList<>();
        }

        String key = v.getPatente();
        String record = format(v);
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
            throw new RuntimeException("Error escribiendo archivo de vehiculos", e);
        }
    }

    @Override
    public Optional<Vehiculo> buscarPorPatente(String patente) {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return br.lines()
                    .map(this::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(v -> v.getPatente().equalsIgnoreCase(patente))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo de vehiculos", e);
        }
    }

    @Override
    public List<Vehiculo> listar() {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return br.lines()
                    .map(this::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo de vehiculos", e);
        }
    }

    private String format(Vehiculo v) {
        return String.join(";",
                escape(v.getPatente()),
                escape(v.getMarca()),
                escape(v.getModelo()),
                String.valueOf(v.getAnio()),
                escape(v.getVin() == null ? "" : v.getVin())
        );
    }

    private Optional<Vehiculo> parse(String line) {
        if (line == null || line.trim().isEmpty()) return Optional.empty();
        String[] parts = line.split(";");
        if (parts.length < 5) return Optional.empty();
        try {
            String patente = unescape(parts[0]);
            String marca = unescape(parts[1]);
            String modelo = unescape(parts[2]);
            int anio = Integer.parseInt(parts[3]);
            String vin = unescape(parts[4]);
            Vehiculo v = new Vehiculo(marca, modelo, patente, anio, vin);
            return Optional.of(v);
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
