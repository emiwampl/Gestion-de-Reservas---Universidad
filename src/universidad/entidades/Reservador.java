package universidad.entidades;

import java.io.Serializable;

/**
 * Clase abstracta que define el comportamiento general de un reservador de recursos.
 * Cualquier clase que implemente Reservador debe proporcionar implementaciones para los metodos abstractos.
 */

abstract public class Reservador implements Serializable {
    public abstract String getCodigo();
    public abstract String toString();
    public abstract int getInscriptos();
    public abstract RangoFechas getRangoFecha();
    public abstract RangoHora getRangoHora();
    public float getCosto(){return 0;}
    public int getCantClases(){return 0;}
    public  void setNombre(String nom){}
    public void setCosto(float costo){}
}
