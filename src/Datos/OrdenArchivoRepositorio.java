package Datos;

import repositorio.Repositorio;
import dominio.orden.OrdenDeTrabajo;
import dominio.orden.EstadoOT;
import dominio.orden.Prioridad;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class OrdenArchivoRepositorio implements Repositorio<OrdenDeTrabajo, Integer> {
    private final File archivo;

    public OrdenArchivoRepositorio(String ruta) {
        this.archivo = new File(ruta);
        if (archivo.getParentFile() != null) archivo.getParentFile().mkdirs();
    }

    @Override
    public void guardar(OrdenDeTrabajo ot) throws IOException {
        boolean existe = archivo.exists();
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, true))) {
            if (!existe) {
                pw.println("numeroOrden;fechaIngreso;estado;prioridad;diagnostico;horasTrabajadas");
            }
            pw.printf("%d;%s;%s;%s;%s;%.2f%n",
                    ot.getNumeroOrden(),
                    ot.getFechaIngreso(),
                    ot.getEstado(),
                    ot.getPrioridad(),
                    safe(ot.getDiagnostico()),
                    ot.getHorasTrabajadas()
            );
        }
    }

    @Override
    public List<OrdenDeTrabajo> listar() throws IOException {
        List<OrdenDeTrabajo> out = new ArrayList<>();
        if (!archivo.exists()) return out;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea = br.readLine(); // header
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";", -1);
                int nro = Integer.parseInt(p[0]);
                LocalDate fecha = LocalDate.parse(p[1]);
                EstadoOT estado = EstadoOT.valueOf(p[2]);
                Prioridad pr = Prioridad.valueOf(p[3]);
                String diag = p[4];
                double horas = Double.parseDouble(p[5]);

                OrdenDeTrabajo ot = new OrdenDeTrabajo(nro, fecha, estado, diag, pr, null, null, horas);
                out.add(ot);
            }
        }
        return out;
    }

    @Override
    public Optional<OrdenDeTrabajo> buscarPorId(Integer id) throws IOException {
        return listar().stream().filter(o -> o.getNumeroOrden() == id).findFirst();
    }

    private static String safe(String s) {
        if (s == null) return "";
        return s.replace(";", ",");
    }
}
