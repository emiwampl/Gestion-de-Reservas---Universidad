package universidad;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import excepciones.NoDisponibilidadException;
import universidad.entidades.RangoHora;
import universidad.entidades.Reservador;

/**
 * La clase Aula representa un aula en una universidad, la cual puede tener
 * reservas.
 * Implementa la interfaz Serializable.
 * Añade una reserva al aula si está disponible.
 */

public class Aula implements Serializable, Comparable<Aula> {
    private final int numero;
    private final int capMax;
    private final LinkedHashMap<Integer, Reserva> reservas;

    public Aula(int cap, int n) {
        capMax = cap;
        numero = n;
        reservas = new LinkedHashMap<>();
    }

    public int getPiso() {
        return numero / 100;
    }

    public int getNum() {
        return numero;
    }

    public int getCapMax() {
        return capMax;
    }

    public int getCantReservas() {
        return reservas.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("| Aula ").append(numero).append(" |\n");
        sb.append("* Capacidad Maxima: ").append(capMax).append("\n");
        sb.append("* Reservas:").append("\n");
        for (Map.Entry<Integer, Reserva> entry : reservas.entrySet()) {
            sb.append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || this.getClass()!=obj.getClass()) return false;
        Aula aula = (Aula)obj;
        return this.numero == aula.numero;
    }

    @Override
    public int compareTo(Aula aula){
        return Integer.compare(this.numero, aula.getNum());
    }

    public void eliminaReserva(int codRes) {
        reservas.remove(codRes);
        Reserva.decrementaNumReservas();
    }

    public void eliminaReservaUnica(int codRes) {
        Reserva eliminado = reservas.remove(codRes);
        if (eliminado == null) {
            System.out.println("Reserva no encontrada");
        } else
            System.out.println("Eliminado con exito.\n Reserva eliminada:\n" + eliminado);
    }

    public boolean muestraPorReservador(String codigo) {
        boolean encontroAlguna = false;
        Iterator<Reserva> it = reservas.values().iterator();
        try {
            Reserva r = it.next();
            while (it.hasNext() && !(r.getReservador().getCodigo().equals(codigo))) {
                r = it.next();
            }
            if (r.getReservador().getCodigo().equals(codigo)) {
                if(!encontroAlguna) encontroAlguna = true;
                muestra();
            }
        } catch (NoSuchElementException ignored) {
        }
        return encontroAlguna;
    }

    public void muestra(){
        System.out.println(this);
        muestraReservas();
    }

    public int poneReserva(Reservador re, LocalDate fecha, RangoHora horario) throws NoDisponibilidadException {
        Reserva r;
        if (!consultaDisponibilidad(fecha, horario)) {
            throw new NoDisponibilidadException("No se pudo lograr la reserva para el reservador : " + re.toString()+"Por falta de disponibilidad en el aula " + numero);
        } else {
            r = new Reserva(re, fecha, horario);
            reservas.put(r.getCodigoReserva(), r);
        }
        return r.getCodigoReserva();
    }

    private boolean consultaDisponibilidad(LocalDate fecha, RangoHora rangoH) {
        Iterator<Reserva> it = reservas.values().iterator();
        boolean Disponible = true;
        while (it.hasNext() && Disponible) {
            Reserva r = it.next();
            // Si alguna de las reservas impide el uso del aula ,se descarta
            LocalDate rFecha = r.getFecha();
            RangoHora rRangoH = r.getRangoHorario();
            if ((fecha.equals(rFecha)) && (rangoH.equals(rRangoH))){
                Disponible = false;
            }
        }
        return Disponible;
    }

    private void muestraReservas() {
        Iterator<Reserva> it = reservas.values().iterator();
        Reserva r;
        System.out.println("* Reservas: ");
        while (it.hasNext()) {
            r = it.next();
            System.out.println(r);
        }
    }

    public float getMontoAula() {
        float acumulado = 0;
        for (Reserva r : reservas.values()) {
            acumulado += r.getReservador().getCosto();
        }
        return acumulado;
    }

}
