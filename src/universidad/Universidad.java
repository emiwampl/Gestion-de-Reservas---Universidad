package universidad;

import excepciones.NoEncuentraReservadorException;
import excepciones.NoDisponibilidadException;
import universidad.entidades.RangoHora;
import universidad.entidades.Reservador;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import comparadores.ComparadorPorCapacidadAula;
import ejecutables.Menu;

/**
 * Clase Uni que representa una Universidad con funcionalidades para administrar
 * aulas y reservas.
 * Esta clase implementa la interfaz Serializable para permitir la serializacion
 * de objetos.
 */

public class Universidad implements Serializable {

    /**
     * Atributos:
     * - aulas: TreeMap<Integer, Aula> - Mapa ordenado de aulas indexado por numero
     * de aula.
     * * - reservadores: HashSet<Reservador> - Conjunto de reservadores asociados a
     * la universidad.
     * * - sc: Scanner (transient) - Objeto Scanner para entrada estandar.
     * * - serialVersionUID: long - Numero de versión de serializacion.
     */

    private TreeMap<Integer, Aula> aulas = new TreeMap<>();
    private HashSet<Reservador> reservadores = new HashSet<>();

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Metodo para agregar un reservador al conjunto de reservadores.
     *
     * @param reservador Objeto Reservador a ser agregado.
     */

    public void agregaReservador(Reservador reservador) {
        reservadores.add(reservador);
    }

    /**
     * Metodo para agregar un aula al mapa de aulas.
     *
     * @param aula Objeto Aula a ser agregado.
     */

    public void agregaAula(Aula aula) {
        aulas.put(aula.getNum(), aula);
    }

    /**
     * Metodo que filtra y muestra las aulas de un determinado piso que tienen
     * reservas con un codigo especifico.
     *
     * @param piso          Numero de piso a filtrar.
     * @param codReservador Código a buscar en las reservas de las aulas.
     */

     public void muestraAulas() {
        for (Aula aulaCons : aulas.values()) {
            aulaCons.muestra();
        }
    }

    public Map<Integer, Aula> filtraAulasPorPiso(int piso){
        int aulaInicial = piso * 100;            
        int aulaFinal = aulaInicial + 99;  
        Map<Integer, Aula> subAulas = aulas.subMap(aulaInicial, aulaFinal);
        return subAulas; 
     }

    public void muestraAulasPorPiso(int piso) {  
        Map<Integer, Aula> subAulas = filtraAulasPorPiso(piso);
        for(Aula aulaCons : subAulas.values()){
            aulaCons.muestra();
        }
    }

    public boolean muestraAulasPorReservador(String codReservador) {
        boolean encontroAlguna = false;
        boolean informaNoEncontro = true;
        for (Aula aulaCons : aulas.values()) {
            encontroAlguna = aulaCons.muestraPorReservador(codReservador);
            if (encontroAlguna && informaNoEncontro) {
                informaNoEncontro = false;
            }
        }
        return informaNoEncontro;
    }

    public boolean muestraAulasPorPisoReservador(String codReservador, int piso){
        boolean encontroAlguna = false;
        boolean informaNoEncontro = true;
        Map<Integer, Aula> subAulas = filtraAulasPorPiso(piso);
        for (Aula aulaCons : subAulas.values()) {
            encontroAlguna = aulaCons.muestraPorReservador(codReservador);
            if (encontroAlguna && informaNoEncontro) {
                informaNoEncontro = false;
            }
        }
        return informaNoEncontro;
    } 
    /**
     * Metodo que busca un reservador por su codigo.
     *
     * @param code Codigo del reservador a buscar.
     * @return Objeto Reservador encontrado.
     * @throws NoEncuentraReservadorException Si no se encuentra el reservador con
     *                                        el codigo especificado.
     */

    public Reservador buscaReservador(String code) throws NoEncuentraReservadorException {
        Iterator<Reservador> itRe = reservadores.iterator();
        Reservador re = null;
        boolean encontroReservador = false;
        while (itRe.hasNext() && !encontroReservador) {
            re = itRe.next();
            if (re.getCodigo().equals(code))
                encontroReservador = true;
        }
        if (!encontroReservador)
            throw new NoEncuentraReservadorException("No se encontro el reservador indicado");
        else
            return re;
    }

    /**
     * Metodo para crear una reserva de asignatura o evento interno.
     *
     * @param re Objeto Reservador que realiza la reserva.
     */

    // ASIGNATURA / EVENTO INTERNO

    public Aula creaReserva(Reservador re) {
        ArrayList<Integer> reservasHechas = new ArrayList<>();
        LocalDate fechaInicial = re.getRangoFecha().getFechaInicial();
        LocalDate fechaFinal = re.getRangoFecha().getFechaFinal();
        RangoHora horario = re.getRangoHora();
        Aula aulaReservada = intentaReservarAula(re, fechaInicial, fechaFinal, horario, reservasHechas);
        return aulaReservada;
    }

    // CURSO DE EXTENSION

    /**
     * Metodo para crear una reserva de curso de extension.
     *
     * @param re       Objeto Reservador que realiza la reserva.
     * @param fechaIni Fecha inicial de la reserva.
     * @param horario  Horario de la reserva.
     */

    public Aula creaReserva(Reservador re, LocalDate fechaIni, RangoHora horario) {
        ArrayList<Integer> reservasHechas = new ArrayList<>();
        LocalDate fechaFinal = fechaIni.plusDays(7L * re.getCantClases());
        Aula aulaReservada = intentaReservarAula(re, fechaIni, fechaFinal, horario, reservasHechas);
        return aulaReservada;
    }

    // EVENTO EXTERNO

    /**
     * Metodo para crear una reserva de evento externo.
     *
     * @param re    Objeto Reservador que realiza la reserva.
     * @param costo Costo del alquiler para el evento externo.
     * @param nom   Nombre de la organización que realiza el evento.
     */

    public Aula creaReserva(Reservador re, float costo, String nom) {
        // Pone sus atributos
        re.setCosto(costo);
        re.setNombre(nom);
        ArrayList<Integer> reservasHechas = new ArrayList<>();
        LocalDate fechaIncicial, fechaFinal;
        fechaIncicial = fechaFinal = re.getRangoFecha().getFechaFinal();
        RangoHora horario = re.getRangoHora();
        Aula aulaReservada = intentaReservarAula(re, fechaIncicial, fechaFinal, horario, reservasHechas);
        return aulaReservada;
    }

    public Aula intentaReservarAula(Reservador re, LocalDate fechaInicial, LocalDate fechaFinal, RangoHora horario,
            ArrayList<Integer> reservasHechas) {
        LocalDate fechaAct;
        int codReservaActual;
        Aula aulaReservada = null;
        ComparadorPorCapacidadAula comparador = new ComparadorPorCapacidadAula();
        TreeSet<Aula> aulasPorCapacidad = new TreeSet<>(comparador);
        aulasPorCapacidad.addAll(aulas.values());
        Iterator<Aula> itRe = aulasPorCapacidad.iterator();
        Aula aulaAct = null;
        while (itRe.hasNext() && aulaReservada == null) {
            aulaAct = itRe.next();
            if (aulaAct.getCapMax() >= re.getInscriptos()) {
                try {
                    fechaAct = fechaInicial;
                    while (!fechaAct.isAfter(fechaFinal)) {
                        // Reservamos
                        codReservaActual = aulaAct.poneReserva(re, fechaAct, horario);
                        // Avanzamos
                        fechaAct = fechaAct.plusDays(7);
                        reservasHechas.add(codReservaActual);
                    }
                    // Si salio del while es que no tiro excepcion y reservo todo
                    aulaReservada = aulaAct;
                }
                // Si alguna no se pudo reservar
                catch (NoDisponibilidadException e) {
                    System.out.println(e.getMessage());
                    // Desreservamos
                    for (int codReserva : reservasHechas) {
                        aulaAct.eliminaReserva(codReserva);
                    }
                    reservasHechas.clear();
                    // Nos movemos
                }
            }
        }
        return aulaReservada;
    }

    /**
     * Metodo para generar un reporte del monto recaudado por aulas.
     *
     * @throws IOException Si hay un problema al escribir el archivo de reporte.
     */

    public Aula cancelareserva(int numaula, int codres) {
        Aula aulaEncontrada = aulas.get(numaula); // tenemos referencia al objeto encontrado
        if(aulaEncontrada != null)
            aulaEncontrada.eliminaReservaUnica(codres);// emma te queremos musho, meli te queremos musho, valen te queremos musho
        return aulaEncontrada; 
    }

    public void listarEntidades() {
        for (Reservador res : reservadores) {
            System.out.println(res);
        }
    }

    public void generaReportes(Universidad uni, Scanner sc) throws IOException {
        int opcionSeleccionada = 0;
        List<String> reporte;
        do {
            opcionSeleccionada = sc.nextInt();
            switch (opcionSeleccionada) {
                case 1:
                    reporte = Reportes.reporteMontoRecaudado(aulas);
                    Reportes.escribirReporteEnArchivo(reporte, "ReporteMonto.txt");
                    Reportes.mostrarReporteEnConsola(reporte);
                    break;
                case 2:
                    reporte = Reportes.reporteListadoAulas(aulas);
                    Reportes.escribirReporteEnArchivo(reporte, "ReporteListado.txt");
                    Reportes.mostrarReporteEnConsola(reporte);
                    break;
                case 3:
                    Menu.despliegaMenu(uni);
                    break;
                default:
                    System.out.println("Valor inesperado, ingrese otra opcion");
            }
        } while (opcionSeleccionada != 3);
    }
}