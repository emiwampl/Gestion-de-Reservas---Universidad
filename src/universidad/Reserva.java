package universidad;

import java.io.Serializable;
import java.time.LocalDate;

import universidad.entidades.RangoHora;
import universidad.entidades.Reservador;

/**
 * Clase que representa una reserva de un aula.
 * Cada reserva tiene un codigo unico, fecha, rango horario y reservador asociado.
 */
public class Reserva implements Serializable, Comparable<Reserva>{
    //Atributos
    private static int numReservas = 0;
    private int codigoReserva;
    private LocalDate fecha;
    private RangoHora rangoHorario;
    private Reservador rs;
    //Constructor
    public Reserva(Reservador res, LocalDate fech, RangoHora rangoHor){
        numReservas++;
        codigoReserva = numReservas;
        rs =res;
        fecha = fech;
        rangoHorario = rangoHor;
    }

    public static void decrementaNumReservas(){--numReservas;}
    public Reservador getReservador(){
        return rs;
    }
    public int getCodigoReserva(){return codigoReserva;}
    public LocalDate getFecha(){return fecha;}
    public RangoHora getRangoHorario(){return rangoHorario;}

    @Override
    public String toString(){
        return "  < Reserva " + codigoReserva + " >\n\t- Fecha: " + fecha + "\n\t- Rango Horario: " + rangoHorario + "\n\t- Reservador: " + rs.toString();
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;
        Reserva reserva = (Reserva)obj;
        return codigoReserva == reserva.codigoReserva;
    }

    @Override
    public int compareTo(Reserva reserva){
        return Integer.compare(codigoReserva, reserva.codigoReserva);
    }
}
