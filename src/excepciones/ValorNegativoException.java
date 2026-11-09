package excepciones;

public class ValorNegativoException extends RuntimeException{
    public ValorNegativoException(String mensaje){
        super(mensaje);
    }
}
