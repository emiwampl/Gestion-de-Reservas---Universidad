package comparadores;
import java.util.Comparator;
import universidad.Aula;

public class ComparadorPorCapacidadAula implements Comparator<Aula>{
    @Override
    public int compare(Aula a1, Aula a2){
        return Integer.compare(a1.getCapMax(), a2.getCapMax());
    }
}
