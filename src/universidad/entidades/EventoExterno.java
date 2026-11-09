package universidad.entidades;
import java.time.LocalDate;

/**
 * Clase que representa un Evento Externo en la universidad.
 * Extiende la clase Reservador e implementa Serializable.
 * Proporciona informacion sobre el evento, incluyendo el codigo, fecha, horario, descripcion,
 * cantidad maxima de participantes y el costo.
 */

public class EventoExterno extends EventoInterno{
    private float costo;

    public EventoExterno(String cod, LocalDate fech, RangoHora hora, int maxPart, float cost){
        super(cod, fech, hora, "", maxPart); 
        costo = cost;
    }

    public void setCosto(float cos){costo = cos;};
    public float getCosto(){return costo;}

    @Override
    public String toString() {
        return super.toString() + "\n\t  . Costo:" + costo + "\n";
    }
}



