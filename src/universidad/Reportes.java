package universidad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.TreeSet;

import comparadores.ComparadorPorCantReservas;;

public class Reportes {
    public static List<String> reporteMontoRecaudado(TreeMap<Integer, Aula> aulas) throws IOException {
        List<String> reporte = new ArrayList<>();
        float montoAula, montoPiso, montoTotal;
        int pisoAct;
        montoTotal = 0;

        // Itera sobre las aulas
        Iterator<Aula> itA = aulas.values().iterator();

        Aula aulaAct = null;
        if (itA.hasNext()) {
            aulaAct = itA.next();
        }

        while (aulaAct != null) {
            pisoAct = aulaAct.getPiso();
            reporte.add("Piso Actual: " + pisoAct);
            montoPiso = 0;

            // Mientras se encuentre en el mismo piso, recorre.
            while (aulaAct != null && aulaAct.getPiso() == pisoAct) {
                montoAula = aulaAct.getMontoAula();
                montoPiso += montoAula;
                reporte.add(" El Aula " + aulaAct.getNum() + " recaudo un total de $ " + montoAula);

                // Avanzar el iterador y verificar si hay más elementos
                if (itA.hasNext()) {
                    aulaAct = itA.next();
                } else {
                    aulaAct = null;
                }
            }

            montoTotal += montoPiso;
            reporte.add(" El Piso " + pisoAct + " recaudo un total de $ " + montoPiso);
        }

        reporte.add("El monto total es de $" + montoTotal + "\n");
        return reporte;
    }

    public static List<String> reporteListadoAulas(TreeMap<Integer, Aula> aulas) {
        List<String> reporte = new ArrayList<>();
        int cantAulas, resTot;
        cantAulas = resTot = 0;
        ComparadorPorCantReservas comp = new ComparadorPorCantReservas();
        TreeSet<Aula> aulasOrdenadas = new TreeSet<>(comp);

        for (Aula aulaAct : aulas.values()) { // Recorre container de aulas.
            cantAulas++;
            resTot += aulaAct.getCantReservas();
            aulasOrdenadas.add(aulaAct);
        }

        reporte.add("Listado:");
        for (Aula aulaAct : aulasOrdenadas) {
            reporte.add(aulaAct.toString());
        }
        reporte.add("El promedio de reservas por aulas es de " + (float) resTot / cantAulas + "\n");

        return reporte;
    }

    public static void escribirReporteEnArchivo(List<String> reporte, String nombreArchivo) throws IOException {
        PrintWriter escritor = new PrintWriter(new BufferedWriter(new FileWriter(nombreArchivo)));
        for (String linea : reporte) {
            escritor.println(linea);
        }
        escritor.close();
    }

    public static void mostrarReporteEnConsola(List<String> reporte) {
        for (String linea : reporte) {
            System.out.println(linea);
        }
    }
}
