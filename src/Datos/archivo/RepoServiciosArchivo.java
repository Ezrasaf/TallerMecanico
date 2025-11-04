package Datos.archivo;

import dominio.orden.LineaServicio;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class RepoServiciosArchivo {
    private final Path file;

    public RepoServiciosArchivo(String filename) {
        this.file = Paths.get(filename);
        try {
            Path parent = this.file.getParent();
            if (parent != null && Files.notExists(parent)) Files.createDirectories(parent);
            if (Files.notExists(this.file)) Files.createFile(this.file);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el archivo de servicios", e);
        }
    }

    public synchronized void guardar(LineaServicio s) {
        List<LineaServicio> lista = listar();
        boolean reemplazado = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getDescripcion().equalsIgnoreCase(s.getDescripcion())) {
                lista.set(i, s);
                reemplazado = true;
                break;
            }
        }
        if (!reemplazado) lista.add(s);
        actualizar(lista);
    }

    public synchronized void eliminar(String descripcion) {
        List<LineaServicio> lista = listar().stream()
                .filter(s -> !s.getDescripcion().equalsIgnoreCase(descripcion))
                .collect(Collectors.toList());
        actualizar(lista);
    }

    public synchronized List<LineaServicio> listar() {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return br.lines()
                    .map(this::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void actualizar(List<LineaServicio> lista) {
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            for (LineaServicio s : lista) {
                bw.write(String.join(";", s.getDescripcion(),
                        String.valueOf(s.getTarifaHora())));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo archivo de servicios", e);
        }
    }

    private LineaServicio parse(String line) {
        try {
            String[] p = line.split(";");
            if (p.length < 2) return null;
            return new LineaServicio(p[0], 1, Double.parseDouble(p[1]));
        } catch (Exception e) {
            return null;
        }
    }
}
