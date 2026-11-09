package ejecutables;
import org.w3c.dom.Document;

import cargador.Cargador;
import persistencia.Persistencia;
import universidad.Universidad;
/** El main va a cargar el archivo, serializar e invocar a la funcion que despliega el menu
 * Autores:
 * - @author Colavita, Melisa.
 * - @author Failla, Emmanuel.
 * - @author Galvan, Valentin.
 * - @author Wampl, Emiliano.
 * @version 23/06/2024
 * */

public class Main {
    public static void main(String[] args) {
        Universidad caece = Persistencia.instanciarUni();
        if (caece == null) {
            // Si la universidad no se pudo instanciar (deserializar), cargar desde el XML y persistir
            Document doc = Cargador.cargarDocumentoDesdeXML("src/universidad.xml");
            caece = Cargador.cargaUniversidad(doc);
            Persistencia.persistirUniversidad(caece);
            System.out.println("Datos cargados desde el XML y serializados exitosamente.");
        } else {
            //Si no, carga los datos desde la serializacion
            System.out.println("Datos cargados desde la serializacion.");
        }
        Menu.despliegaMenu(caece);
    }
}

