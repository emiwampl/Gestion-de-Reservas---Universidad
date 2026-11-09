package universidad.entidades;
import java.time.LocalDate;

/**
 * Clase que representa un Evento Interno en la universidad.
 * Extiende la clase Reservador e implementa Serializable.
 * Proporciona informacion sobre el evento, incluyendo el codigo, fecha, horario, descripcion,
 * cantidad moxima de participantes y el costo (que siempre es 0).
 */

public class EventoInterno extends Reservador implements Comparable<EventoInterno>{
    private LocalDate fecha;
    private  String codigo;
    private RangoHora horario;
    private String descripcion;
    private int cantMaxPart;

    public EventoInterno(String cod, LocalDate fech, RangoHora horar, String descrip, int cantMaxPar){
        horario = horar;
        codigo = cod;
        descripcion = descrip;
        cantMaxPart = cantMaxPar;
        fecha = fech;
    }

    public String getCodigo(){
        return codigo;
    }
    public void setCod(String cod){
        this.codigo = cod;
    }
    public String getDesc(){
        return descripcion;
    }
    public void setDesc(String desc){
        this.descripcion = desc;
    }
    public int getCantMaxPart(){
        return cantMaxPart;
    }
    public void setCantMaxPart(int cantMaxPart){
        this.cantMaxPart = cantMaxPart;
    }
    public RangoFechas getRangoFecha(){return new RangoFechas(fecha,fecha);}
    public RangoHora getRangoHora(){return horario;}
    public int getInscriptos(){return cantMaxPart;}

    @Override
    public String toString() {
        return "\n\t  . Horario: " + horario + "\n\t  . Codigo: " + codigo + "\n\t  . Descripcion: " + descripcion + "\n\t  . Cantidad Maxima de Participantes: " + cantMaxPart + "\n\t  . Fecha: " + fecha + "\n";
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;
        EventoInterno eventoInterno = (EventoInterno)obj;
        return codigo == eventoInterno.codigo;
    }

    @Override
    public int compareTo(EventoInterno eventoInterno){
        return codigo.compareTo(eventoInterno.codigo);
    }
}

