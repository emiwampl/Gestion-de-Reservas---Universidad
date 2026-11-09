package comparadores;
import java.util.Comparator;

import universidad.Aula;

public class ComparadorPorCantReservas implements Comparator<Aula> {
        @Override
        public int compare(Aula a1, Aula a2){
            int resultado = Integer.compare(a2.getCantReservas(),a1.getCantReservas());
            //Si tienen igual cantidad de reservas
            if(resultado == 0){
                //Compara por numero de aula
                resultado = Integer.compare(a2.getNum(),a1.getNum());
            }

            return resultado;
    }
}
