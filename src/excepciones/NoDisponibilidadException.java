package excepciones;

public class NoDisponibilidadException extends RuntimeException{
    public NoDisponibilidadException(String mensaje){
        super(mensaje);
    }
}
