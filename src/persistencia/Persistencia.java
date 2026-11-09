package persistencia;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import universidad.Universidad;

import java.io.ObjectInputStream;
import java.io.FileInputStream;

/**
 * Clase que proporciona metodos estaticos para la persistencia de objetos Uni mediante serializacion.
 * Permite la escritura (persistencia) y lectura (instanciacion) de objetos Uni en/desde archivos.
 */

public class Persistencia {
    public static void persistirUniversidad(Universidad uni){
        try {
            FileOutputStream archivoSalida = new FileOutputStream("datosSerializados.file");
            ObjectOutputStream objetoS=new ObjectOutputStream(archivoSalida);
            objetoS.writeObject(uni);
            objetoS.flush();
            objetoS.close();
        }
        catch(Exception e){
            System.out.println("Error al persistir " + e);
        }
    }
    public static Universidad instanciarUni() {
        Universidad uni = null;
        try {
            //abre el archivo
            FileInputStream fis = new FileInputStream("datosSerializados.file");
            ObjectInputStream ois = new ObjectInputStream(fis);
            //lee el objeto del archivo
            uni = (Universidad) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("Excepcion durante la deserializacion: " + e + "\nSe procedera a cargar el archivo desde el xml y serializarlo...");
        }
        return uni;
    }

}
