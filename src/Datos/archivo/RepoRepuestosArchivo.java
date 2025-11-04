package Datos.archivo;

import dominio.orden.ItemRepuesto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class RepoRepuestosArchivo {
    private final Path file;

    public RepoRepuestosArchivo(String filename) {
        this.file = Paths.get(filename);
        try {
            Path parent = this.file.getParent();
            if (parent != null && Files.notExists(parent)) Files.createDirectories(parent);
            if (Files.notExists(this.file)) Files.createFile(this.file);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el archivo de repuestos", e);
        }
    }

    public synchronized void guardar(ItemRepuesto r) {
        List<ItemRepuesto> lista = listar();
        boolean reemplazado = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo().equals(r.getCodigo())) {
                lista.set(i, r);
                reemplazado = true;
                break;
            }
        }
        if (!reemplazado) lista.add(r);
        actualizar(lista);
    }

    public synchronized void eliminar(String codigo) {
        List<ItemRepuesto> lista = listar().stream()
                .filter(r -> !r.getCodigo().equals(codigo))
                .collect(Collectors.toList());
        actualizar(lista);
    }

    public synchronized List<ItemRepuesto> listar() {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return br.lines()
                    .map(this::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void actualizar(List<ItemRepuesto> lista) {
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            for (ItemRepuesto r : lista) {
                bw.write(String.join(";", r.getCodigo(), r.getDescripcion(),
                        String.valueOf(r.getPrecioUnitario())));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo archivo de repuestos", e);
        }
    }

    private ItemRepuesto parse(String line) {
        try {
            String[] p = line.split(";");
            if (p.length < 3) return null;
            return new ItemRepuesto(p[0], p[1], 1, Double.parseDouble(p[2]));
        } catch (Exception e) {
            return null;
        }
    }
}
