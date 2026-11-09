package universidad.entidades;
import java.io.Serializable;
import java.time.LocalTime;

public class RangoHora implements Comparable<RangoHora>, Serializable {
    //Atributos
    private LocalTime horaInicial;
    private LocalTime horaFinal;
    //Constructor
    public RangoHora(LocalTime inicio, LocalTime fin) {
        horaInicial = inicio;
        horaFinal = fin;
    }
    //Metodos
    public LocalTime getInicio() {return horaInicial;}
    public void setInicio(LocalTime inicio) {
        horaInicial = inicio;
    }
    public LocalTime getFin() {return horaFinal;}
    public void setFin(LocalTime fin) {
        horaFinal = fin;
    }
    @Override
    public String toString() {
        return "\n\t\t  [ Hora inicio: " + horaInicial + " ]\n\t\t  [ Hora fin: " + horaFinal + " ]";
    }
    @Override
    public int compareTo(RangoHora o) {
        // Compara devuelve true si los rangos horarios son equivalentes o comparten un rango horario.
        //Se utilizara mas adelante en la reserva, con tal de compartir un rango horario no se reserva.
        boolean compara = horaInicial.isAfter(o.getInicio())|| horaInicial.equals(o.getInicio()) && horaFinal.isBefore(o.getFin())|| horaFinal.equals(o.getFin());
        //Si no son iguales, se llama al comparador de Local Time y se comparan las horas finales
        return compara ? 0: horaFinal.compareTo(o.getFin());
    }
    @Override
    public boolean equals(Object o) {
        //Si el objeto no es una instancia de RangoHora, recibe directamente nulo
        if(!(o instanceof RangoHora rh)) return false;
        //Si el objeto comparado tiene una hora incial igual o mayor y una hora final igual o menor, se superpondrian por lo que no nos sirve
        return horaInicial.isAfter(rh.getInicio())||horaInicial.equals(rh.getInicio())&& horaFinal.isBefore(rh.getFin()) || horaFinal.equals(rh.getFin());
    }
}