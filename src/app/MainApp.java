package app;

import dominio.orden.*;
import Datos.OrdenArchivoRepositorio;
import repositorio.Repositorio;
import servicio.OrdenService;

import java.time.LocalDate;

public class MainApp {
    public static void main(String[] args) throws Exception {
        Repositorio<OrdenDeTrabajo, Integer> repo = new OrdenArchivoRepositorio("data/ordenes.csv");
        OrdenService service = new OrdenService(repo);

        OrdenDeTrabajo ot = new OrdenDeTrabajo(
                1001, LocalDate.now(), EstadoOT.ABIERTA, "Cambio de pastillas",
                Prioridad.MEDIA, null, null, 2.5
        );
        ot.agregarServicio(new LineaServicio("Mano de obra", 2.5, 8000));
        ot.agregarRepuesto(new ItemRepuesto("PAST123","Pastillas freno", 1, 35000));

        service.registrar(ot);
        System.out.println("Registrada OT #" + ot.getNumeroOrden() + " | Total c/IVA: " + ot.calcularCostoTotal());
        System.out.println("Órdenes en archivo: " + service.listar().size());
    }
}
