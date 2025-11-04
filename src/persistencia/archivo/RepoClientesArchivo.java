package persistencia.archivo;

import persistencia.RepositorioClientes;
import dominio.cliente.Cliente;

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
 * Implementación simple en CSV/TXT para clientes.
 * Formato por línea: id;nombre;edad;telefono;email
 */
public class RepoClientesArchivo implements RepositorioClientes {
    private final Path file;

    public RepoClientesArchivo(String filename) {
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
            throw new RuntimeException("No se pudo inicializar el archivo de clientes: " + filename, e);
        }
    }

    @Override
    public synchronized void guardar(Cliente c) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            lines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            // si hay un problema leyendo, intentamos continuar creando el archivo
            lines = new ArrayList<>();
        }

        String id = String.valueOf(c.getId());
        String record = format(c);

        boolean reemplazado = false;
        for (int i = 0; i < lines.size(); i++) {
            String ln = lines.get(i);
            if (ln.startsWith(id + ";")) {
                lines.set(i, record);
                reemplazado = true;
                break;
            }
        }
        if (!reemplazado) lines.add(record);

        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo archivo de clientes", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(String dni) {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return br.lines()
                    .map(this::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(c -> String.valueOf(c.getId()).equals(dni))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo de clientes", e);
        }
    }

    @Override
    public List<Cliente> listar() {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return br.lines()
                    .map(this::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo de clientes", e);
        }
    }

    private String format(Cliente c) {
        // id;nombre;edad;telefono;email
        return String.join(";",
                String.valueOf(c.getId()),
                escape(c.getNombre()),
                String.valueOf(c.getEdad()),
                String.valueOf(c.getTelefono()),
                escape(c.getEmail() == null ? "" : c.getEmail())
        );
    }

    private Optional<Cliente> parse(String line) {
        if (line == null || line.trim().isEmpty()) return Optional.empty();
        String[] parts = line.split(";");
        if (parts.length < 5) return Optional.empty();
        try {
            int id = Integer.parseInt(parts[0]);
            String nombre = unescape(parts[1]);
            int edad = Integer.parseInt(parts[2]);
            int telefono = Integer.parseInt(parts[3]);
            String email = unescape(parts[4]);
            Cliente c = new Cliente(nombre, edad, telefono, id, email);
            return Optional.of(c);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // simple escaping to avoid semicolons in text
    private String escape(String s) {
        return s.replace("\\", "\\\\").replace(";", "\\;");
    }

    private String unescape(String s) {
        return s.replace("\\;", ";").replace("\\\\", "\\");
    }
}
