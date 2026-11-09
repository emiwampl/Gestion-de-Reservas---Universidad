package excepciones;

public class NoEncuentraReservadorException extends RuntimeException{
    public NoEncuentraReservadorException(String mensaje){
        super(mensaje);
    }
}
